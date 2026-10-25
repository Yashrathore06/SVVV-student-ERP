package com.svvv.erp.repository;

import com.svvv.erp.entity.FeeStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeeStructureRepository extends JpaRepository<FeeStructure, Long> {
    Optional<FeeStructure> findByBranchAndSemester(String branch, Integer semester);
    Optional<FeeStructure> findFirstBySemester(Integer semester);
}
