package it.epicode.U4_S7_L5_weekly_project.runners;

import com.github.javafaker.Faker;
import it.epicode.U4_S7_L5_weekly_project.entities.Location;
import it.epicode.U4_S7_L5_weekly_project.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class LocationsRunner implements CommandLineRunner {

    @Autowired
    private LocationService locationService;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        for (int i = 0; i < 30; i++) {
            Location location = Location.builder()
                    .withLocationName(faker.address().buildingNumber())
                    .withLocationCity(faker.address().city())
                    .build();

            locationService.saveLocation(location);
        }

    }
}
