package it.epicode.U4_S7_L5_weekly_project.repositories;

import it.epicode.U4_S7_L5_weekly_project.entities.Event;
import it.epicode.U4_S7_L5_weekly_project.entities.enums.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, PagingAndSortingRepository<Event, Long> {
}
