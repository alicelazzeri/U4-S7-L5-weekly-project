package it.epicode.U4_S7_L5_weekly_project.services;

import it.epicode.U4_S7_L5_weekly_project.entities.Event;
import it.epicode.U4_S7_L5_weekly_project.entities.Location;
import it.epicode.U4_S7_L5_weekly_project.exceptions.NotFoundException;
import it.epicode.U4_S7_L5_weekly_project.payloads.EventDTO;
import it.epicode.U4_S7_L5_weekly_project.repositories.EventRepository;
import it.epicode.U4_S7_L5_weekly_project.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private LocationRepository locationRepository;

    // GET all
    @Transactional(readOnly = true)
    public Page<Event> getAllEvents(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    // GET id
    @Transactional(readOnly = true)
    public Event getEventById(long id) {
        return eventRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    // POST
    @Transactional
    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    // PUT
    @Transactional
    public Event updateEvent(long id, EventDTO updatedEvent) {
        Event eventToBeUpdated = this.getEventById(id);
        eventToBeUpdated.setEventName(updatedEvent.eventName());
        eventToBeUpdated.setEventDescription(updatedEvent.eventDescription());
        eventToBeUpdated.setEventDate(updatedEvent.eventDate());
        eventToBeUpdated.setEventType(updatedEvent.eventType());
        eventToBeUpdated.setEventAvailableSeats(updatedEvent.eventAvailableSeats());
        return eventRepository.save(eventToBeUpdated);
    }

    // DELETE
    @Transactional
    public void deleteEvent(long id) {
        eventRepository.deleteById(id);
    }

    @Transactional
    public Event bookSeat(long id) {
        Event event = this.getEventById(id);
        if (event.getEventAvailableSeats() > 0) {
            event.setEventAvailableSeats(event.getEventAvailableSeats() - 1);
            return eventRepository.save(event);
        } else {
            throw new IllegalStateException("No available seats for this event.");
        }
    }
}
