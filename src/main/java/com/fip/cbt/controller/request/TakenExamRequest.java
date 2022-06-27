package com.fip.cbt.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fip.cbt.model.QuestionResponse;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TakenExamRequest {
    @NotBlank(message = "Exam number cannot be blank!")
    private String examNumber;

    @NotNull(message = "must include time candidate started exam.")
    private LocalDateTime userStartTime;

    private List<@Valid QuestionResponseRequest> responses;
}
