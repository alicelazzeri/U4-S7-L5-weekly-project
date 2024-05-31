package it.epicode.U4_S7_L5_weekly_project.controllers;

import it.epicode.U4_S7_L5_weekly_project.entities.Booking;
import it.epicode.U4_S7_L5_weekly_project.entities.Location;
import it.epicode.U4_S7_L5_weekly_project.exceptions.BadRequestException;
import it.epicode.U4_S7_L5_weekly_project.exceptions.NoContentException;
import it.epicode.U4_S7_L5_weekly_project.exceptions.NotFoundException;
import it.epicode.U4_S7_L5_weekly_project.payloads.BookingDTO;
import it.epicode.U4_S7_L5_weekly_project.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // GET all

    @GetMapping
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
    public ResponseEntity<Booking> saveBooking(@RequestBody @Validated BookingDTO bookingPayload, BindingResult validation){
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            Booking locationToBeSaved = Booking.builder()
                    .withBookedEvent(bookingPayload.bookedEvent())
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
    public ResponseEntity<Booking> updateBooking(@PathVariable long id, @RequestBody @Validated BookingDTO updatedBookingPayload, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            Booking bookingToBeUpdated = bookingService.getBookingById(id);
            if (bookingToBeUpdated == null) {
                throw new NotFoundException(id);
            }
            bookingToBeUpdated.setBookedEvent(updatedBookingPayload.bookedEvent());
            bookingToBeUpdated.setBookingDate(updatedBookingPayload.bookingDate());
            bookingToBeUpdated.setUser(updatedBookingPayload.user());

            Booking updatedBooking = bookingService.updateBooking(id, updatedBookingPayload);
            ResponseEntity<Booking> responseEntity = new ResponseEntity<>(updatedBooking, HttpStatus.OK);
            return responseEntity;
        }
    }

    // DELETE

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable long id) {
        bookingService.deleteBooking(id);
        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return responseEntity;
    }

}
