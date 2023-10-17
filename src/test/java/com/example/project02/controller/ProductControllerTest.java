package com.example.project02.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("이미지 업로드 테스트")
    public void testImageUpload() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "my_image.jpg",
                "image/jpeg",
                new FileInputStream("/Users/lee/Desktop/Supercoding-project2_back/src/main/resources/images/my_image.jpg")
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/product/image")
                        .file(file))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
