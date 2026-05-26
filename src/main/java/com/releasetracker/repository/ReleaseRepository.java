package com.releasetracker.repository;

import com.releasetracker.entity.Release;
import com.releasetracker.enums.DeploymentEnvironment;
import com.releasetracker.enums.DeploymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Release entity.
 * Extends JpaRepository to provide standard CRUD operations.
 */
@Repository
public interface ReleaseRepository extends JpaRepository<Release, Long> {

    /**
     * Find all releases for a specific project.
     */
    List<Release> findByProjectNameIgnoreCase(String projectName);

    /**
     * Find all releases with a given deployment status.
     */
    List<Release> findByStatus(DeploymentStatus status);

    /**
     * Find all releases for a specific environment.
     */
    List<Release> findByEnvironment(DeploymentEnvironment environment);

    /**
     * Find releases by both environment and status.
     */
    List<Release> findByEnvironmentAndStatus(DeploymentEnvironment environment, DeploymentStatus status);

    /**
     * Check if a specific version already exists for a project.
     */
    boolean existsByProjectNameIgnoreCaseAndVersion(String projectName, String version);
}
