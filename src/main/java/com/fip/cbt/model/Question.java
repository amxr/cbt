package com.fip.cbt.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Accessors(chain = true)
public class Question {
    @NotBlank(message = "Question text cannot be blank!")
    private String text;

    @Min(1)
    private int point;

    @NotEmpty(message = "Options cannot be empty!")
    private List<@NotBlank(message = "No blank options allowed!") String> options;

    @NotBlank(message = "Answer cannot be blank!")
    private String answer;
}
