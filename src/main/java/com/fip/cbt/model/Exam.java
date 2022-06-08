//package com.fip.cbt.model;
//
//import com.fasterxml.jackson.annotation.JsonIncludeProperties;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.databind.PropertyNamingStrategy;
//import com.fasterxml.jackson.databind.annotation.JsonNaming;
//import lombok.Data;
//import lombok.experimental.Accessors;
//import org.hibernate.annotations.CreationTimestamp;
//
//import javax.persistence.*;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Set;
//
//@Entity
//@Data
//@Accessors(chain = true)
//@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
//public class Exam {
//    @Id
//    @SequenceGenerator(name = "exam_sequence", allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exam_sequence")
//    private long id;
//
//    @ManyToOne
//    @JsonIncludeProperties({"id", "email", "name"})
//    private User owner;
//
//    @JsonProperty("exam_number")
//    private String examNumber;
//
//    private String name;
//
//    @JsonProperty("pass_mark")
//    private double passMark;
//
//    private String description;
//
//    private String instructions;
//
//    private LocalDateTime start;
//
//    @JsonProperty("registered_candidates")
//    @JsonIncludeProperties({"id", "email", "name"})
//    private Set<User> registeredCandidates;
//
//    @JsonIncludeProperties({"id", "email", "name"})
//    private Set<User> candidates;
//
//    private int duration;
//
//    @JsonProperty("timed")
//    private boolean isTimed;
//
//    @JsonProperty("open")
//    private boolean isOpen;
//
//    private List<Question> questions;
//
//    @CreationTimestamp
//    private LocalDateTime created;
//}
