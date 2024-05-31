package it.epicode.U4_S7_L5_weekly_project.payloads;

import it.epicode.U4_S7_L5_weekly_project.entities.Event;
import it.epicode.U4_S7_L5_weekly_project.entities.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record BookingDTO(
        @NotNull(message = "User name is mandatory")
        @NotEmpty(message = "User name cannot be empty")
        User user,
        @NotNull (message = "Booked event is mandatory")
        @NotEmpty(message = "Booked event cannot be empty")
        Event bookedEvent,
        LocalDateTime bookingDate
) {
}
