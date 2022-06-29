package com.fip.cbt.controller.api;

import com.fip.cbt.controller.request.TakenExamRequest;
import com.fip.cbt.model.TakenExam;
import com.fip.cbt.service.TakenExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("/api/v1/exam/taken")
@SecurityRequirement(name = "Bearer Authentication")
public class TakenExamController {

    @Autowired
    TakenExamService takenExamService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "submit exam",description = "This is used to submit a taken exam")
    public TakenExam submit(@Valid @RequestBody TakenExamRequest takenExamRequest){
        return takenExamService.submit(takenExamRequest);
    }

    @GetMapping
    @Operation(summary = "get all taken exam",description = "This is used to get all taken exams (with certain parameters), (works differently based on user role)")
    public List<TakenExam> getAll(){
        return takenExamService.getAll();
    }

    @GetMapping("/{examNumber}")
    @Operation(summary = "get one taken exam",description = "This is used to get a taken exam be id")
    public TakenExam getOne(@PathVariable String examNumber){
        return takenExamService.getOne(examNumber);
    }

    @DeleteMapping("/{examId}")
    @Operation(summary = "delete taken exam",description = "This is used to delete a taken exam by id")
    public void delete(@PathVariable Long examId){
        takenExamService.delete(examId);
    }

//    @PutMapping
//    public void update(@Valid @RequestBody UpdateExamTakenRequest updateExamTakenRequest){
//        examTakenService.update(updateExamTakenRequest);
//    }
}
