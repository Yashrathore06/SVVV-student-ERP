package com.svvv.erp.repository;

import com.svvv.erp.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findByStatus(String status);
    long countByStatus(String status);

    @Query(value = "SELECT * FROM companies c WHERE c.status = 'ACTIVE' AND (" +
            "UPPER(c.allowed_branch) LIKE '%ALL%' " +
            "OR UPPER(c.allowed_branch) = UPPER(:exactBranch) " +
            "OR UPPER(c.allowed_branch) LIKE CONCAT('%', UPPER(:exactBranch), '%') " +
            "OR UPPER(c.allowed_branch) LIKE CONCAT('%', UPPER(:key1), '%') " +
            "OR UPPER(c.allowed_branch) LIKE CONCAT('%', UPPER(:key2), '%'))",
            nativeQuery = true)
    List<Company> findActiveCompaniesMatchingBranch(
            @Param("exactBranch") String exactBranch,
            @Param("key1") String key1,
            @Param("key2") String key2);
}
