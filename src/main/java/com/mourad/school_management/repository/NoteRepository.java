package com.mourad.school_management.repository;

import com.mourad.school_management.entity.Note;
import com.mourad.school_management.entity.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByStudentId(Long studentId);
    List<Note> findByStudentIdAndTerm(Long studentId, Term term);
    List<Note> findBySubjectId(Long subjectId);
    List<Note> findByStudentIdAndSubjectId(Long studentId, Long subjectId);

    List<Note> findByTeacherId(Long teacherId);
    List<Note> findByClasseId(Long classeId);
    List<Note> findByClasseIdAndTerm(Long classeId, Term term);
}