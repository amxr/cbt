package com.fip.cbt.controller.api;

import com.fip.cbt.controller.request.ExamTakenRequest;
import com.fip.cbt.model.ExamTaken;
import com.fip.cbt.service.ExamTakenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/exam/taken")
@SecurityRequirement(name = "cbt")
public class ExamTakenController {
    
    @Autowired
    ExamTakenService examTakenService;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "This is used to submit a taken exam")
    public ExamTaken submit(@Valid @RequestBody ExamTakenRequest examTakenRequest){
        return examTakenService.add(examTakenRequest);
    }
    
    @GetMapping
    @Operation(summary = "This is used to get all taken exams (with certain parameters), (works differently based on user role)")
    public List<ExamTaken> getAll(@RequestParam(required = false, defaultValue = "") String examNumber){
        return examTakenService.getAll(examNumber);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "This is used to get a taken exam be id")
    public ExamTaken getOne(@PathVariable String id){
        return examTakenService.getOne(id);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "This is used to delete a taken exam by id")
    public void delete(@PathVariable String id){
        examTakenService.delete(id);
    }
}
