package com.mourad.school_management.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subjects")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    private Double coefficient;

    @ManyToMany(mappedBy = "subjects")
    private List<Teacher> teachers;

    @OneToMany(mappedBy = "subject")
    private List<Note> notes;

    @OneToMany(mappedBy = "subject")
    private List<Schedule> schedules;
}