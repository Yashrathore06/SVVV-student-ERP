package com.svvv.erp.repository;

import com.svvv.erp.entity.HallTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HallTicketRepository extends JpaRepository<HallTicket, Long> {
    Optional<HallTicket> findByStudentIdAndSemester(Long studentId, Integer semester);
    Optional<HallTicket> findFirstByStudentIdOrderBySemesterDesc(Long studentId);
}
