package com.fip.cbt.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document("testcollection")
@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Exam {
    @Id
    private String id;
    private String examNumber;
    private String name;
    private double passMark;
    private String description;
    private String instructions;
    private LocalDateTime start;
    private int duration;
    private boolean isTimed;
    private List<Question> questions;
    private LocalDateTime created;
}
