package com.fip.cbt.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document("examscollection")
@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Exam {
    @Id
    private String id;
    @JsonProperty("exam_number")
    private String examNumber;
    private String name;
    @JsonProperty("pass_mark")
    private double passMark;
    private String description;
    private String instructions;
    private LocalDateTime start;
    private int duration;
    @JsonProperty("timed")
    private boolean isTimed;
    private List<Question> questions;
    @CreatedDate
    private LocalDateTime created;
}
