package it.epicode.U4_S7_L5_weekly_project.payloads;

import it.epicode.U4_S7_L5_weekly_project.entities.Event;
import it.epicode.U4_S7_L5_weekly_project.entities.User;
import it.epicode.U4_S7_L5_weekly_project.entities.enums.EventStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AttendanceDTO(
        EventStatus eventStatus,
        @NotNull(message = "User name is mandatory")
        @NotEmpty(message = "User name cannot be empty")
        User user,
        @NotNull (message = "Event is mandatory")
        @NotEmpty(message = "Event cannot be empty")
        Event event
) {
}
