package com.releasetracker.dto;

import com.releasetracker.enums.DeploymentEnvironment;
import com.releasetracker.enums.DeploymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReleaseRequestDTO {

    @NotBlank(message = "Project name must not be blank")
    @Size(max = 100, message = "Project name must be at most 100 characters")
    private String projectName;

    @NotBlank(message = "Version must not be blank")
    @Size(max = 50, message = "Version must be at most 50 characters")
    private String version;

    @NotNull(message = "Environment must not be null")
    private DeploymentEnvironment environment;

    @NotNull(message = "Release date must not be null")
    private LocalDate releaseDate;

    @NotNull(message = "Status must not be null")
    private DeploymentStatus status;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;
}
