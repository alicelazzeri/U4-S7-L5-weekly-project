package it.epicode.U4_S7_L5_weekly_project.controllers;

import it.epicode.U4_S7_L5_weekly_project.entities.Attendance;
import it.epicode.U4_S7_L5_weekly_project.exceptions.BadRequestException;
import it.epicode.U4_S7_L5_weekly_project.exceptions.NoContentException;
import it.epicode.U4_S7_L5_weekly_project.exceptions.NotFoundException;
import it.epicode.U4_S7_L5_weekly_project.payloads.AttendanceDTO;
import it.epicode.U4_S7_L5_weekly_project.services.AttendanceService;
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
@RequestMapping("/api/attendances")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    // GET all

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    public ResponseEntity<Page<Attendance>> getAllAttendances(Pageable pageable) {
        Page<Attendance> attendances = attendanceService.getAllAttendances(pageable);
        if (attendances.isEmpty()) {
            throw new NoContentException("No attendances were found.");
        } else {
            ResponseEntity<Page<Attendance>> responseEntity = new ResponseEntity<>(attendances, HttpStatus.OK);
            return responseEntity;
        }
    }

    // GET id

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    public ResponseEntity<Attendance> getAttendanceById (@PathVariable long id) {
        Attendance attendance = attendanceService.getAttendanceById(id);
        if (attendance == null) {
            throw new NotFoundException(id);
        } else {
            ResponseEntity<Attendance> responseEntity = new ResponseEntity<>(attendance, HttpStatus.OK);
            return responseEntity;
        }
    }

    // POST

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Attendance> saveAttendance(@RequestBody @Validated AttendanceDTO attendancePayload, BindingResult validation){
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            Attendance attendanceToBeSaved = Attendance.builder()
                    .withEvent(attendancePayload.event())
                    .withEventStatus(attendancePayload.eventStatus())
                    .withUser(attendancePayload.user())
                    .build();
            Attendance savedAttendance = attendanceService.saveAttendance(attendanceToBeSaved);
            ResponseEntity<Attendance> responseEntity = new ResponseEntity<>(savedAttendance, HttpStatus.CREATED);
            return responseEntity;
        }
    }

    // PUT

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Attendance> updateAttendance(@PathVariable long id, @RequestBody @Validated AttendanceDTO updatedAttendancePayload, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            Attendance attendanceToBeUpdated = attendanceService.getAttendanceById(id);
            if (attendanceToBeUpdated == null) {
                throw new NotFoundException(id);
            }
            attendanceToBeUpdated.setEvent(updatedAttendancePayload.event());
            attendanceToBeUpdated.setEventStatus(updatedAttendancePayload.eventStatus());
            attendanceToBeUpdated.setUser(updatedAttendancePayload.user());

            Attendance updatedAttendance = attendanceService.updateAttendance(id, updatedAttendancePayload);
            ResponseEntity<Attendance> responseEntity = new ResponseEntity<>(updatedAttendance, HttpStatus.OK);
            return responseEntity;
        }
    }

}
