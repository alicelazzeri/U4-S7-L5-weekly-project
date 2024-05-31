package it.epicode.U4_S7_L5_weekly_project.services;

import it.epicode.U4_S7_L5_weekly_project.entities.User;
import it.epicode.U4_S7_L5_weekly_project.exceptions.UnauthorizedException;
import it.epicode.U4_S7_L5_weekly_project.payloads.UserLoginRequestDTO;
import it.epicode.U4_S7_L5_weekly_project.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    UserService userService;

    @Autowired
    JWTTools jwtTools;

    @Autowired
    PasswordEncoder bcrypt;

    public String authenticateUserAndGenerateToken(UserLoginRequestDTO loginPayload) {
        User user = userService.findByEmail(loginPayload.email());
        if (bcrypt.matches(loginPayload.password(), user.getPassword())) {
            return jwtTools.createToken(user);
        } else {
            throw new UnauthorizedException("Invalid credentials! Try login again.");
        }

    }
}
