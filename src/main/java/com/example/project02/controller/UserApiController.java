package com.example.project02.controller;

import com.example.project02.dto.SmsCertification;
import com.example.project02.service.sms.SmsCertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class UserApiController {

    private final SmsCertificationService smsCertificationService;

    @Autowired
    public UserApiController(SmsCertificationService smsCertificationService) {
        this.smsCertificationService = smsCertificationService;
    }

    @PostMapping("/sms-certification/sends")
    public ResponseEntity<Map<String, Object>> sendSms(@RequestBody SmsCertification requestDto) {
        smsCertificationService.sendSms(requestDto.getPhone());
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "SMS sent successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PostMapping("/sms-certification/confirms")
    public ResponseEntity<Map<String, Object>> verifySms(@RequestBody SmsCertification requestDto) {
        smsCertificationService.verifySms(requestDto);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "SMS verified successfully");
        return ResponseEntity.ok(response);
    }

}