package it.epicode.U4_S7_L5_weekly_project.payloads;

import it.epicode.U4_S7_L5_weekly_project.entities.Booking;
import it.epicode.U4_S7_L5_weekly_project.entities.Location;
import it.epicode.U4_S7_L5_weekly_project.entities.User;
import it.epicode.U4_S7_L5_weekly_project.entities.enums.EventStatus;
import it.epicode.U4_S7_L5_weekly_project.entities.enums.EventType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record EventDTO(
        @NotNull(message = "Event name is mandatory")
        @NotEmpty(message = "Event name cannot be empty")
        String eventName,
        @NotNull (message = "Event description is mandatory")
        @NotEmpty(message = "Event description cannot be empty")
        String eventDescription,
        @NotNull (message = "Event date is mandatory")
        @NotEmpty(message = "Event date cannot be empty")
        LocalDateTime eventDate,
        EventType eventType,
        EventStatus eventStatus,
        int eventAvailableSeats,
        User eventOrganizer,
        List<Booking> bookings,
        Location location
) {
}
