package it.epicode.U4_S7_L5_weekly_project.services;

import it.epicode.U4_S7_L5_weekly_project.entities.Attendance;
import it.epicode.U4_S7_L5_weekly_project.exceptions.NotFoundException;
import it.epicode.U4_S7_L5_weekly_project.payloads.AttendanceDTO;
import it.epicode.U4_S7_L5_weekly_project.repositories.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    // GET all
    @Transactional(readOnly = true)
    public Page<Attendance> getAllAttendances(Pageable pageable) {
        return attendanceRepository.findAll(pageable);
    }

    // GET id
    @Transactional(readOnly = true)
    public Attendance getAttendanceById(long id) {
        return attendanceRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    // POST
    @Transactional
    public Attendance saveAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    // PUT
    @Transactional
    public Attendance updateAttendance(long id, AttendanceDTO updatedAttendance) {
        Attendance attendanceToBeUpdated = this.getAttendanceById(id);
        attendanceToBeUpdated.setEventStatus(updatedAttendance.eventStatus());
        return attendanceRepository.save(attendanceToBeUpdated);
    }

    // DELETE
    @Transactional
    public void deleteAttendance(long id) {
        attendanceRepository.deleteById(id);
    }
}
