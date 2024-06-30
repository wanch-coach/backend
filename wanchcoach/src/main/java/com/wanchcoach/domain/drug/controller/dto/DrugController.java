package com.wanchcoach.domain.drug.controller.dto;

import com.wanchcoach.domain.drug.controller.dto.response.SearchDrugDetailResponse;
import com.wanchcoach.domain.drug.controller.dto.response.SearchDrugsResponse;
import com.wanchcoach.domain.drug.controller.dto.response.SearchFavoritesResponse;
import com.wanchcoach.domain.drug.service.DrugQService;
import com.wanchcoach.domain.drug.service.DrugService;
import com.wanchcoach.domain.drug.service.FavoriteDrugQService;
import com.wanchcoach.domain.drug.service.FavoriteDrugService;
import com.wanchcoach.domain.member.entity.Member;
import com.wanchcoach.global.api.ApiResult;
import com.wanchcoach.global.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;

import static com.wanchcoach.global.api.ApiResult.OK;
import static com.wanchcoach.global.api.ApiResult.ERROR;


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
    //일반/전문 의약품 Detail DB 업데이트
    @GetMapping("/update-detail-drug-info")
    public ApiResult<?> updateDetailDrugDB() throws IOException, ParseException {
        drugService.updateDrugDetailDB();
        return OK(null);
    }

    //파일 ResponseEntity byte[] 반환 테스트
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
    //파일 ApiResult byte[] 반환 테스트
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
    @GetMapping
    public ApiResult<List<SearchDrugsResponse>> searchDrugs(@RequestParam("type") String type, @RequestParam("keyword") String keyword, @AuthenticationPrincipal User user){
        Long memberId = Long.valueOf(user.getUsername());
        List<SearchDrugsResponse> drugList = drugQService.searchDrugs(type, keyword, memberId);
        return OK(drugList);

    }

    //약 상세 조회
    @GetMapping("/{drugId}")
    public ApiResult<?> searchDrugDetail(@PathVariable(value = "drugId") Long drugId){
        try{
            SearchDrugDetailResponse drugDetail = drugQService.searchDrugDetail(drugId);
            return OK(drugDetail);
        }catch(NotFoundException e){
            return ERROR(HttpStatus.NO_CONTENT, e.getMessage());
        }
    }

    // 약 즐겨찾기 목록 조회
    @GetMapping("/favorites")
    public ApiResult<List<SearchFavoritesResponse>> searchFavorites(@AuthenticationPrincipal User user){

        Long memberId = Long.valueOf(user.getUsername());

        List<SearchFavoritesResponse> favoriteList = favoriteDrugQService.searchFavorites(memberId);

        return OK(favoriteList);
    }

    // 약 즐겨찾기 등록
    @PostMapping("/{drugId}/favorites")
    public ApiResult<?> createFavorites(@AuthenticationPrincipal User user, @PathVariable(value = "drugId") Long drugId){

        Long memberId = Long.valueOf(user.getUsername());
        try{
            favoriteDrugService.createFavorite(memberId, drugId);
            return OK(true);
        }catch(NoSuchElementException e){
            return ERROR(HttpStatus.NO_CONTENT, e.getMessage());
        }
    }

    //약 즐겨찾기 삭제
    @DeleteMapping("/favorites/{favoriteId}")
    public ApiResult<Boolean> deleteFavorite(@PathVariable(value = "favoriteId") Long favoriteId){

        favoriteDrugService.deleteFavorite(favoriteId);

        return OK(true);
    }

}
