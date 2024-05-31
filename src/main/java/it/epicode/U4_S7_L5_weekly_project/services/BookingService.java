package it.epicode.U4_S7_L5_weekly_project.services;

import it.epicode.U4_S7_L5_weekly_project.entities.Booking;
import it.epicode.U4_S7_L5_weekly_project.exceptions.NotFoundException;
import it.epicode.U4_S7_L5_weekly_project.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

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

    // POST
    @Transactional
    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    // PUT
    @Transactional
    public Booking updateBooking(long id, Booking updatedBooking) {
        Booking bookingToBeUpdated = this.getBookingById(id);
        bookingToBeUpdated.setUser(updatedBooking.getUser());
        bookingToBeUpdated.setBookedEvent(updatedBooking.getBookedEvent());
        bookingToBeUpdated.setBookingDate(updatedBooking.getBookingDate());
        return bookingRepository.save(bookingToBeUpdated);
    }

    // DELETE
    @Transactional
    public void deleteBooking(long id) {
        bookingRepository.deleteById(id);
    }
}
