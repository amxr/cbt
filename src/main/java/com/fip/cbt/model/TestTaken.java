package com.fip.cbt.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Document(collection = "teststaken")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Accessors(chain = true)
public class TestTaken {
    @Id
    private String id;
    
    private String username;
    
    private String testId;
    
    private Set<QuestionResponse> responseSet;
    
    private boolean isPassed;
    
    private int totalPoints;
    
    private LocalDateTime submissionDate;
}
