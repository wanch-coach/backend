package com.wanchcoach.app.domain.drug.service;

import com.wanchcoach.app.domain.drug.entity.Drug;
import com.wanchcoach.app.domain.drug.repository.DrugRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@RequiredArgsConstructor
@Slf4j
public class DrugService {

    private final DrugRepository drugRepository;

    @Value("${data.this-medicine}")
    private String serviceKey;

    public void updateDrugDb() throws IOException, ParseException {

        System.out.println(serviceKey);

        for(int i=1; i<=48; i++) {

            String link = "http://apis.data.go.kr/1471000/DrbEasyDrugInfoService/getDrbEasyDrugList"; /*URL*/
            String uri = link + "?"
                    +"serviceKey="+serviceKey
                    +"&pageNo="+i
                    +"&numOfRows=100"
                    +"&type=json";

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
                JSONObject item = (JSONObject) items.get(j);
                Drug drug = Drug.builder()
                        .entpName((String) item.get("entpName"))
                        .itemName((String) item.get("itemName"))
                        .itemSeq(Long.parseLong((String)item.get("itemSeq")))
                        .efcyQesitm((String) item.get("efcyQesitm"))
                        .useMethodQesitm((String) item.get("useMethodQesitm"))
                        .atpnWarnQesitm((String) item.get("atpnWarnQesitm"))
                        .atpnQesitm((String) item.get("atpnQesitm"))
                        .intrcQesitm((String) item.get("intrcQesitm"))
                        .seQesitm((String) item.get("seQesitm"))
                        .depositMethodQesitm((String) item.get("depositMethodQesitm"))
                        .openDe((String)item.get("openDe"))
                        .updateDe((String)item.get("updateDe"))
                        .itemImage((String) item.get("itemImage"))
                        .build();
                drugRepository.save(drug);
            }
        }
    }
}
