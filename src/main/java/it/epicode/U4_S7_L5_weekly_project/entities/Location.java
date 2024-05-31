package it.epicode.U4_S7_L5_weekly_project.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "locations")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")

public class Location {

    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "locations_seq")
    @SequenceGenerator(name ="locations_seq", sequenceName = "locations_seq")
    private long id;

    @Column
    private String locationName;
    @Column
    private String locationCity;

    @OneToMany(mappedBy = "location")
    private List<Event> events = new ArrayList<>();
}
