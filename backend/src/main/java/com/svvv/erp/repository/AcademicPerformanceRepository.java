package com.svvv.erp.repository;

import com.svvv.erp.entity.AcademicPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AcademicPerformanceRepository extends JpaRepository<AcademicPerformance, Long> {
    Optional<AcademicPerformance> findByStudentId(Long studentId);
}
