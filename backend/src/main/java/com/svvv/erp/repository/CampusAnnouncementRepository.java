package com.svvv.erp.repository;

import com.svvv.erp.entity.CampusAnnouncement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampusAnnouncementRepository extends JpaRepository<CampusAnnouncement, Long> {
    
    List<CampusAnnouncement> findAllByOrderByCreatedAtDesc();

    @Query("SELECT a FROM CampusAnnouncement a WHERE a.targetAudience = 'All Enrolled Students (Campus Wide)' OR a.targetAudience = 'ALL' OR LOWER(a.targetAudience) LIKE LOWER(CONCAT('%', :branch, '%')) ORDER BY a.createdAt DESC")
    List<CampusAnnouncement> findForBranchOrAll(@Param("branch") String branch);
}
