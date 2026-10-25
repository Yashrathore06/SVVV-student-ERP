package com.svvv.erp.repository;

import com.svvv.erp.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUserId(Long userId);
    Optional<Student> findByEnrollmentNo(String enrollmentNo);
    Optional<Student> findByUser_Username(String username);
    boolean existsByEnrollmentNo(String enrollmentNo);
}
