package it.epicode.U4_S7_L5_weekly_project.payloads;

import it.epicode.U4_S7_L5_weekly_project.entities.Event;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record LocationDTO(
        @NotNull (message = "Location name is mandatory")
        @NotEmpty(message = "Location name cannot be empty")
        String locationName,
        @NotNull (message = "Location city is mandatory")
        @NotEmpty(message = "Location city cannot be empty")
        String locationCity,
        List<Event> events
) {
}
