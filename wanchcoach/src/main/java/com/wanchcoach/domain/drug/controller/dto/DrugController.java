package com.wanchcoach.domain.drug.controller.dto;

import com.wanchcoach.domain.drug.controller.dto.response.SearchDrugDetailResponse;
import com.wanchcoach.domain.drug.controller.dto.response.SearchDrugsResponse;
import com.wanchcoach.domain.drug.controller.dto.response.SearchFavoritesResponse;
import com.wanchcoach.domain.drug.service.DrugQService;
import com.wanchcoach.domain.drug.service.DrugService;
import com.wanchcoach.domain.drug.service.FavoriteDrugQService;
import com.wanchcoach.domain.drug.service.FavoriteDrugService;
import com.wanchcoach.global.api.ApiResult;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

import static com.wanchcoach.global.api.ApiResult.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/drug")
public class DrugController {

    private final DrugService drugService;
    private final DrugQService drugQService;
    private final FavoriteDrugService favoriteDrugService;
    private final FavoriteDrugQService favoriteDrugQService;


    //일반 의약품 DB 업데이트
    @GetMapping("/update-normal-drug")
    public ApiResult<?> updateNormalDrug() throws IOException, ParseException {
//        drugService.updateNormalDrug();
        return OK(null);
    }

    //일반/전문 의약품 DB 업데이트 및 이미지 저장
    @GetMapping("/update-total-drug-info")
    public ApiResult<?> updateTotalDrugDB() throws IOException, ParseException {
        drugService.updateDrugDB();
        return OK(null);
    }

    @GetMapping("/update-detail-drug-info")
    public ApiResult<?> updateDetailDrugDB() throws IOException, ParseException {
        drugService.updateDrugDetailDB();
        return OK(null);
    }

    @GetMapping("/files/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable(value = "fileName") String fileName) {

        //파일이 저장된 경로
        String savename = "/Users/tlsrb/Desktop/test/"+fileName+".jpg";
        File file = new File(savename);

        //저장된 이미지파일의 이진데이터 형식을 구함
        byte[] result = null;
        ResponseEntity<byte[]> entity=null;
        try {
            result = FileCopyUtils.copyToByteArray(file);
            HttpHeaders header = new HttpHeaders();
            header.add("Content-type",Files.probeContentType(file.toPath())); //파일의 컨텐츠타입을 직접 구해서 header에 저장

            //3. 응답본문
            entity = new ResponseEntity<>(result,header, HttpStatus.OK);//데이터, 헤더, 상태값
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entity;
    }
    @GetMapping("/test")
    public ApiResult<String> downloadFile() {

        //파일이 저장된 경로
        String savename = "/Users/tlsrb/Desktop/test/195900043.jpg";
        File file = new File(savename);

        //저장된 이미지파일의 이진데이터 형식을 구함
        byte[] result = null;
        String str = null;
        try {
            result = FileCopyUtils.copyToByteArray(file);
            str = Base64.getEncoder().encodeToString(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return OK(str);
    }


    // 약품 목록 검색
    @GetMapping("/")
    public ApiResult<List<SearchDrugsResponse>> searchDrugs(@RequestParam("type") String type, @RequestParam("keyword") String keyword){
        List<SearchDrugsResponse> drugList = drugQService.searchDrugs(type, keyword);
        return OK(drugList);

    }

    //약 상세 조회
    @GetMapping("/{drugId}")
    public ApiResult<SearchDrugDetailResponse> searchDrugDetail(@PathVariable(value = "drugId") Long drugId){

        SearchDrugDetailResponse drugDetail = drugQService.searchDrugDetail(drugId);

        return OK(drugDetail);
    }

    // 약 즐겨찾기 목록 조회
    @GetMapping("/favorites")
    public ApiResult<List<SearchFavoritesResponse>> searchFavorites(){

        // TODO: 2024-06-16 사용자 id를 입력받아야함
        Long memberId = 1L;
        List<SearchFavoritesResponse> favoriteList = favoriteDrugQService.searchFavorites(memberId);

        return OK(favoriteList);
    }

    // 약 즐겨찾기 등록
    @PostMapping("/{drugId}/favorites")
    public ApiResult<Boolean> createFavorites(@PathVariable(value = "drugId") Long drugId){

        // TODO: 2024-06-16 사용자 id를 입력받아야함
        Long memberId = 1L;
        favoriteDrugService.createFavorite(memberId, drugId);

        return OK(true);
    }

    //약 즐겨찾기 삭제
    @DeleteMapping("/favorites/{favoriteId}")
    public ApiResult<Boolean> deleteFavorite(@PathVariable(value = "favoriteId") Long favoriteId){

        favoriteDrugService.deleteFavorite(favoriteId);

        return OK(true);
    }

}
