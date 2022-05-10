package com.fip.cbt.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fip.cbt.model.QuestionResponse;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ExamTakenRequest {
    
    @NotBlank(message = "Username cannot be blank!")
    private String username;
    
    @NotBlank(message = "ExamId cannot be blank!")
    private String examId;
    
    private Set<@Valid QuestionResponse> responseSet;
    
    private boolean isPassed;
    
    @Min(1)
    private int totalPoints;
    
    private LocalDateTime submissionDate;
}
