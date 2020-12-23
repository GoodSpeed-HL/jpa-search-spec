package com.example.demo.domain;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Data
public class Lesson {
    @Id
    @GeneratedValue
    private Long id;

    private String label;

    private BaseStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="course_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Course course;
}
