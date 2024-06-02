package it.epicode.U4_S7_L5_weekly_project.runners;

import com.github.javafaker.Faker;
import it.epicode.U4_S7_L5_weekly_project.entities.Booking;
import it.epicode.U4_S7_L5_weekly_project.entities.Event;
import it.epicode.U4_S7_L5_weekly_project.entities.User;
import it.epicode.U4_S7_L5_weekly_project.repositories.BookingRepository;
import it.epicode.U4_S7_L5_weekly_project.repositories.EventRepository;
import it.epicode.U4_S7_L5_weekly_project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Component
public class BookingsRunner implements CommandLineRunner {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();

        Iterable<Event> events = eventRepository.findAll();
        Iterable<User> users = userRepository.findAll();

        for (int i = 0; i < 30; i++) {
            Event randomEvent = getRandomItem(events, random);
            User randomUser = getRandomItem(users, random);

            Booking booking = Booking.builder()
                    .withUser(randomUser)
                    .withEvent(randomEvent)
                    .withBookingDate(LocalDateTime.now().minusDays(random.nextInt(30)))
                    .build();

            bookingRepository.save(booking);
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
