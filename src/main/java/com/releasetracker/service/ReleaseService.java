package com.releasetracker.service;

import com.releasetracker.dto.ReleaseRequestDTO;
import com.releasetracker.dto.ReleaseResponseDTO;
import com.releasetracker.dto.StatusUpdateDTO;
import com.releasetracker.enums.DeploymentEnvironment;
import com.releasetracker.enums.DeploymentStatus;

import java.util.List;

/**
 * Service interface declaring all business operations for release management.
 */
public interface ReleaseService {

    /** Create a new release record. */
    ReleaseResponseDTO createRelease(ReleaseRequestDTO requestDTO);

    /** Retrieve all releases. */
    List<ReleaseResponseDTO> getAllReleases();

    /** Retrieve a single release by ID. */
    ReleaseResponseDTO getReleaseById(Long id);

    /** Fully update an existing release. */
    ReleaseResponseDTO updateRelease(Long id, ReleaseRequestDTO requestDTO);

    /** Partially update only the deployment status of a release. */
    ReleaseResponseDTO updateStatus(Long id, StatusUpdateDTO statusUpdateDTO);

    /** Delete a release by ID. */
    void deleteRelease(Long id);

    /** Filter releases by project name. */
    List<ReleaseResponseDTO> getReleasesByProject(String projectName);

    /** Filter releases by deployment status. */
    List<ReleaseResponseDTO> getReleasesByStatus(DeploymentStatus status);

    /** Filter releases by deployment environment. */
    List<ReleaseResponseDTO> getReleasesByEnvironment(DeploymentEnvironment environment);
}
