package it.epicode.U4_S7_L5_weekly_project.repositories;

import it.epicode.U4_S7_L5_weekly_project.entities.Booking;
import it.epicode.U4_S7_L5_weekly_project.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, PagingAndSortingRepository<Booking, Long> {
    public List<Event> findBookedEventsByUserId(long id);
}
