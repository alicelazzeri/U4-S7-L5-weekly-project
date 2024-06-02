package it.epicode.U4_S7_L5_weekly_project.services;

import it.epicode.U4_S7_L5_weekly_project.entities.Booking;
import it.epicode.U4_S7_L5_weekly_project.entities.Event;
import it.epicode.U4_S7_L5_weekly_project.exceptions.NotFoundException;
import it.epicode.U4_S7_L5_weekly_project.payloads.BookingDTO;
import it.epicode.U4_S7_L5_weekly_project.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EventService eventService;

    // GET all
    @Transactional(readOnly = true)
    public Page<Booking> getAllBookings(Pageable pageable) {
        return bookingRepository.findAll(pageable);
    }

    // GET id
    @Transactional(readOnly = true)
    public Booking getBookingById(long id) {
        return bookingRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    @Transactional
    public Booking saveBooking(Booking booking) {
        Event event = booking.getEvent();
        if (event == null || event.getLocation() == null) {
            throw new IllegalArgumentException("Booking must have a valid event with a location.");
        }

        if (event.getId() != null) {
            Event existingEvent = eventService.getEventById(event.getId());
            if (existingEvent == null) {
                throw new NotFoundException("Event with ID " + event.getId() + " not found.");
            }
            booking.setEvent(existingEvent);
        } else {
            event = eventService.saveEvent(event);
            booking.setEvent(event);
        }

        return bookingRepository.save(booking);
    }



    // PUT
    @Transactional
    public Booking updateBooking(long id, BookingDTO updatedBooking) {
        Booking bookingToBeUpdated = this.getBookingById(id);
        bookingToBeUpdated.setUser(updatedBooking.user());
        bookingToBeUpdated.setEvent(updatedBooking.bookedEvent());
        bookingToBeUpdated.setBookingDate(updatedBooking.bookingDate());
        return bookingRepository.save(bookingToBeUpdated);
    }

    // DELETE
    @Transactional
    public void deleteBooking(long id) {
        bookingRepository.deleteById(id);
    }

    @Transactional
    public List<Event> getBookedEventsByUserId(long id) {
        return bookingRepository.findBookedEventsByUserId(id);

    }
}
