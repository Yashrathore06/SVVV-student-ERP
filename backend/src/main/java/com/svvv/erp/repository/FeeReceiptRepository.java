package com.svvv.erp.repository;

import com.svvv.erp.entity.FeeReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeeReceiptRepository extends JpaRepository<FeeReceipt, Long> {
    List<FeeReceipt> findByStudentIdOrderByPaymentDateDesc(Long studentId);
    Optional<FeeReceipt> findByTransactionId(String transactionId);
}
