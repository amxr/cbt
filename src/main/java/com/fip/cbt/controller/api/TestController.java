package com.fip.cbt.controller.api;

import com.fip.cbt.controller.request.TestRequest;
import com.fip.cbt.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/test")
public class TestController {
    @Autowired
    TestService testService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TestRequest add(@Valid @RequestBody TestRequest testRequest){
        return testRequest;
    }
}
