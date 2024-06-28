package com.wanchcoach.domain.treatment.service.dto;

public record DrugOcrDto(Long drugId, String itemName, Double quantity, Integer frequency, Integer day, String direction) {

}
