package com.svvv.erp.repository;

import com.svvv.erp.entity.HostelApplication;
import com.svvv.erp.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HostelApplicationRepository extends JpaRepository<HostelApplication, Long> {

    List<HostelApplication> findByStudentOrderByApplicationDateDesc(Student student);

    Optional<HostelApplication> findTopByStudentOrderByApplicationDateDesc(Student student);

    List<HostelApplication> findByStatusOrderByApplicationDateDesc(String status);

    List<HostelApplication> findAllByOrderByApplicationDateDesc();
}
