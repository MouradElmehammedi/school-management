package com.mourad.school_management.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "classes")
public class Classe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String level;

    @Column(nullable = false)
    private Integer capacity;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @OneToMany(mappedBy = "classe")
    private Set<Student> students = new HashSet<>();

    @OneToMany(mappedBy = "classe")
    private Set<Note> notes = new HashSet<>();

    @OneToMany(mappedBy = "classe")
    private Set<Absence> absences = new HashSet<>();

    @OneToMany(mappedBy = "classe")
    private Set<Schedule> schedules = new HashSet<>();
}