package it.epicode.U4_S7_L5_weekly_project.controllers;

import it.epicode.U4_S7_L5_weekly_project.entities.Event;
import it.epicode.U4_S7_L5_weekly_project.exceptions.BadRequestException;
import it.epicode.U4_S7_L5_weekly_project.exceptions.NoContentException;
import it.epicode.U4_S7_L5_weekly_project.exceptions.NotFoundException;
import it.epicode.U4_S7_L5_weekly_project.payloads.EventDTO;
import it.epicode.U4_S7_L5_weekly_project.services.BookingService;
import it.epicode.U4_S7_L5_weekly_project.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private BookingService bookingService;

    // GET all

    @GetMapping
    public ResponseEntity<Page<Event>> getAllEvents(Pageable pageable) {
        Page<Event> events = eventService.getAllEvents(pageable);
        if (events.isEmpty()) {
            throw new NoContentException("No events were found.");
        } else {
            ResponseEntity<Page<Event>> responseEntity = new ResponseEntity<>(events, HttpStatus.OK);
            return responseEntity;
        }
    }

    // GET id

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable long id) {
        Event event = eventService.getEventById(id);
        if (event == null) {
            throw new NotFoundException(id);
        } else {
            ResponseEntity<Event> responseEntity = new ResponseEntity<>(event, HttpStatus.OK);
            return responseEntity;
        }
    }

    // POST

    @PostMapping
    @PreAuthorize("hasRole('EVENT_ORGANIZER_USER')")
    public ResponseEntity<Event> saveEvent(@RequestBody @Validated EventDTO eventPayload, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            Event eventToBeSaved = Event.builder()
                    .withEventName(eventPayload.eventName())
                    .withEventDescription(eventPayload.eventDescription())
                    .withEventDate(eventPayload.eventDate())
                    .withEventStatus(eventPayload.eventStatus())
                    .withEventType(eventPayload.eventType())
                    .withUser(eventPayload.eventOrganizer())
                    .withEventAvailableSeats(eventPayload.eventAvailableSeats())
                    .withBookings(eventPayload.bookings())
                    .withLocation(eventPayload.location())
                    .build();
            Event savedEvent = eventService.saveEvent(eventToBeSaved);
            ResponseEntity<Event> responseEntity = new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
            return responseEntity;
        }
    }

    // PUT

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EVENT_ORGANIZER_USER')")
    public ResponseEntity<Event> updateEvent(@PathVariable long id, @RequestBody @Validated EventDTO updatedEventPayload, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            Event eventToBeUpdated = eventService.getEventById(id);
            if (eventToBeUpdated == null) {
                throw new NotFoundException(id);
            }
            eventToBeUpdated.setEventName(updatedEventPayload.eventName());
            eventToBeUpdated.setEventDescription(updatedEventPayload.eventDescription());
            eventToBeUpdated.setEventType(updatedEventPayload.eventType());
            eventToBeUpdated.setEventStatus(updatedEventPayload.eventStatus());
            eventToBeUpdated.setEventAvailableSeats(updatedEventPayload.eventAvailableSeats());
            eventToBeUpdated.setUser(updatedEventPayload.eventOrganizer());
            eventToBeUpdated.setBookings(updatedEventPayload.bookings());
            eventToBeUpdated.setLocation(updatedEventPayload.location());

            Event updatedEvent = eventService.saveEvent(eventToBeUpdated);
            ResponseEntity<Event> responseEntity = new ResponseEntity<>(updatedEvent, HttpStatus.OK);
            return responseEntity;
        }
    }

    // DELETE

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EVENT_ORGANIZER_USER')")
    public ResponseEntity<Void> deleteEvent(@PathVariable long id) {
        eventService.deleteEvent(id);
        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return responseEntity;
    }

    // POST bookSeat

    @PostMapping("/{id}/book")
    @PreAuthorize("hasRole('BASIC_USER') or hasRole('EVENT_ORGANIZER_USER')")
    public ResponseEntity<Event> bookSeat(@PathVariable long id) {
        Event event = eventService.bookSeat(id);
        ResponseEntity<Event> responseEntity = new ResponseEntity<>(event, HttpStatus.OK);
        return responseEntity;
    }


}
