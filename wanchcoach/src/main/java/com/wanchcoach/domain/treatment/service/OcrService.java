package com.wanchcoach.domain.treatment.service;

import com.wanchcoach.domain.drug.repository.DrugQRepository;
import com.wanchcoach.domain.drug.service.dto.SearchDrugsDto;
import com.wanchcoach.domain.treatment.service.dto.DrugOcrDto;
import com.wanchcoach.domain.treatment.controller.dto.response.PrescriptionOcrResponse;
import com.wanchcoach.global.util.JsonUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * OCR 서비스
 */

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OcrService {

    @Value("${data.this-ocr-url}")
    private String CLOVA_URL;

    @Value("${data.this-ocr}")
    private String SECRET_KEY;

    private String boundary;

    private final DrugQRepository drugQRepository;

    public PrescriptionOcrResponse getPrescriptionInfo(File file) throws IOException, ParseException {

        log.info("OcrService#getPrescriptionInfo called");
        List<String> results;
        try {
            HttpURLConnection conn = createRequestHeader(new URL(CLOVA_URL));
            createRequestBody(conn, file);
            StringBuilder sb = new StringBuilder(getResponseData(conn));
            results = parseResponseData(sb);

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        List<DrugOcrDto> ocrItems = new ArrayList<>();

        List<String> patterns = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\b\\d{9}\\b");

        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            Matcher matcher = pattern.matcher(results.get(i));
            if (matcher.find()) {
                indices.add(i);
            }
        }
        indices.add(results.size());

        // 정렬된 인덱스로 패턴 추출
        for (int i = 0; i < indices.size() - 1; i++) {
            int startIndex = indices.get(i);
            int endIndex = indices.get(i + 1);

            String substring = String.join(" ", results.subList(startIndex, endIndex));
            patterns.add(substring);
        }

        for (String p : patterns) {
            // 1회 투여량, 1일 투여 횟수, 투약일 추출 (숫자와 소수점만 추출하여 double 또는 int로 변환)
            Pattern dosePattern = Pattern.compile("\\s*(.*?)\\s+(\\d*\\.?\\d+)\\s+(\\d+)\\s+(\\d+)");
            Matcher doseMatcher = dosePattern.matcher(p);

            if (doseMatcher.find()) {
                String medicineName = doseMatcher.group(1);
                String oneDose = doseMatcher.group(2); // 1회 투여량
                String dailyDoses = doseMatcher.group(3); // 1일 투약 횟수
                String treatmentDays = doseMatcher.group(4); // 투약일

                if(medicineName.contains("주사제") || medicineName.contains("조제시")) break;
                medicineName = medicineName.replaceFirst("^[^가-힣]*", "");
                medicineName = medicineName.replaceFirst("\\(.*", "").trim();

                // 용법 추출 (투약일 이후 문자열에서 주사제 이전까지)
                String method = "", methodWithDetails = p.substring(doseMatcher.end()).trim();
                if (methodWithDetails.contains("주사제")) {
                    int index = methodWithDetails.indexOf("주사제");
                    method = methodWithDetails.substring(0, index).trim();
                } else if (methodWithDetails.contains("조제시")) {
                    int index = methodWithDetails.indexOf("조제시");
                    method = methodWithDetails.substring(0, index).trim();
                } else {
                    method = methodWithDetails;
                }

                List<SearchDrugsDto> searchResult = drugQRepository.findDrugsContainKeyword("itemName", medicineName);
                if (searchResult == null) {
                    medicineName = medicineName.replaceFirst("\\d.*", "");
                    searchResult = drugQRepository.findDrugsContainKeyword("itemName", medicineName);
                    if (searchResult == null) continue;
                }
                ocrItems.add(new DrugOcrDto(searchResult.get(0).getDrugId(),
                                            searchResult.get(0).getItemName(),
                                            Double.valueOf(oneDose),
                                            Integer.valueOf(dailyDoses),
                                            Integer.valueOf(treatmentDays),
                                            method)
                );
            }
        }

        return new PrescriptionOcrResponse(ocrItems);
    }

    private HttpURLConnection createRequestHeader(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setReadTimeout(5000);
        conn.setRequestMethod("POST");

        boundary = "----" + UUID.randomUUID().toString().replaceAll("-", "");
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        conn.setRequestProperty("X-OCR-SECRET", SECRET_KEY);

        return conn;
    }

    private void createRequestBody(HttpURLConnection conn, File file) throws IOException {
        JSONObject image = new JSONObject();
        image.put("format", StringUtils.getFilenameExtension(file.getName()));
        image.put("name", file.getName());

        JSONArray images = new JSONArray();
        images.add(image);

        JSONObject request = new JSONObject();
        request.put("version", "V2");
        request.put("requestId", UUID.randomUUID().toString());
        request.put("timestamp", System.currentTimeMillis());
        request.put("lang", "ko");
        request.put("resultType", "string");
        request.put("images", images);

        String postParams = request.toString();
        System.out.println(postParams);

        conn.connect();
        DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
        writeMultiPart(outputStream, postParams, file, boundary);
        outputStream.close();
    }

    private void writeMultiPart(OutputStream out, String jsonMessage, File file, String boundary) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition:form-data; name=\"message\"\r\n\r\n");
        sb.append(jsonMessage);
        sb.append("\r\n");

        out.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        out.flush();

        if (file != null && file.isFile()) {
            out.write(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
            StringBuilder fileString = new StringBuilder();
            fileString
                    .append("Content-Disposition:form-data; name=\"file\"; filename=");
            fileString.append("\"" + file.getName() + "\"\r\n");
            fileString.append("Content-Type: application/octet-stream\r\n\r\n");
            out.write(fileString.toString().getBytes(StandardCharsets.UTF_8));
            out.flush();

            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[8192];
                int count;
                while ((count = fis.read(buffer)) != -1) {
                    out.write(buffer, 0, count);
                }
                out.write("\r\n".getBytes());
            }
            out.write(("--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));
        }
        out.flush();
    }

    private BufferedReader checkResponse(HttpURLConnection conn) throws IOException {
        int responseCode = conn.getResponseCode();
        System.out.println(responseCode);
        BufferedReader reader;

        if (responseCode == 200) {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        return reader;
    }

    private StringBuilder getResponseData(HttpURLConnection conn) throws IOException {
        BufferedReader reader = checkResponse(conn);

        String line;
        StringBuilder response = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();
        return response;
    }

    private List<String> parseResponseData(StringBuilder response) throws ParseException {
        JSONParser responseParser = new JSONParser();
        JSONObject parsedData = (JSONObject) responseParser.parse(response.toString());

        JSONArray parsedImageData = (JSONArray) parsedData.get("images");

        if (parsedImageData != null) {
            JSONObject parsedImage = (JSONObject) parsedImageData.get(0);
            JSONArray parsedTexts = (JSONArray) parsedImage.get("fields");

            List<Map<String, Object>> map = JsonUtils.getListMapFromJsonArray(parsedTexts);
            List<String> result = new ArrayList<>();
            for (Map<String, Object> m: map) {
                result.add((String) m.get("inferText"));
            }
            return result;
        }
        return null;
    }
}
