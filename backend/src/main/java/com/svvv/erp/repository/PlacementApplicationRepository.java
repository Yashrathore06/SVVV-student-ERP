package com.svvv.erp.repository;

import com.svvv.erp.entity.PlacementApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlacementApplicationRepository extends JpaRepository<PlacementApplication, Long> {
    List<PlacementApplication> findByStudentId(Long studentId);
    List<PlacementApplication> findByCompanyId(Long companyId);
    boolean existsByStudentIdAndCompanyId(Long studentId, Long companyId);
    java.util.Optional<PlacementApplication> findByStudentIdAndCompanyId(Long studentId, Long companyId);
    long countByStudentId(Long studentId);
}
