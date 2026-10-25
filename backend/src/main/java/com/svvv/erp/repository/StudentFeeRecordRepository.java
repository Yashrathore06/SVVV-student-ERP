package com.svvv.erp.repository;

import com.svvv.erp.entity.StudentFeeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentFeeRecordRepository extends JpaRepository<StudentFeeRecord, Long> {
    List<StudentFeeRecord> findByStudentIdOrderBySemesterDesc(Long studentId);
    Optional<StudentFeeRecord> findByStudentIdAndSemester(Long studentId, Integer semester);
    Optional<StudentFeeRecord> findFirstByStudentIdOrderBySemesterDesc(Long studentId);
}
