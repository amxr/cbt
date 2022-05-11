package com.fip.cbt.model;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@Accessors(chain = true)
public class QuestionResponse extends Question{
    private String userChoice;
    
    private boolean isCorrect;
}
