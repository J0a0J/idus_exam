package com.example.idus_exam.member;

import com.example.idus_exam.member.model.EmailVerify;
import com.example.idus_exam.member.model.Member;
import com.example.idus_exam.member.model.MemberDto;
import com.example.idus_exam.member.repository.EmailVerifyRepository;
import com.example.idus_exam.member.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final EmailVerifyRepository emailVerifyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    public void sendEmail(String uuid, String email) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("아이디어스 회원 가입을 위한 인증 과정입니다.");
            helper.setText(String.format("""
                <p>아래의 링크를 눌러서 인증해주세요.</p>
                <a href="http://localhost:8080/member/verify?uuid=%s">이메일 인증</a>
                """, uuid), true); // true를 설정하면 HTML 형식으로 보냄

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public MemberDto.SignupResponse signup(MemberDto.SignupRequest dto) {
        String uuid = UUID.randomUUID().toString();
        Member member = memberRepository.save(dto.toEntity(passwordEncoder.encode(dto.getPassword())));
        sendEmail(uuid, member.getEmail());
        emailVerifyRepository.save(EmailVerify.builder()
                .member(member)
                .uuid(uuid)
                .build());
        return MemberDto.SignupResponse.from(member);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByEmail(username)
                .filter(Member::isEnabled)  // enabled = false면 Optional 비움
                .orElseThrow(() -> new DisabledException("계정이 활성화되지 않았습니다."));
    }



    public void verify(String uuid) {
        EmailVerify emailVerify = emailVerifyRepository.findByUuid(uuid).orElseThrow();

        Member member = emailVerify.getMember();

        member.memberVerify();

        memberRepository.save(member);

    }
}
