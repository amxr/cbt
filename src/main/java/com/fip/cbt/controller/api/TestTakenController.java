package com.fip.cbt.controller.api;

import com.fip.cbt.controller.request.TestRequest;
import com.fip.cbt.controller.request.TestTakenRequest;
import com.fip.cbt.controller.request.UpdateTestRequest;
import com.fip.cbt.controller.request.UpdateTestTakenRequest;
import com.fip.cbt.model.Test;
import com.fip.cbt.model.TestTaken;
import com.fip.cbt.service.TestTakenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/tests/taken")
public class TestTakenController {
    
    @Autowired
    TestTakenService testTakenService;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TestTaken add(@Valid @RequestBody TestTakenRequest testTakenRequest){
        return testTakenService.add(testTakenRequest);
    }
    
    @GetMapping
    public List<TestTaken> getAll(){
        return testTakenService.getAll();
    }
    
    @GetMapping("/{testId}")
    public TestTaken getOne(@PathVariable String testId){
        return testTakenService.getOne(testId);
    }
    
    @DeleteMapping("/{testId}")
    public void delete(@PathVariable String testId){
        testTakenService.delete(testId);
    }
    
    @PutMapping
    public void update(@Valid @RequestBody UpdateTestTakenRequest updateTestTakenRequest){
        testTakenService.update(updateTestTakenRequest);
    }
}
