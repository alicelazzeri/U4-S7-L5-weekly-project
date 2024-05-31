package it.epicode.U4_S7_L5_weekly_project.repositories;

import it.epicode.U4_S7_L5_weekly_project.entities.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long>, PagingAndSortingRepository<Attendance, Long> {
}
