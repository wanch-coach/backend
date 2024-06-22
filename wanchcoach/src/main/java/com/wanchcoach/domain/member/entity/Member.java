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

import com.wanchcoach.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * 회원 엔티티
 *
 * @author 박은규
 */

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String encryptedPwd;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false, length = 20)
    private String gender;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private boolean loginType;

    @Column
    private String refreshToken;

    @Column(nullable = false)
    private boolean locationPermission;

    @Column(nullable = false)
    private boolean callPermission;

    @Column(nullable = false)
    private boolean cameraPermission;

    @Transient
    private OAuthProvider oAuthProvider;

    public void updateRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

    public void updateLocation() {
        this.locationPermission = true;
    }


//    public void modify(MemberModifyRequest memberModifyRequest){
//        //getNickName으로 수정
//        this.nickName = memberModifyRequest.getEmail();
//    }

//    public void modifyProfileImg(String imgPath) {
//        this.profileImg = imgPath;
//    }


}
