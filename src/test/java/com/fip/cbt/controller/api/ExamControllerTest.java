package com.fip.cbt.controller.api;

import com.fip.cbt.CbtApplication;
import com.fip.cbt.repository.ExamRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = {CbtApplication.class})
class ExamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExamRepository examRepository;

    private final String URI = "/api/v1/test";

    @AfterEach
    void tearDown() {
        examRepository.deleteAll();
    }

    @Test
    void add() {
    }

    @Test
    void getAll() {
    }

    @Test
    void getOne() {
    }

    @Test
    void delete() {
    }

    @Test
    void update() {
    }
}