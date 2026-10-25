package com.svvv.erp.repository;

import com.svvv.erp.entity.SemesterResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SemesterResultRepository extends JpaRepository<SemesterResult, Long> {
    List<SemesterResult> findByStudentIdOrderBySemesterNumberDesc(Long studentId);
}
