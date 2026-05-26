package com.releasetracker.dto;

import com.releasetracker.enums.DeploymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusUpdateDTO {

    @NotNull(message = "Status must not be null")
    private DeploymentStatus status;
}
