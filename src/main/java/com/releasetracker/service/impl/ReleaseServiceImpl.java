package com.releasetracker.service.impl;

import com.releasetracker.dto.ReleaseRequestDTO;
import com.releasetracker.dto.ReleaseResponseDTO;
import com.releasetracker.dto.StatusUpdateDTO;
import com.releasetracker.entity.Release;
import com.releasetracker.enums.DeploymentEnvironment;
import com.releasetracker.enums.DeploymentStatus;
import com.releasetracker.exception.ReleaseNotFoundException;
import com.releasetracker.repository.ReleaseRepository;
import com.releasetracker.service.ReleaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReleaseServiceImpl implements ReleaseService {

    private final ReleaseRepository releaseRepository;

    @Override
    @Transactional
    public ReleaseResponseDTO createRelease(ReleaseRequestDTO requestDTO) {
        log.info("Creating release for project '{}' version '{}'",
                requestDTO.getProjectName(), requestDTO.getVersion());
        Release release = mapToEntity(requestDTO);
        Release saved = releaseRepository.save(release);
        log.info("Release created successfully with ID: {}", saved.getId());
        return mapToResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReleaseResponseDTO> getAllReleases() {
        log.info("Fetching all releases");
        return releaseRepository.findAll()
                .stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ReleaseResponseDTO getReleaseById(Long id) {
        log.info("Fetching release with ID: {}", id);
        return mapToResponseDTO(findByIdOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReleaseResponseDTO> getReleasesByProject(String projectName) {
        log.info("Fetching releases for project: {}", projectName);
        return releaseRepository.findByProjectNameIgnoreCase(projectName)
                .stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReleaseResponseDTO> getReleasesByStatus(DeploymentStatus status) {
        log.info("Fetching releases with status: {}", status);
        return releaseRepository.findByStatus(status)
                .stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReleaseResponseDTO> getReleasesByEnvironment(DeploymentEnvironment environment) {
        log.info("Fetching releases for environment: {}", environment);
        return releaseRepository.findByEnvironment(environment)
                .stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReleaseResponseDTO updateRelease(Long id, ReleaseRequestDTO requestDTO) {
        log.info("Updating release with ID: {}", id);
        Release existing = findByIdOrThrow(id);
        existing.setProjectName(requestDTO.getProjectName());
        existing.setVersion(requestDTO.getVersion());
        existing.setEnvironment(requestDTO.getEnvironment());
        existing.setReleaseDate(requestDTO.getReleaseDate());
        existing.setStatus(requestDTO.getStatus());
        existing.setDescription(requestDTO.getDescription());
        Release updated = releaseRepository.save(existing);
        log.info("Release with ID {} updated successfully", id);
        return mapToResponseDTO(updated);
    }

    @Override
    @Transactional
    public ReleaseResponseDTO updateStatus(Long id, StatusUpdateDTO statusUpdateDTO) {
        log.info("Updating status of release ID {} to {}", id, statusUpdateDTO.getStatus());
        Release existing = findByIdOrThrow(id);
        existing.setStatus(statusUpdateDTO.getStatus());
        Release updated = releaseRepository.save(existing);
        log.info("Status of release ID {} updated to {}", id, updated.getStatus());
        return mapToResponseDTO(updated);
    }

    @Override
    @Transactional
    public void deleteRelease(Long id) {
        log.info("Deleting release with ID: {}", id);
        Release release = findByIdOrThrow(id);
        releaseRepository.delete(release);
        log.info("Release with ID {} deleted successfully", id);
    }

    private Release findByIdOrThrow(Long id) {
        return releaseRepository.findById(id)
                .orElseThrow(() -> new ReleaseNotFoundException(id));
    }

    private Release mapToEntity(ReleaseRequestDTO dto) {
        return Release.builder()
                .projectName(dto.getProjectName())
                .version(dto.getVersion())
                .environment(dto.getEnvironment())
                .releaseDate(dto.getReleaseDate())
                .status(dto.getStatus())
                .description(dto.getDescription())
                .build();
    }

    private ReleaseResponseDTO mapToResponseDTO(Release release) {
        return ReleaseResponseDTO.builder()
                .id(release.getId())
                .projectName(release.getProjectName())
                .version(release.getVersion())
                .environment(release.getEnvironment())
                .releaseDate(release.getReleaseDate())
                .status(release.getStatus())
                .description(release.getDescription())
                .createdAt(release.getCreatedAt())
                .updatedAt(release.getUpdatedAt())
                .build();
    }
}
