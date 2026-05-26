package com.releasetracker.controller;

import com.releasetracker.dto.ReleaseRequestDTO;
import com.releasetracker.dto.ReleaseResponseDTO;
import com.releasetracker.dto.StatusUpdateDTO;
import com.releasetracker.enums.DeploymentEnvironment;
import com.releasetracker.enums.DeploymentStatus;
import com.releasetracker.service.ReleaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller exposing all Release management endpoints.
 *
 * Base URL: /api/releases
 */
@RestController
@RequestMapping("/api/releases")
@RequiredArgsConstructor
@Slf4j
public class ReleaseController {

    private final ReleaseService releaseService;

    // ----------------------------------------------------------------
    // POST /api/releases — Create a new release
    // ----------------------------------------------------------------
    @PostMapping
    public ResponseEntity<ReleaseResponseDTO> createRelease(
            @Valid @RequestBody ReleaseRequestDTO requestDTO) {
        log.info("POST /api/releases - Create release");
        ReleaseResponseDTO response = releaseService.createRelease(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ----------------------------------------------------------------
    // GET /api/releases — Retrieve all releases (with optional filters)
    // ----------------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<ReleaseResponseDTO>> getAllReleases(
            @RequestParam(required = false) String project,
            @RequestParam(required = false) DeploymentStatus status,
            @RequestParam(required = false) DeploymentEnvironment environment) {

        if (project != null && !project.isBlank()) {
            log.info("GET /api/releases?project={}", project);
            return ResponseEntity.ok(releaseService.getReleasesByProject(project));
        }

        if (status != null) {
            log.info("GET /api/releases?status={}", status);
            return ResponseEntity.ok(releaseService.getReleasesByStatus(status));
        }

        if (environment != null) {
            log.info("GET /api/releases?environment={}", environment);
            return ResponseEntity.ok(releaseService.getReleasesByEnvironment(environment));
        }

        log.info("GET /api/releases - Fetch all");
        return ResponseEntity.ok(releaseService.getAllReleases());
    }

    // ----------------------------------------------------------------
    // GET /api/releases/{id} — Retrieve a release by ID
    // ----------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<ReleaseResponseDTO> getReleaseById(@PathVariable Long id) {
        log.info("GET /api/releases/{}", id);
        return ResponseEntity.ok(releaseService.getReleaseById(id));
    }

    // ----------------------------------------------------------------
    // PUT /api/releases/{id} — Full update
    // ----------------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<ReleaseResponseDTO> updateRelease(
            @PathVariable Long id,
            @Valid @RequestBody ReleaseRequestDTO requestDTO) {
        log.info("PUT /api/releases/{}", id);
        return ResponseEntity.ok(releaseService.updateRelease(id, requestDTO));
    }

    // ----------------------------------------------------------------
    // PATCH /api/releases/{id}/status — Update status only
    // ----------------------------------------------------------------
    @PatchMapping("/{id}/status")
    public ResponseEntity<ReleaseResponseDTO> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusUpdateDTO statusUpdateDTO) {
        log.info("PATCH /api/releases/{}/status", id);
        return ResponseEntity.ok(releaseService.updateStatus(id, statusUpdateDTO));
    }

    // ----------------------------------------------------------------
    // DELETE /api/releases/{id} — Delete a release
    // ----------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRelease(@PathVariable Long id) {
        log.info("DELETE /api/releases/{}", id);
        releaseService.deleteRelease(id);
        return ResponseEntity.noContent().build();
    }
}
