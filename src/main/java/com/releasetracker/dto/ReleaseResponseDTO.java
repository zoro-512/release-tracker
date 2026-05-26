package com.releasetracker.dto;

import com.releasetracker.enums.DeploymentEnvironment;
import com.releasetracker.enums.DeploymentStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReleaseResponseDTO {

    private Long id;
    private String projectName;
    private String version;
    private DeploymentEnvironment environment;
    private LocalDate releaseDate;
    private DeploymentStatus status;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
