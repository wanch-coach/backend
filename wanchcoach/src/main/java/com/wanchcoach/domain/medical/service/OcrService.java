package com.wanchcoach.domain.medical.service;

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
import org.springframework.web.multipart.MultipartFile;

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

    public PrescriptionOcrResponse getPrescriptionInfo(File file) throws IOException, ParseException {
        List<String> data = Arrays.asList(
                "병", "N300", "처방 의료인", "박희수", "(피부/비뇨기과)", "서명", "분류", "기호",
                "면허종류", "의사", "면허번호", "제", "48607", "호", "환자의", "요구가", "있을", "때에는",
                "질병분류기호를", "적지", "않습니다.", "처방", "의약품의", "명칭", "1회", "1일", "총",
                "부담률", "본인", "용", "법", "투약량투여횟수투약일수", "구분코드", "[2][649900060]",
                "무코스타정", "100mg", "(오츠카)", "1.000", "3", "3", "하루", "3회;식사", "30분", "후",
                "복용", "[2][645401470]", "크라비트정", "100mg(제", "일)18세이하금기", "1.000", "3",
                "3", "주사제", "처방명세([", "]원내", "조제,", "[", "]원외처방)", "조제시", "참고사항본인부담",
                "구분기호", "V252", "사용기간", "발급일부터", "(", "3", ")", "일간", "사용기간내에",
                "약국에", "제출하여야", "합니다.", "의", "약", "품", "조", "제", "명", "세", "조제기관의",
                "명칭", "조제명세", "조제", "약 사", "성명", "(서명", "또는", "날인)", "조제량(조제일수)",
                "조제년월일", "1.", "본인부담률", "구분코드:", "「국민건강보험법", "별표2", "항목설명", "여된",
                "해당", "구분코드를", "적습니다.", "시행령"
        );

        // 리스트 문자열을 하나의 문자열로 연결
        String dataStr = String.join(" ", data);
        System.out.println(dataStr);

        // 9자리 숫자 찾기
        Pattern pattern = Pattern.compile("\\b\\d{9}\\b");
        Matcher matcher = pattern.matcher(dataStr);

        // 필요한 정보를 저장할 리스트
        List<Map<String, String>> drugsInfo = new ArrayList<>();

        while (matcher.find()) {
            String code = matcher.group();
            // 약물 이름과 세부사항 추출을 위한 정규식 패턴
            String regex = code + "\\s+([\\w\\(\\)]+(?:\\s+[\\w\\(\\)]+)*)\\s+(\\d+mg(?:\\s*[\\w\\(\\)]*)*)\\s+([\\d.]+)\\s+(\\d+)\\s+(\\d+)\\s+(.*?)(?=\\s*\\[\\d*\\]|\\s*\\[|\\s*\\d{9}|\\s*$)";
            Pattern infoPattern = Pattern.compile(regex);
            Matcher infoMatcher = infoPattern.matcher(dataStr);

            if (infoMatcher.find()) {
                String drugName = infoMatcher.group(1) + " " + infoMatcher.group(2);
                String dose = infoMatcher.group(3);
                String freqPerDay = infoMatcher.group(4);
                String days = infoMatcher.group(5);
                String usage = infoMatcher.group(6).trim();

                Map<String, String> drugInfo = new HashMap<>();
                drugInfo.put("drug_code", code);
                drugInfo.put("drug_name", drugName);
                drugInfo.put("dose", dose);
                drugInfo.put("freq_per_day", freqPerDay);
                drugInfo.put("days", days);
                drugInfo.put("usage", usage);

                drugsInfo.add(drugInfo);
            }
        }

        // 결과 출력
        for (Map<String, String> info : drugsInfo) {
            System.out.println(info);
        }

        return null;
    }

//        log.info("OcrService#getPrescriptionInfo called");
//        try {
//            HttpURLConnection conn = createRequestHeader(new URL(CLOVA_URL));
//            createRequestBody(conn, file);
//            StringBuilder sb = new StringBuilder(getResponseData(conn));
//            List<String> result = parseResponseData(sb);
//            for (String s: result) {
//                System.out.println(s);
//            }
//
//            return null;
//        } catch (IOException | ParseException e) {
//            throw new RuntimeException(e);
//        }
//    }

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

    private String parseResponseData(StringBuilder response) throws ParseException {
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
            return String.join(" ", result);
        }
        return null;
    }
}
