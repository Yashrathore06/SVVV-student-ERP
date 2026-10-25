package com.svvv.erp.repository;

import com.svvv.erp.entity.ElectiveCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ElectiveCourseRepository extends JpaRepository<ElectiveCourse, Long> {
    List<ElectiveCourse> findByStatus(String status);
    Optional<ElectiveCourse> findByCourseCode(String courseCode);
    boolean existsByCourseCode(String courseCode);
}
