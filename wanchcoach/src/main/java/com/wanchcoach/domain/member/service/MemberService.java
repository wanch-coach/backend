package com.wanchcoach.domain.member.service;

import com.wanchcoach.domain.member.entity.Member;
import com.wanchcoach.domain.member.repository.MemberRepository;
import com.wanchcoach.domain.member.service.dto.MemberSignupDto;
import com.wanchcoach.global.api.ApiResult;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Random;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private DefaultMessageService messageService;

    @Value("${sms.api-key}")
    private String apiKey;

    @Value("${sms.secret-key}")
    private String secretKey;

    @Value("${sms.domain}")
    private String smsDomain;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    @PostConstruct
    private void initializeMessageService(){
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, secretKey, smsDomain);
    }

    public ApiResult<Member> signup(MemberSignupDto memberSignupDto) {
        return ApiResult.OK(memberRepository.save(memberSignupDto.toEntity()));
    }

    public ApiResult<Boolean> idDuplicateCheck(String id) {
        return ApiResult.OK(memberRepository.findByLoginId(id) == null);
    }

    public ApiResult<String> sendsms(String phoneNumber) {
        Message message = new Message();
        message.setFrom("01072260214");
        message.setTo(phoneNumber);
        String randomCode = String.valueOf(new Random().nextInt(1000000));
        message.setText("본인확인 인증번호 (" + randomCode + ")을 입력해주세요.");

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));

        return ApiResult.OK(randomCode);
    }
}
