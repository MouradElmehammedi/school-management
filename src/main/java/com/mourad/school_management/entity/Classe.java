package com.mourad.school_management.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "classes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Classe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String level;

    @OneToMany(mappedBy = "classe")
    private List<Student> students;

    @ManyToOne
    @JoinColumn(name = "main_teacher_id")
    private Teacher mainTeacher;

    @OneToMany(mappedBy = "classe")
    private List<Schedule> schedules;
}
