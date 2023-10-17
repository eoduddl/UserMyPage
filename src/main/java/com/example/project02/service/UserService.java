package com.example.project02.service;

import com.example.project02.converter.UserConverter;
import com.example.project02.entity.User;
import com.example.project02.dto.SmsCertification;
import com.example.project02.dto.UserRequest;
import com.example.project02.repository.UserRepository;
import com.example.project02.service.sms.SmsCertificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    UserConverter userConverter = new UserConverter();
    private final SmsCertificationService smsCertificationService;


    public User findByEmail(String email) {
        var isUser = userRepository.findByEmail(email);
        return isUser.orElse(null);
    }

    public void signup(UserRequest userRequest) {
        isValidPassword(userRequest.getPassword());
        userRequest.setPassword(sha256(userRequest.getPassword()));
        userRequest.setAddress(userRequest.getAddress() + " " + userRequest.getDetailedAddress());
        User NewUser = userConverter.toEntity(userRequest);

        SmsCertification smsCertification = new SmsCertification(userRequest.getPhone(), userRequest.getCertificationNumber());
        if (!smsCertificationService.isVerify(smsCertification)) {
            throw new RuntimeException("SMS 인증에 실패했습니다.");
        }
        userRepository.save(NewUser);
    }

    // 회원가입시 비밀번호 검증
    public void isValidPassword(String password) {
        // 비밀번호 길이 체크 (8 ~ 20 자)
        if (password.length() < 8 || password.length() > 20) {
            throw new IllegalArgumentException("비밀번호는 8~20자 사이여야 합니다.");
        }

        boolean hasLetter = false;
        boolean hasDigit = false;

        // 영문자와 숫자의 조합인지 체크
        for (char ch : password.toCharArray()) {
            if (Character.isLetter(ch)) {
                hasLetter = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            }
        }

        if (!hasLetter || !hasDigit) {
            throw new IllegalArgumentException("비밀번호는 영문자와 숫자의 조합이어야 합니다.");
        }
    }

    public String sha256(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            try (Formatter formatter = new Formatter()) {
                for (byte b : hash) {
                    formatter.format("%02x", b);
                }
                return formatter.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public User findByEmailAndPassword(String email, String password) {
        var isUser = userRepository.findFirstByEmailAndPasswordOrderByIdDesc(email, password);
        return isUser.orElse(null);
    }

    public ResponseEntity<?> validateUser(UserRequest userRequest) {
        Map<String, String> responseBody = new HashMap<>();

        if (findByEmail(userRequest.getEmail()) == null) {
            responseBody.put("error", "가입된 이메일이 아닙니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
        } else if (findByEmailAndPassword(userRequest.getEmail(), userRequest.getPassword()) == null) {
            responseBody.put("error", "잘못된 비밀번호 입니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
        }

        return ResponseEntity.ok(responseBody);
    }

    public void withdrawal(UserRequest userRequest) {
        var existingUser = userRepository.findByEmailAndStatus(userRequest.getEmail(), "REGISTERED");
        if (existingUser.isPresent()) {
            User deleteUser = existingUser.get();
            deleteUser.setStatus("delete");
            userRepository.save(deleteUser);
        }
    }
}
