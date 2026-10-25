package com.svvv.erp.repository;

import com.svvv.erp.entity.BonafideRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BonafideRequestRepository extends JpaRepository<BonafideRequest, Long> {
    List<BonafideRequest> findByStudentIdOrderByRequestedAtDesc(Long studentId);
    List<BonafideRequest> findByStatus(String status);
    long countByStatus(String status);
}
