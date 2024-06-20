package com.wanchcoach.domain.medical.service;

import com.wanchcoach.domain.medical.controller.dto.response.GetHospitalDataResponse;
import com.wanchcoach.domain.medical.controller.dto.response.GetPharmacyDataResponse;
import com.wanchcoach.domain.medical.entity.Hospital;
import com.wanchcoach.domain.medical.entity.HospitalOpeningHour;
import com.wanchcoach.domain.medical.entity.Pharmacy;
import com.wanchcoach.domain.medical.entity.PharmacyOpeningHour;
import com.wanchcoach.domain.medical.repository.command.HospitalOpeningHourRepository;
import com.wanchcoach.domain.medical.repository.command.HospitalRepository;
import com.wanchcoach.domain.medical.repository.command.PharmacyOpeningHourRepository;
import com.wanchcoach.domain.medical.repository.command.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.time.LocalTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicalService {

    private final HospitalRepository hospitalRepository;
    private final PharmacyRepository pharmacyRepository;
    private final HospitalOpeningHourRepository hospitalOpeningHourRepository;
    private final PharmacyOpeningHourRepository pharmacyOpeningHourRepository;

    @Value("${data.this-medical}")
    private String serviceKey;

    private static String getTagValue(String tag, Element element) {
        if (element.getElementsByTagName(tag).item(0) == null) return null;
        NodeList nList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = (Node) nList.item(0);
        if (nValue == null) return null;
        return nValue.getNodeValue();
    }

    public GetHospitalDataResponse getHospitalData() {
        int numOfRows = 100;
        int numOfPages = (76440/numOfRows)+1;
        int savedHospitals = 0;
        int savedOpeningHours = 0;

        try {
            for (int page = 1; page <= numOfPages; page++) {
                String urlBuilder = "http://apis.data.go.kr/B552657/HsptlAsembySearchService/getHsptlMdcncFullDown" + "?ServiceKey=" + serviceKey +
                        "&pageNo=" + page + "&numOfRows=" + numOfRows;

                Document documentInfo = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(urlBuilder);
                documentInfo.getDocumentElement().normalize();
                NodeList nList = documentInfo.getElementsByTagName("item");

                List<Hospital> hospitals = new ArrayList<>();
                List<HospitalOpeningHour> hospitalOpeningHours = new ArrayList<>();

                for (int i = 0; i < nList.getLength(); i++) {
                    Node node = nList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        Hospital hospital = Hospital.builder()
                                .hospitalId(Long.parseLong(Objects.requireNonNull(getTagValue("rnum", element))))
                                .typeId((Integer) (Objects.requireNonNull(getTagValue("dutyDiv", element)).charAt(0)-64))
                                .type(getTagValue("dutyDivNam", element))
                                .name(getTagValue("dutyName", element))
                                .address(getTagValue("dutyAddr", element))
                                .phoneNumber(getTagValue("dutyTel1", element))
                                .longitude(getTagValue("wgs84Lon", element) == null ? null :
                                        new BigDecimal(getTagValue("wgs84Lon", element))
                                )
                                .latitude(getTagValue("wgs84Lat", element) == null ? null :
                                        new BigDecimal(getTagValue("wgs84Lat", element))
                                )
                                .hasEmergencyRoom(getTagValue("dutyEryn", element) == null ? 2 : Integer.parseInt(getTagValue("dutyEryn", element)))
                                .postCdn(
                                        getTagValue("postCdn1", element) == null || getTagValue("postCdn2", element) == null ? null :
                                        (getTagValue("postCdn1", element)+getTagValue("postCdn2", element)).strip()
                                )
                                .etc(getTagValue("dutyEtc", element))
                                .hpid(getTagValue("hpid", element))
                                .build();

                        System.out.println(hospital);
                        hospitals.add(hospital);

                        int[] hour = new int[2], minute = new int[2];
                        if (getTagValue("dutyTime1s", element) != null && getTagValue("dutyTime1c", element) != null) {
                            hour[0] = Integer.parseInt(getTagValue("dutyTime1s", element).substring(0, 2));
                            hour[1] = Integer.parseInt(getTagValue("dutyTime1c", element).substring(0, 2));
                            minute[0] = Integer.parseInt(getTagValue("dutyTime1s", element).substring(2, 4));
                            minute[1] = Integer.parseInt(getTagValue("dutyTime1c", element).substring(2, 4));
                            HospitalOpeningHour openingHour = HospitalOpeningHour.builder()
                                    .hospital(hospital)
                                    .dayOfWeek(1)
                                    .startTime(LocalTime.of(hour[0] >= 24 ? hour[0]-24 : hour[0], minute[0]))
                                    .endTime(LocalTime.of(hour[1] >= 24 ? hour[1]-24 : hour[1], minute[1]))
                                    .build();
                            hospitalOpeningHours.add(openingHour);
                        } if (getTagValue("dutyTime2s", element) != null && getTagValue("dutyTime2c", element) != null) {
                            hour[0] = Integer.parseInt(getTagValue("dutyTime2s", element).substring(0, 2));
                            hour[1] = Integer.parseInt(getTagValue("dutyTime2c", element).substring(0, 2));
                            minute[0] = Integer.parseInt(getTagValue("dutyTime2s", element).substring(2, 4));
                            minute[1] = Integer.parseInt(getTagValue("dutyTime2c", element).substring(2, 4));
                            HospitalOpeningHour openingHour = HospitalOpeningHour.builder()
                                    .hospital(hospital)
                                    .dayOfWeek(2)
                                    .startTime(LocalTime.of(hour[0] >= 24 ? hour[0]-24 : hour[0], minute[0]))
                                    .endTime(LocalTime.of(hour[1] >= 24 ? hour[1]-24 : hour[1], minute[1]))
                                    .build();
                            hospitalOpeningHours.add(openingHour);
                        } if (getTagValue("dutyTime3s", element) != null && getTagValue("dutyTime3c", element) != null) {
                            hour[0] = Integer.parseInt(getTagValue("dutyTime3s", element).substring(0, 2));
                            hour[1] = Integer.parseInt(getTagValue("dutyTime3c", element).substring(0, 2));
                            minute[0] = Integer.parseInt(getTagValue("dutyTime3s", element).substring(2, 4));
                            minute[1] = Integer.parseInt(getTagValue("dutyTime3c", element).substring(2, 4));
                            HospitalOpeningHour openingHour = HospitalOpeningHour.builder()
                                    .hospital(hospital)
                                    .dayOfWeek(3)
                                    .startTime(LocalTime.of(hour[0] >= 24 ? hour[0]-24 : hour[0], minute[0]))
                                    .endTime(LocalTime.of(hour[1] >= 24 ? hour[1]-24 : hour[1], minute[1]))
                                    .build();
                            hospitalOpeningHours.add(openingHour);
                        } if (getTagValue("dutyTime4s", element) != null && getTagValue("dutyTime4c", element) != null) {
                            hour[0] = Integer.parseInt(getTagValue("dutyTime4s", element).substring(0, 2));
                            hour[1] = Integer.parseInt(getTagValue("dutyTime4c", element).substring(0, 2));
                            minute[0] = Integer.parseInt(getTagValue("dutyTime4s", element).substring(2, 4));
                            minute[1] = Integer.parseInt(getTagValue("dutyTime4c", element).substring(2, 4));
                            HospitalOpeningHour openingHour = HospitalOpeningHour.builder()
                                    .hospital(hospital)
                                    .dayOfWeek(4)
                                    .startTime(LocalTime.of(hour[0] >= 24 ? hour[0]-24 : hour[0], minute[0]))
                                    .endTime(LocalTime.of(hour[1] >= 24 ? hour[1]-24 : hour[1], minute[1]))
                                    .build();
                            hospitalOpeningHours.add(openingHour);
                        } if (getTagValue("dutyTime5s", element) != null && getTagValue("dutyTime5c", element) != null) {
                            hour[0] = Integer.parseInt(getTagValue("dutyTime5s", element).substring(0, 2));
                            hour[1] = Integer.parseInt(getTagValue("dutyTime5c", element).substring(0, 2));
                            minute[0] = Integer.parseInt(getTagValue("dutyTime5s", element).substring(2, 4));
                            minute[1] = Integer.parseInt(getTagValue("dutyTime5c", element).substring(2, 4));
                            HospitalOpeningHour openingHour = HospitalOpeningHour.builder()
                                    .hospital(hospital)
                                    .dayOfWeek(5)
                                    .startTime(LocalTime.of(hour[0] >= 24 ? hour[0]-24 : hour[0], minute[0]))
                                    .endTime(LocalTime.of(hour[1] >= 24 ? hour[1]-24 : hour[1], minute[1]))
                                    .build();
                            hospitalOpeningHours.add(openingHour);
                        } if (getTagValue("dutyTime6s", element) != null && getTagValue("dutyTime6c", element) != null) {
                            hour[0] = Integer.parseInt(getTagValue("dutyTime6s", element).substring(0, 2));
                            hour[1] = Integer.parseInt(getTagValue("dutyTime6c", element).substring(0, 2));
                            minute[0] = Integer.parseInt(getTagValue("dutyTime6s", element).substring(2, 4));
                            minute[1] = Integer.parseInt(getTagValue("dutyTime6c", element).substring(2, 4));
                            HospitalOpeningHour openingHour = HospitalOpeningHour.builder()
                                    .hospital(hospital)
                                    .dayOfWeek(6)
                                    .startTime(LocalTime.of(hour[0] >= 24 ? hour[0]-24 : hour[0], minute[0]))
                                    .endTime(LocalTime.of(hour[1] >= 24 ? hour[1]-24 : hour[1], minute[1]))
                                    .build();
                            hospitalOpeningHours.add(openingHour);
                        } if (getTagValue("dutyTime7s", element) != null && getTagValue("dutyTime7c", element) != null) {
                            hour[0] = Integer.parseInt(getTagValue("dutyTime7s", element).substring(0, 2));
                            hour[1] = Integer.parseInt(getTagValue("dutyTime7c", element).substring(0, 2));
                            minute[0] = Integer.parseInt(getTagValue("dutyTime7s", element).substring(2, 4));
                            minute[1] = Integer.parseInt(getTagValue("dutyTime7c", element).substring(2, 4));
                            HospitalOpeningHour openingHour = HospitalOpeningHour.builder()
                                    .hospital(hospital)
                                    .dayOfWeek(7)
                                    .startTime(LocalTime.of(hour[0] >= 24 ? hour[0]-24 : hour[0], minute[0]))
                                    .endTime(LocalTime.of(hour[1] >= 24 ? hour[1]-24 : hour[1], minute[1]))
                                    .build();
                            hospitalOpeningHours.add(openingHour);
                        } if (getTagValue("dutyTime8s", element) != null && getTagValue("dutyTime8c", element) != null) {
                            hour[0] = Integer.parseInt(getTagValue("dutyTime8s", element).substring(0, 2));
                            hour[1] = Integer.parseInt(getTagValue("dutyTime8c", element).substring(0, 2));
                            minute[0] = Integer.parseInt(getTagValue("dutyTime8s", element).substring(2, 4));
                            minute[1] = Integer.parseInt(getTagValue("dutyTime8c", element).substring(2, 4));
                            HospitalOpeningHour openingHour = HospitalOpeningHour.builder()
                                    .hospital(hospital)
                                    .dayOfWeek(8)
                                    .startTime(LocalTime.of(hour[0] >= 24 ? hour[0]-24 : hour[0], minute[0]))
                                    .endTime(LocalTime.of(hour[1] >= 24 ? hour[1]-24 : hour[1], minute[1]))
                                    .build();
                            hospitalOpeningHours.add(openingHour);
                        }
                    }
                }

                hospitalRepository.saveAll(hospitals);
                hospitalOpeningHourRepository.saveAll(hospitalOpeningHours);

                savedHospitals += hospitals.size();
                savedOpeningHours += hospitalOpeningHours.size();
            }

        } catch (UnsupportedEncodingException | MalformedURLException | ProtocolException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        }

        return new GetHospitalDataResponse(savedHospitals, savedOpeningHours);
    }

    public GetPharmacyDataResponse getPharmacyData() {
        String serviceKey = "16fZL%2B8eTFQLjZIgHjTFioQnSWOWSQH0DHQQEptoPyLratGWMhKa%2FRLAZ25s3Ta0gvEr4clGBbe78nexVyFmpg%3D%3D";
        int numOfRows = 100;
        int numOfPages = (24545/numOfRows)+1;
        int savedPharmacies = 0;
        int savedOpeningHours = 0;

        try {
            for (int page = 1; page <= numOfPages; page++) {
                String urlBuilder = "http://apis.data.go.kr/B552657/ErmctInsttInfoInqireService/getParmacyListInfoInqire" + "?ServiceKey=" + serviceKey +
                        "&pageNo=" + page + "&numOfRows=" + numOfRows;

                Document documentInfo = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(urlBuilder);
                documentInfo.getDocumentElement().normalize();
                NodeList nList = documentInfo.getElementsByTagName("item");

                List<Pharmacy> pharmacies = new ArrayList<>();
                List<PharmacyOpeningHour> pharmacyOpeningHours = new ArrayList<>();

                for (int i = 0; i < nList.getLength(); i++) {
                    Node node = nList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        Pharmacy pharmacy = Pharmacy.builder()
                                .pharmacyId(Long.parseLong(Objects.requireNonNull(getTagValue("rnum", element))))
                                .name(getTagValue("dutyName", element))
                                .address(getTagValue("dutyAddr", element))
                                .phoneNumber(getTagValue("dutyTel1", element))
                                .longitude(getTagValue("wgs84Lon", element) == null ? null :
                                        new BigDecimal(getTagValue("wgs84Lon", element))
                                )
                                .latitude(getTagValue("wgs84Lat", element) == null ? null :
                                        new BigDecimal(getTagValue("wgs84Lat", element))
                                )
                                .postCdn(
                                        getTagValue("postCdn1", element) == null || getTagValue("postCdn2", element) == null ? null :
                                                (getTagValue("postCdn1", element)+getTagValue("postCdn2", element)).strip()
                                )
                                .hpid(getTagValue("hpid", element))
                                .build();

                        System.out.println(pharmacy);
                        pharmacies.add(pharmacy);

                        int[] hour = new int[2], minute = new int[2];
                        if (getTagValue("dutyTime1s", element) != null && getTagValue("dutyTime1c", element) != null) {
                            hour[0] = Integer.parseInt(getTagValue("dutyTime1s", element).substring(0, 2));
                            hour[1] = Integer.parseInt(getTagValue("dutyTime1c", element).substring(0, 2));
                            minute[0] = Integer.parseInt(getTagValue("dutyTime1s", element).substring(2, 4));
                            minute[1] = Integer.parseInt(getTagValue("dutyTime1c", element).substring(2, 4));
                            PharmacyOpeningHour openingHour = PharmacyOpeningHour.builder()
                                    .pharmacy(pharmacy)
                                    .dayOfWeek(1)
                                    .startTime(LocalTime.of(hour[0] >= 24 ? hour[0]-24 : hour[0], minute[0]))
                                    .endTime(LocalTime.of(hour[1] >= 24 ? hour[1]-24 : hour[1], minute[1]))
                                    .build();
                            pharmacyOpeningHours.add(openingHour);
                        } if (getTagValue("dutyTime2s", element) != null && getTagValue("dutyTime2c", element) != null) {
                            hour[0] = Integer.parseInt(getTagValue("dutyTime2s", element).substring(0, 2));
                            hour[1] = Integer.parseInt(getTagValue("dutyTime2c", element).substring(0, 2));
                            minute[0] = Integer.parseInt(getTagValue("dutyTime2s", element).substring(2, 4));
                            minute[1] = Integer.parseInt(getTagValue("dutyTime2c", element).substring(2, 4));
                            PharmacyOpeningHour openingHour = PharmacyOpeningHour.builder()
                                    .pharmacy(pharmacy)
                                    .dayOfWeek(2)
                                    .startTime(LocalTime.of(hour[0] >= 24 ? hour[0]-24 : hour[0], minute[0]))
                                    .endTime(LocalTime.of(hour[1] >= 24 ? hour[1]-24 : hour[1], minute[1]))
                                    .build();
                            pharmacyOpeningHours.add(openingHour);
                        } if (getTagValue("dutyTime3s", element) != null && getTagValue("dutyTime3c", element) != null) {
                            hour[0] = Integer.parseInt(getTagValue("dutyTime3s", element).substring(0, 2));
                            hour[1] = Integer.parseInt(getTagValue("dutyTime3c", element).substring(0, 2));
                            minute[0] = Integer.parseInt(getTagValue("dutyTime3s", element).substring(2, 4));
                            minute[1] = Integer.parseInt(getTagValue("dutyTime3c", element).substring(2, 4));
                            PharmacyOpeningHour openingHour = PharmacyOpeningHour.builder()
                                    .pharmacy(pharmacy)
                                    .dayOfWeek(3)
                                    .startTime(LocalTime.of(hour[0] >= 24 ? hour[0]-24 : hour[0], minute[0]))
                                    .endTime(LocalTime.of(hour[1] >= 24 ? hour[1]-24 : hour[1], minute[1]))
                                    .build();
                            pharmacyOpeningHours.add(openingHour);
                        } if (getTagValue("dutyTime4s", element) != null && getTagValue("dutyTime4c", element) != null) {
                            hour[0] = Integer.parseInt(getTagValue("dutyTime4s", element).substring(0, 2));
                            hour[1] = Integer.parseInt(getTagValue("dutyTime4c", element).substring(0, 2));
                            minute[0] = Integer.parseInt(getTagValue("dutyTime4s", element).substring(2, 4));
                            minute[1] = Integer.parseInt(getTagValue("dutyTime4c", element).substring(2, 4));
                            PharmacyOpeningHour openingHour = PharmacyOpeningHour.builder()
                                    .pharmacy(pharmacy)
                                    .dayOfWeek(4)
                                    .startTime(LocalTime.of(hour[0] >= 24 ? hour[0]-24 : hour[0], minute[0]))
                                    .endTime(LocalTime.of(hour[1] >= 24 ? hour[1]-24 : hour[1], minute[1]))
                                    .build();
                            pharmacyOpeningHours.add(openingHour);
                        } if (getTagValue("dutyTime5s", element) != null && getTagValue("dutyTime5c", element) != null) {
                            hour[0] = Integer.parseInt(getTagValue("dutyTime5s", element).substring(0, 2));
                            hour[1] = Integer.parseInt(getTagValue("dutyTime5c", element).substring(0, 2));
                            minute[0] = Integer.parseInt(getTagValue("dutyTime5s", element).substring(2, 4));
                            minute[1] = Integer.parseInt(getTagValue("dutyTime5c", element).substring(2, 4));
                            PharmacyOpeningHour openingHour = PharmacyOpeningHour.builder()
                                    .pharmacy(pharmacy)
                                    .dayOfWeek(5)
                                    .startTime(LocalTime.of(hour[0] >= 24 ? hour[0]-24 : hour[0], minute[0]))
                                    .endTime(LocalTime.of(hour[1] >= 24 ? hour[1]-24 : hour[1], minute[1]))
                                    .build();
                            pharmacyOpeningHours.add(openingHour);
                        } if (getTagValue("dutyTime6s", element) != null && getTagValue("dutyTime6c", element) != null) {
                            hour[0] = Integer.parseInt(getTagValue("dutyTime6s", element).substring(0, 2));
                            hour[1] = Integer.parseInt(getTagValue("dutyTime6c", element).substring(0, 2));
                            minute[0] = Integer.parseInt(getTagValue("dutyTime6s", element).substring(2, 4));
                            minute[1] = Integer.parseInt(getTagValue("dutyTime6c", element).substring(2, 4));
                            PharmacyOpeningHour openingHour = PharmacyOpeningHour.builder()
                                    .pharmacy(pharmacy)
                                    .dayOfWeek(6)
                                    .startTime(LocalTime.of(hour[0] >= 24 ? hour[0]-24 : hour[0], minute[0]))
                                    .endTime(LocalTime.of(hour[1] >= 24 ? hour[1]-24 : hour[1], minute[1]))
                                    .build();
                            pharmacyOpeningHours.add(openingHour);
                        } if (getTagValue("dutyTime7s", element) != null && getTagValue("dutyTime7c", element) != null) {
                            hour[0] = Integer.parseInt(getTagValue("dutyTime7s", element).substring(0, 2));
                            hour[1] = Integer.parseInt(getTagValue("dutyTime7c", element).substring(0, 2));
                            minute[0] = Integer.parseInt(getTagValue("dutyTime7s", element).substring(2, 4));
                            minute[1] = Integer.parseInt(getTagValue("dutyTime7c", element).substring(2, 4));
                            PharmacyOpeningHour openingHour = PharmacyOpeningHour.builder()
                                    .pharmacy(pharmacy)
                                    .dayOfWeek(7)
                                    .startTime(LocalTime.of(hour[0] >= 24 ? hour[0]-24 : hour[0], minute[0]))
                                    .endTime(LocalTime.of(hour[1] >= 24 ? hour[1]-24 : hour[1], minute[1]))
                                    .build();
                            pharmacyOpeningHours.add(openingHour);
                        } if (getTagValue("dutyTime8s", element) != null && getTagValue("dutyTime8c", element) != null) {
                            hour[0] = Integer.parseInt(getTagValue("dutyTime8s", element).substring(0, 2));
                            hour[1] = Integer.parseInt(getTagValue("dutyTime8c", element).substring(0, 2));
                            minute[0] = Integer.parseInt(getTagValue("dutyTime8s", element).substring(2, 4));
                            minute[1] = Integer.parseInt(getTagValue("dutyTime8c", element).substring(2, 4));
                            PharmacyOpeningHour openingHour = PharmacyOpeningHour.builder()
                                    .pharmacy(pharmacy)
                                    .dayOfWeek(8)
                                    .startTime(LocalTime.of(hour[0] >= 24 ? hour[0]-24 : hour[0], minute[0]))
                                    .endTime(LocalTime.of(hour[1] >= 24 ? hour[1]-24 : hour[1], minute[1]))
                                    .build();
                            pharmacyOpeningHours.add(openingHour);
                        }
                    }
                }

                pharmacyRepository.saveAll(pharmacies);
                pharmacyOpeningHourRepository.saveAll(pharmacyOpeningHours);

                savedPharmacies += pharmacies.size();
                savedOpeningHours += pharmacyOpeningHours.size();
            }

        } catch (UnsupportedEncodingException | MalformedURLException | ProtocolException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        }

        return new GetPharmacyDataResponse(savedPharmacies, savedOpeningHours);
    }
}
