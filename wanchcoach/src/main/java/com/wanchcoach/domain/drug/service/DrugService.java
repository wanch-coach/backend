package com.wanchcoach.domain.drug.service;

import com.wanchcoach.domain.drug.entity.Drug;
import com.wanchcoach.domain.drug.entity.DrugImage;
import com.wanchcoach.domain.drug.repository.DrugImageRepository;
import com.wanchcoach.domain.drug.repository.DrugRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
//@Transactional
@Slf4j
public class DrugService {

    private final DrugRepository drugRepository;
    private final DrugImageRepository drugImageRepository;

    @Value("${data.this-medicine}")
    private String serviceKey;

    @Value("${data.drug-upload-link}")
    private String uploadLink;

//    public void updateNormalDrug() throws IOException, ParseException {
//
//        for(int i=1; i<=48; i++) {
//
//            String link = "http://apis.data.go.kr/1471000/DrbEasyDrugInfoService/getDrbEasyDrugList"; /*URL*/
//            String uri = link + "?"
//                    +"serviceKey="+serviceKey
//                    +"&pageNo="+i
//                    +"&numOfRows=100"
//                    +"&type=json";
//
//            URL url = new URL(uri);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Content-type", "application/json");
//            System.out.println("Response code: " + conn.getResponseCode());
//            BufferedReader rd;
//            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            } else {
//                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//            }
//            StringBuilder sb = new StringBuilder();
//            String line;
//            while ((line = rd.readLine()) != null) {
//                sb.append(line);
//            }
//            rd.close();
//            conn.disconnect();
//            System.out.println(sb.toString());
//
//            JSONParser jsonParser = new JSONParser();
//            JSONObject jsonObject = (JSONObject)jsonParser.parse(sb.toString());
//            JSONObject body = (JSONObject)jsonObject.get("body");
//            JSONArray items = (JSONArray) body.get("items");
//            for(int j=0;j<items.size();j++){
//                JSONObject item = (JSONObject) items.get(j);
//                Drug drug = Drug.builder()
//                        .entpName((String) item.get("entpName"))
//                        .itemName((String) item.get("itemName"))
//                        .itemSeq(Long.parseLong((String)item.get("itemSeq")))
//                        .efcyQesitm((String) item.get("efcyQesitm"))
//                        .useMethodQesitm((String) item.get("useMethodQesitm"))
//                        .atpnWarnQesitm((String) item.get("atpnWarnQesitm"))
//                        .atpnQesitm((String) item.get("atpnQesitm"))
//                        .intrcQesitm((String) item.get("intrcQesitm"))
//                        .seQesitm((String) item.get("seQesitm"))
//                        .depositMethodQesitm((String) item.get("depositMethodQesitm"))
//                        .openDe((String)item.get("openDe"))
//                        .updateDe((String)item.get("updateDe"))
//                        .itemImage((String) item.get("itemImage"))
//                        .build();
//                drugRepository.save(drug);
//            }
//        }
//    }

    public void updateDrugDB() throws IOException, ParseException {

        for(int i=1; i<=480; i++) { //총 약품 47384개

            String link = "http://apis.data.go.kr/1471000/DrugPrdtPrmsnInfoService05/getDrugPrdtPrmsnInq05"; /*URL*/
            String uri = link + "?"
                    +"serviceKey="+serviceKey
                    +"&pageNo="+i
                    +"&numOfRows=100"
                    +"&type=json"
//                    +"&prdlst_Stdr_code=195900043"
                    ;

            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());

            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            System.out.println(sb.toString());

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse(sb.toString());
            JSONObject body = (JSONObject)jsonObject.get("body");
            JSONArray items = (JSONArray) body.get("items");

            for(int j=0;j<items.size();j++){
//            for(int j=0;j<1;j++){
                JSONObject item = (JSONObject) items.get(j);

                String imageDownloadUrl = (String) item.get("BIG_PRDT_IMG_URL");
                byte[] imageBytes = null;

                //이미지가 있는 경우
                if(!imageDownloadUrl.equals("")){

                    imageBytes = downloadImage(imageDownloadUrl); //이미지 바이트

                    System.out.println((String) item.get("ITEM_NAME"));
                    System.out.println(imageBytes.length);

                    // TODO: 2024-06-13 파일 이름 수정, 경로 수정 -> 절대경로 /images
                    String filePath = "/Users/tlsrb/Desktop/test/" + (String)item.get("ITEM_SEQ")+".jpg";

                    try (FileOutputStream fos = new FileOutputStream(filePath)) {
                        fos.write(imageBytes);
                        System.out.println("File written successfully: " + filePath);
                    }catch (IOException e) {
                        System.err.println("Failed to write file: " + e.getMessage());
                    }
                    DrugImage drugImage = DrugImage.builder()
                            .originFileName((String) item.get("ITEM_NAME"))
                            .filePath("/Users/tlsrb/Desktop/test/" + (String)item.get("ITEM_SEQ")+".jpg")
                            .build();

                    drugImageRepository.save(drugImage);

                    // 낱알 이미지 제외 데이터 삽입
                    Drug drug = Drug.builder()
                            .itemSeq(Long.parseLong((String)item.get("ITEM_SEQ")))
                            .drugImage(drugImage)
                            .itemName((String) item.get("ITEM_NAME"))
                            .itemEngName((String)item.get("ITEM_ENG_NAME"))
                            .entpName((String) item.get("ENTP_NAME"))
                            .entpSeq((String) item.get("ENTP_SEQ"))
                            .itemPermitDate((String) item.get("ITEM_PERMIT_DATE"))
                            .spcltyPblc((String) item.get("SPCLTY_PBLC"))
                            .prductType((String) item.get("PRDUCT_TYPE"))
                            .itemIngrName((String) item.get("ITEM_INGR_NAME"))
                            .bigPrdtImgUrl(imageDownloadUrl)
                            .build();

                    drugRepository.save(drug);

                }
                else{
                    Drug drug = Drug.builder()
                            .itemSeq(Long.parseLong((String)item.get("ITEM_SEQ")))
                            .itemName((String) item.get("ITEM_NAME"))
                            .itemEngName((String)item.get("ITEM_ENG_NAME"))
                            .entpName((String) item.get("ENTP_NAME"))
                            .entpSeq((String) item.get("ENTP_SEQ"))
                            .itemPermitDate((String) item.get("ITEM_PERMIT_DATE"))
                            .spcltyPblc((String) item.get("SPCLTY_PBLC"))
                            .prductType((String) item.get("PRODUCT_TYPE"))
                            .itemIngrName((String) item.get("ITEM_INGR_NAME"))
                            .build();
                    System.out.println(drugRepository.save(drug));
                }
            }
        }
    }

    public void updateDrugDetailDB() throws IOException, ParseException{

        for(int i=367; i<=480; i++) { //총 약품 47384개

            List<Drug> drugList = new ArrayList<>();

            String link = "http://apis.data.go.kr/1471000/DrugPrdtPrmsnInfoService05/getDrugPrdtPrmsnDtlInq04"; /*URL*/
            String uri = link + "?"
                    +"serviceKey="+serviceKey
                    +"&pageNo="+i
                    +"&numOfRows=100"
                    +"&type=json"
                    ;

            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());

            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse(sb.toString());
            JSONObject body = (JSONObject)jsonObject.get("body");
            JSONArray items = (JSONArray) body.get("items");

            for (Object o : items) {
                JSONObject item = (JSONObject) o;

                System.out.println((String)item.get("ITEM_SEQ"));

                drugRepository.findByItemSeq(Long.parseLong((String) item.get("ITEM_SEQ"))).ifPresent(drug -> {
                            drug.updateDrugDetail(
                                    (String) item.get("STORAGE_METHOD"),
                                    (String) item.get("VALID_TERM"),
                                    (String) item.get("CHANGE_DATE"),
                                    (String) item.get("EE_DOC_DATA"),
                                    (String) item.get("UD_DOC_DATA"),
                                    (String) item.get("NB_DOC_DATA")
                            );
                            drugList.add(drug);
                        }
                );
            }
            drugRepository.saveAll(drugList);
        }
    }

    private byte[] downloadImage(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        try (InputStream is = url.openStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toByteArray();
        }
    }

    public Resource loadFileAsResource(String fileName) throws Exception {
        try {
            // TODO: 2024-06-14 /images로 수정
            Path filePath = Paths.get("/Users/tlsrb/Desktop/test/").resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new Exception("File not found or not readable: " + fileName);
            }
        } catch (MalformedURLException e) {
            throw new Exception("File path is invalid: " + fileName, e);
        }
    }


}
