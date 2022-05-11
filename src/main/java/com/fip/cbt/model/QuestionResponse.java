package com.fip.cbt.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class QuestionResponse{
    @NotBlank(message = "Question text cannot be blank!")
    private String text;

    @Min(value = 1, message = "minimum value for point is 1")
    private double point;

    @NotEmpty(message = "Options cannot be empty!")
    private List<@NotBlank(message = "No blank options allowed!") String> options;

    @NotBlank(message = "Answer cannot be blank!")
    private String answer;

    @NotBlank(message = "Answer cannot be blank!")
    @JsonProperty("user_choice")
    private String userChoice;

    @NotNull
    @JsonProperty("correct")
    private boolean isCorrect;
}
