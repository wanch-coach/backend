package com.wanchcoach.domain.family.controller.request;

import java.time.LocalDate;

public record FamilyAddRequest (
        String name,
        LocalDate birthDate,
        String gender,
        String imageFileName,
        String color
){

}
