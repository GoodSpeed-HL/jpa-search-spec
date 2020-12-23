package com.example.demo.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue
    private Long id;

    private String label;

    private BaseStatus status;

    @Column(columnDefinition = "TIMESTAMP")
    private Date startDate;

    @Column(columnDefinition = "TIMESTAMP")
    private Date endDate;

    @OneToMany(mappedBy = "course")
    @JsonIgnore
    private Set<Lesson> lessons;
}
