package com.svvv.erp.repository;

import com.svvv.erp.entity.ElectiveEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ElectiveEnrollmentRepository extends JpaRepository<ElectiveEnrollment, Long> {
    List<ElectiveEnrollment> findByStudentId(Long studentId);
    List<ElectiveEnrollment> findByCourseId(Long courseId);
    long countByCourseId(Long courseId);
    boolean existsByStudentId(Long studentId);
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
    Optional<ElectiveEnrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);
}
