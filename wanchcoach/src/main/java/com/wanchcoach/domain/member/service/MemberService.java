package com.wanchcoach.domain.member.service;

import com.wanchcoach.domain.auth.tokens.AuthTokenGenerator;
import com.wanchcoach.domain.auth.tokens.AuthTokens;
import com.wanchcoach.domain.member.controller.response.*;
import com.wanchcoach.domain.member.entity.DrugAdministrationTime;
import com.wanchcoach.domain.member.entity.Member;
import com.wanchcoach.domain.member.repository.DrugAdministrationTimeRepository;
import com.wanchcoach.domain.member.repository.MemberRepository;
import com.wanchcoach.domain.member.service.dto.*;
import com.wanchcoach.global.api.ApiResult;
import com.wanchcoach.global.error.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Service
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private DefaultMessageService messageService;
    private final AuthTokenGenerator authTokenGenerator;
    private final DrugAdministrationTimeRepository drugAdministrationTimeRepository;

    @Value("${sms.api-key}")
    private String apiKey;

    @Value("${sms.secret-key}")
    private String secretKey;

    @Value("${sms.domain}")
    private String smsDomain;

    public MemberService(MemberRepository memberRepository, AuthTokenGenerator authTokenGenerator, DrugAdministrationTimeRepository drugAdministrationTimeRepository) {
        this.memberRepository = memberRepository;
        this.authTokenGenerator = authTokenGenerator;
        this.drugAdministrationTimeRepository = drugAdministrationTimeRepository;
    }
    @PostConstruct
    private void initializeMessageService(){
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, secretKey, smsDomain);
    }

    public ApiResult<MemberInfoResponse> signup(MemberSignupDto memberSignupDto) {
        String pwd = memberSignupDto.pwd();

        String encryptPwd = makeEncryptPwd(pwd, memberSignupDto.loginId());


        Member member = memberRepository.save(memberSignupDto.toEntity(encryptPwd));
        MemberInfoResponse response = MemberInfoResponse.of(member);
        return ApiResult.OK(response);
    }

    private String makeEncryptPwd(String pwd, String loginId) {
        String encryptPwd = "";

        String salt = loginId + secretKey;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update((pwd + salt).getBytes());
            byte[] pwdsalt = md.digest();

            StringBuffer sb = new StringBuffer();
            for(byte b: pwdsalt){
                sb.append(String.format("%02x", b));
            }
            encryptPwd = sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return encryptPwd;
    }


    public ApiResult<MemberInfoResponse> getMemberInfo(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId);
        return ApiResult.OK(MemberInfoResponse.of(member));
    }
    @Transactional
    public ApiResult<Void> leaveMember(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId);
        member.updateLeave();
        return ApiResult.OK(null);
    }
    public ApiResult<Boolean> idDuplicateCheck(String id) {
        String loginId = id;
        boolean response = !memberRepository.existsByLoginId(loginId);
        log.info("response : {}", String.valueOf(response));
        return ApiResult.OK(response);
    }

    public ApiResult<String> sendSMS(String phoneNumber) {
        Message message = new Message();
        message.setFrom("01072260214");
        message.setTo(phoneNumber);
        String randomCode = String.valueOf(new Random().nextInt(1000000));
        message.setText("본인확인 인증번호 (" + randomCode + ")을 입력해주세요.");

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));

        return ApiResult.OK(randomCode);
    }

    @Transactional
    public ApiResult<AuthTokens> login(MemberLoginDto memberLoginDto) {
        String encryptedPwd = makeEncryptPwd(memberLoginDto.pwd(), memberLoginDto.loginId());
        log.info(memberLoginDto.loginId());
        log.info(encryptedPwd);
        Member member = memberRepository.findByLoginIdAndEncryptedPwd(memberLoginDto.loginId(), encryptedPwd)
                .orElseThrow(() -> new NotFoundException(Member.class, (memberLoginDto.loginId())+"를 찾을 수 없습니다."));
        AuthTokens authTokens = authTokenGenerator.generate(member.getMemberId());
        member.updateRefreshToken(authTokens.getRefreshToken());

        return ApiResult.OK(authTokens);
    }

    public ApiResult<AlarmSeleteResponse> selectAlarm(Long memberId) {
        DrugAdministrationTime drugAdministrationTime = drugAdministrationTimeRepository.findByMemberMemberId(memberId);
        return ApiResult.OK(AlarmSeleteResponse.of(drugAdministrationTime));
    }

    @Transactional
    public ApiResult<AlarmSeleteResponse> updateAlarm(AlarmUpdateDto alarmUpdateDto) {
        DrugAdministrationTime drugAdministrationTime = drugAdministrationTimeRepository.findByMemberMemberId(alarmUpdateDto.memberId());
        drugAdministrationTime.update(alarmUpdateDto);
        return ApiResult.OK(AlarmSeleteResponse.of(drugAdministrationTime));
    }

    public void addDefaultAlarm(AlarmUpdateDto alarmUpdateDto){
        Member member = memberRepository.findByMemberId(alarmUpdateDto.memberId());
        DrugAdministrationTime drugAdministrationTime = AlarmUpdateDto.toEntity(member, alarmUpdateDto);
        drugAdministrationTimeRepository.save(drugAdministrationTime);
    }

    public ApiResult<LocationPermissionResponse> selectLocationPermission(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId);
        return ApiResult.OK(LocationPermissionResponse.of(member));
    }
    @Transactional
    public ApiResult<LocationPermissionResponse> updateLocationPermission(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId);
        member.updateLocation();
        return ApiResult.OK(LocationPermissionResponse.of(member));
    }


    public ApiResult<CallPermissionResponse> selectCallPermission(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId);
        return ApiResult.OK(CallPermissionResponse.of(member));
    }
    @Transactional
    public ApiResult<CallPermissionResponse> updateCallPermission(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId);
        member.updateCall();
        System.out.println(
                member.isCallPermission());
        return ApiResult.OK(CallPermissionResponse.of(member));
    }

    public ApiResult<CameraPermissionResponse> selectCameraPermission(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId);
        return ApiResult.OK(CameraPermissionResponse.of(member));
    }

    @Transactional
    public ApiResult<CameraPermissionResponse> updateCameraPermission(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId);
        member.updateCamera();
        return ApiResult.OK(CameraPermissionResponse.of(member));
    }

    public ApiResult<AlarmPermissionResponse> selectAlarmPermission(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId);
        return ApiResult.OK(AlarmPermissionResponse.of(member));
    }

    public ApiResult<AlarmPermissionResponse> updateAlarmPermission(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId);
        member.updateAlarm();
        return ApiResult.OK(AlarmPermissionResponse.of(member));
    }
    public ApiResult<FindIdResponse> findMemberId(FindMemberLoginIdDto findMemberLoginIdDto) {
        Member member = memberRepository.findByNameAndPhoneNumberAndBirthDate(
                findMemberLoginIdDto.name(), findMemberLoginIdDto.phoneNumber(), findMemberLoginIdDto.birthDate()
        );
        return ApiResult.OK(FindIdResponse.of(member.getLoginId()));
    }

    @Transactional
    public ApiResult<MemberInfoResponse> updateMemberInfo(MemberUpdateInfoDto memberUpdateInfoDto) {
        Member member = memberRepository.findByMemberId(memberUpdateInfoDto.memberId());
        member.updateMemberInfo(memberUpdateInfoDto);
        return ApiResult.OK(MemberInfoResponse.of(member));
    }

    public ApiResult<AuthTokens> changePwdAuth(ChangePwdInfoDto changePwdInfoDto) {
        Member member = memberRepository.findByNameAndLoginIdAndPhoneNumberAndBirthDate(
                changePwdInfoDto.name(), changePwdInfoDto.loginId(), changePwdInfoDto.phoneNumber(),
                changePwdInfoDto.birthDate()
        );

        AuthTokens authTokens = authTokenGenerator.generate(member.getMemberId());
        return ApiResult.OK(authTokens);
    }

    @Transactional
    public void changePwd(ChangePwdDto changePwdDto) {
        Member member = memberRepository.findByMemberId(changePwdDto.MemberId());
        member.updatePwd(changePwdDto.pwd());
    }

    @Transactional
    public ApiResult<Void> updateDeviceToken(Long memberId, String deviceToken){
        Member member = memberRepository.findByMemberId(memberId);
        member.updateDeviceToken(deviceToken);
        return ApiResult.OK(null);
    }

    @Transactional
    public ApiResult<Void> signout(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId);
        member.clearRefreshToken();
        return ApiResult.OK(null);
    }
}
