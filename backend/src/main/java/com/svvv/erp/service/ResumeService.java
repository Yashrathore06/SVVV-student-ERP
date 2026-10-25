package com.svvv.erp.service;

import com.svvv.erp.dto.ResumeDto;
import com.svvv.erp.entity.Resume;
import com.svvv.erp.entity.Student;
import com.svvv.erp.repository.PlacementApplicationRepository;
import com.svvv.erp.repository.ResumeRepository;
import com.svvv.erp.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ResumeService {

    private final Path uploadDir = Paths.get("uploads", "resumes");

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PlacementApplicationRepository placementApplicationRepository;

    public ResumeService() {
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            System.err.println("Could not create uploads directory: " + e.getMessage());
        }
    }

    public ResumeDto uploadResume(Long studentId, MultipartFile file) throws IOException {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) {
            throw new IllegalArgumentException("Student not found with ID: " + studentId);
        }

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload empty file.");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            originalFilename = "resume.pdf";
        }

        // Clean unique file name
        String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFilename.replaceAll("[^a-zA-Z0-9.-]", "_");
        Path destination = uploadDir.resolve(uniqueFileName);

        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        Resume resume = new Resume(studentOpt.get(), originalFilename, destination.toString());
        Resume savedResume = resumeRepository.save(resume);

        return toDto(savedResume);
    }

    public List<ResumeDto> getStudentResumes(Long studentId) {
        return resumeRepository.findByStudentId(studentId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Optional<ResumeDto> getLatestResume(Long studentId) {
        return resumeRepository.findTopByStudentIdOrderByUploadedAtDesc(studentId)
                .map(this::toDto);
    }

    public boolean deleteResume(Long resumeId) {
        Optional<Resume> resumeOpt = resumeRepository.findById(resumeId);
        if (resumeOpt.isEmpty()) {
            return false;
        }

        Resume resume = resumeOpt.get();

        // Delete physical file
        try {
            Path filePath = Paths.get(resume.getFilePath());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.err.println("Could not delete physical file: " + e.getMessage());
        }

        resumeRepository.delete(resume);
        return true;
    }

    public Resource loadResumeFileAsResource(Long resumeId) {
        Optional<Resume> resumeOpt = resumeRepository.findById(resumeId);
        if (resumeOpt.isEmpty()) {
            throw new RuntimeException("Resume not found with ID: " + resumeId);
        }

        try {
            Path filePath = Paths.get(resumeOpt.get().getFilePath());
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + filePath);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid file URL: " + e.getMessage());
        }
    }

    public ResumeDto toDto(Resume resume) {
        return new ResumeDto(
                resume.getId(),
                resume.getStudent().getId(),
                resume.getFileName(),
                resume.getFilePath(),
                resume.getUploadedAt()
        );
    }
}
