package it.epicode.U4_S7_L5_weekly_project.entities;

import it.epicode.U4_S7_L5_weekly_project.entities.enums.EventStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "attendances")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")

public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attendances_seq")
    @SequenceGenerator(name = "attendances_seq", sequenceName = "attendances_seq")
    private long id;

    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;

    @ManyToOne
    @JoinColumn(name = "user_id" )
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

}
