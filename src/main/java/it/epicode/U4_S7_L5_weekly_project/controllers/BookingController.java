package it.epicode.U4_S7_L5_weekly_project.controllers;

import it.epicode.U4_S7_L5_weekly_project.entities.Booking;
import it.epicode.U4_S7_L5_weekly_project.entities.Event;
import it.epicode.U4_S7_L5_weekly_project.exceptions.BadRequestException;
import it.epicode.U4_S7_L5_weekly_project.exceptions.NoContentException;
import it.epicode.U4_S7_L5_weekly_project.exceptions.NotFoundException;
import it.epicode.U4_S7_L5_weekly_project.payloads.BookingDTO;
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

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private EventService eventService;

    // GET all

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    public ResponseEntity<org.springframework.data.domain.Page<Booking>> getAllBookings(Pageable pageable) {
        org.springframework.data.domain.Page<Booking> bookings = bookingService.getAllBookings(pageable);
        if (bookings.isEmpty()) {
            throw new NoContentException("No bookings were found.");
        } else {
            ResponseEntity<Page<Booking>> responseEntity = new ResponseEntity<>(bookings, HttpStatus.OK);
            return responseEntity;
        }
    }

    // GET id

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    public ResponseEntity<Booking> getBookingById (@PathVariable long id) {
        Booking booking = bookingService.getBookingById(id);
        if (booking == null) {
            throw new NotFoundException(id);
        } else {
            ResponseEntity<Booking> responseEntity = new ResponseEntity<>(booking, HttpStatus.OK);
            return responseEntity;
        }
    }

    // POST

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Booking> saveBooking(@RequestBody @Validated BookingDTO bookingPayload, BindingResult validation){
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            Booking locationToBeSaved = Booking.builder()
                    .withEvent(bookingPayload.bookedEvent())
                    .withBookingDate(bookingPayload.bookingDate())
                    .withUser(bookingPayload.user())
                    .build();
            Booking savedBooking = bookingService.saveBooking(locationToBeSaved);
            ResponseEntity<Booking> responseEntity = new ResponseEntity<>(savedBooking, HttpStatus.CREATED);
            return responseEntity;
        }
    }

    // PUT

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    public ResponseEntity<Booking> updateBooking(@PathVariable long id, @RequestBody @Validated BookingDTO updatedBookingPayload, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            Booking bookingToBeUpdated = bookingService.getBookingById(id);
            if (bookingToBeUpdated == null) {
                throw new NotFoundException(id);
            }
            bookingToBeUpdated.setEvent(updatedBookingPayload.bookedEvent());
            bookingToBeUpdated.setBookingDate(updatedBookingPayload.bookingDate());
            bookingToBeUpdated.setUser(updatedBookingPayload.user());

            Booking updatedBooking = bookingService.updateBooking(id, updatedBookingPayload);
            ResponseEntity<Booking> responseEntity = new ResponseEntity<>(updatedBooking, HttpStatus.OK);
            return responseEntity;
        }
    }

    // DELETE

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_ROLE') or hasAuthority('BASIC_USER')")
    public ResponseEntity<Void> deleteBooking(@PathVariable long id) {
        bookingService.deleteBooking(id);
        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return responseEntity;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Event>> getBookedEventsByUserId(@PathVariable long id) {
        List<Event> bookedEvents = bookingService.getBookedEventsByUserId(id);
        ResponseEntity<List<Event>> responseEntity = new ResponseEntity<>(bookedEvents, HttpStatus.OK);
        return responseEntity;
    }



}
