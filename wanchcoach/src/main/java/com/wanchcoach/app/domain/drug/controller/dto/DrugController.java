package com.wanchcoach.app.domain.drug.controller.dto;

import com.wanchcoach.app.domain.drug.repository.DrugRepository;
import com.wanchcoach.app.domain.drug.service.DrugService;
import com.wanchcoach.app.global.api.ApiResult;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.wanchcoach.app.global.api.ApiResult.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/drug")
public class DrugController {

    private final DrugService drugService;

    @GetMapping("/update-drug-db")
    public ApiResult<?> updateDrugInfo() throws IOException, ParseException {
        drugService.updateDrugDb();
        return OK(null);
    }

}
