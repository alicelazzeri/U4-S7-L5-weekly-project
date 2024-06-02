package it.epicode.U4_S7_L5_weekly_project.runners;

import com.github.javafaker.Faker;
import it.epicode.U4_S7_L5_weekly_project.entities.Event;
import it.epicode.U4_S7_L5_weekly_project.entities.Location;
import it.epicode.U4_S7_L5_weekly_project.entities.User;
import it.epicode.U4_S7_L5_weekly_project.entities.enums.EventStatus;
import it.epicode.U4_S7_L5_weekly_project.entities.enums.EventType;
import it.epicode.U4_S7_L5_weekly_project.repositories.EventRepository;
import it.epicode.U4_S7_L5_weekly_project.repositories.LocationRepository;
import it.epicode.U4_S7_L5_weekly_project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Component
public class EventsRunner implements CommandLineRunner {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();

        Iterable<Location> locations = locationRepository.findAll();
        Iterable<User> users = userRepository.findAll();

        for (int i = 0; i < 30; i++) {
            Location randomLocation = getRandomItem(locations, random);
            User randomUser = getRandomItem(users, random);

            Event event = Event.builder()
                    .withEventName(faker.lorem().word())
                    .withEventDescription(faker.lorem().sentence())
                    .withEventDate(LocalDateTime.now().plusDays(random.nextInt(30)))
                    .withEventType(EventType.values()[random.nextInt(EventType.values().length)])
                    .withEventStatus(EventStatus.values()[random.nextInt(EventStatus.values().length)])
                    .withEventAvailableSeats(random.nextInt(100))
                    .withUser(randomUser)
                    .withLocation(randomLocation)
                    .build();

            eventRepository.save(event);
        }
    }

    private <T> T getRandomItem(Iterable<T> iterable, Random random) {
        int size = 0;
        for (T ignored : iterable) {
            size++;
        }
        if (size <= 0) {
            return null;
        }
        int randomIndex = random.nextInt(size);
        int currentIndex = 0;
        for (T item : iterable) {
            if (currentIndex == randomIndex) {
                return item;
            }
            currentIndex++;
        }
        return null;
    }

}
