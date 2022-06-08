//package com.fip.cbt.model;
//
//import lombok.*;
//import lombok.experimental.Accessors;
//import org.hibernate.Hibernate;
//
//import javax.persistence.*;
//import javax.validation.constraints.Min;
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotEmpty;
//import java.util.List;
//import java.util.Objects;
//
//@Getter
//@Setter
//@ToString
//@RequiredArgsConstructor
//@Entity
//@Accessors(chain = true)
//public class Question {
//    @Id
//    @SequenceGenerator(name = "exam_sequence", allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exam_sequence")
//    private long id;
//
//    @NotBlank(message = "Question text cannot be blank!")
//    private String text;
//
//    @Min(value = 1, message = "minimum value for point is 1")
//    private double point;
//
//    @NotEmpty(message = "Options cannot be empty!")
//    private List<@NotBlank(message = "No blank options allowed!") String> options;
//
//    @NotBlank(message = "Answer cannot be blank!")
//    private String answer;
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
//        Question question = (Question) o;
//        return id > 0 && Objects.equals(id, question.id);
//    }
//
//    @Override
//    public int hashCode() {
//        return getClass().hashCode();
//    }
//}
