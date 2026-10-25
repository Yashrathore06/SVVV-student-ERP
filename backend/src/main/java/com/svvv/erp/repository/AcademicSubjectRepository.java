package com.svvv.erp.repository;

import com.svvv.erp.entity.AcademicSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcademicSubjectRepository extends JpaRepository<AcademicSubject, Long> {

    List<AcademicSubject> findByBranchAndSemester(String branch, Integer semester);

    List<AcademicSubject> findBySemester(Integer semester);
}
