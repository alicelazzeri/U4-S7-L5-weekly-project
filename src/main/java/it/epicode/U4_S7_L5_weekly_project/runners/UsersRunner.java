package it.epicode.U4_S7_L5_weekly_project.runners;

import com.github.javafaker.Faker;
import it.epicode.U4_S7_L5_weekly_project.entities.enums.Gender;
import it.epicode.U4_S7_L5_weekly_project.entities.enums.Role;
import it.epicode.U4_S7_L5_weekly_project.payloads.UserRegisterRequestDTO;
import it.epicode.U4_S7_L5_weekly_project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class UsersRunner implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    PasswordEncoder bcrypt;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();

        for (int i = 0; i < 30; i++) {
            Gender[] genders = Gender.values();
            Gender randomGender = genders[random.nextInt(genders.length)];

            Role[] roles = Role.values();
            Role randomRole = roles[random.nextInt(roles.length)];

            UserRegisterRequestDTO user = new UserRegisterRequestDTO(
                    faker.name().firstName(),
                    faker.name().lastName(),
                    randomGender,
                    faker.internet().emailAddress(),
                    bcrypt.encode(faker.internet().password()),
                    faker.internet().avatar(),
                    randomRole
            );
            userService.saveUser(user);
        }
    }
}
