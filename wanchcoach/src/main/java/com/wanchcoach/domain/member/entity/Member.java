package com.wanchcoach.domain.member.entity;

import com.wanchcoach.domain.auth.application.OAuthProvider;
import com.wanchcoach.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


import java.time.LocalDate;

@Entity
@Getter
@Setter
@SuperBuilder
@ToString
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    private String loginId;

    private String encryptedPwd;

    private String name;

    private String email;

    private LocalDate birthday;

    private String gender;

    private String phoneNumber;

    private boolean active;

    private boolean loginType;

    private String refreshToken;

    private boolean locationPermission;

    private boolean callPermission;

    private boolean cameraPermission;

    private OAuthProvider oAuthProvider;

    public void updateRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

//    public void modify(MemberModifyRequest memberModifyRequest){
//        //getNickName으로 수정
//        this.nickName = memberModifyRequest.getEmail();
//    }

//    public void modifyProfileImg(String imgPath) {
//        this.profileImg = imgPath;
//    }

}
