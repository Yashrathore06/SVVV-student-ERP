package com.svvv.erp.controller;

import com.svvv.erp.entity.CampusAnnouncement;
import com.svvv.erp.entity.Notification;
import com.svvv.erp.entity.Student;
import com.svvv.erp.repository.CampusAnnouncementRepository;
import com.svvv.erp.repository.NotificationRepository;
import com.svvv.erp.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {

    @Autowired
    private CampusAnnouncementRepository announcementRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping
    public ResponseEntity<List<CampusAnnouncement>> getAnnouncements(
            @RequestParam(value = "branch", required = false) String branch) {
        if (branch != null && !branch.trim().isEmpty() && !branch.equalsIgnoreCase("ALL")) {
            return ResponseEntity.ok(announcementRepository.findForBranchOrAll(branch.trim()));
        }
        return ResponseEntity.ok(announcementRepository.findAllByOrderByCreatedAtDesc());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> publishAnnouncement(@RequestBody Map<String, String> payload) {
        String title = payload.getOrDefault("title", "").trim();
        String targetAudience = payload.getOrDefault("targetAudience", "All Enrolled Students (Campus Wide)").trim();
        String content = payload.getOrDefault("content", "").trim();

        if (title.isEmpty()) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", "Announcement title is required.");
            return ResponseEntity.badRequest().body(err);
        }
        if (content.isEmpty()) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", "Announcement content is required.");
            return ResponseEntity.badRequest().body(err);
        }

        // 1. Save to campus_announcements table
        CampusAnnouncement announcement = new CampusAnnouncement(title, targetAudience, content);
        CampusAnnouncement saved = announcementRepository.save(announcement);

        // 2. Broadcast push notifications to notifications table for enrolled students
        int notifyCount = 0;
        try {
            List<Student> allStudents = studentRepository.findAll();
            for (Student s : allStudents) {
                if (s.getUser() == null) continue;

                boolean shouldNotify = false;
                if (targetAudience.equalsIgnoreCase("ALL") || 
                    targetAudience.toLowerCase().contains("all enrolled")) {
                    shouldNotify = true;
                } else if (s.getBranch() != null && 
                           (targetAudience.toLowerCase().contains(s.getBranch().toLowerCase()) || 
                            s.getBranch().toLowerCase().contains(targetAudience.toLowerCase()))) {
                    shouldNotify = true;
                }

                if (shouldNotify) {
                    Notification notif = new Notification(
                            s.getUser(),
                            "📢 " + title,
                            content,
                            "ANNOUNCEMENT"
                    );
                    notificationRepository.save(notif);
                    notifyCount++;
                }
            }
        } catch (Exception ex) {
            System.out.println(">>> Note: Push notification broadcast error: " + ex.getMessage());
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("message", "Announcement published successfully!");
        resp.put("announcement", saved);
        resp.put("broadcastCount", notifyCount);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteAnnouncement(@PathVariable("id") Long id) {
        if (!announcementRepository.existsById(id)) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", "Announcement not found with id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
        }

        announcementRepository.deleteById(id);

        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("message", "Announcement removed successfully.");
        return ResponseEntity.ok(resp);
    }
}
