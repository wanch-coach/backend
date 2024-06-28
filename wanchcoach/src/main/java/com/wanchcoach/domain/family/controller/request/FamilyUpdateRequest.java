package com.wanchcoach.domain.family.controller.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class FamilyUpdateRequest {

    Long familyId;
    String name;
    LocalDate birthDate;
    String gender;
    String imageFileName;

}
