package com.fip.cbt.controller.api;

import com.fip.cbt.controller.request.TestRequest;
import com.fip.cbt.controller.request.UpdateTestRequest;
import com.fip.cbt.model.Test;
import com.fip.cbt.service.TestService;
import com.fip.cbt.service.TestTakenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/test")
public class TestController {
    @Autowired
    TestService testService;
    
    @Autowired
    TestTakenService testTakenService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Test add(@Valid @RequestBody TestRequest testRequest){
        return testService.add(testRequest);
    }

    @GetMapping
    public List<Test> getAll(){
        return testService.getAll();
    }

    @GetMapping("/{testNumber}")
    public Test getOne(@PathVariable String testNumber){
        return testService.getOne(testNumber);
    }

    @DeleteMapping("/{testNumber}")
    public void delete(@PathVariable String testNumber){
        testService.delete(testNumber);
    }

    @PutMapping
    public void update(@Valid @RequestBody UpdateTestRequest updateTestRequest){
        testService.update(updateTestRequest);
    }
}
