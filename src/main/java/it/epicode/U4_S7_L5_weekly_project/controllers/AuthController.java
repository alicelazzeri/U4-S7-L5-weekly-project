package it.epicode.U4_S7_L5_weekly_project.controllers;

import it.epicode.U4_S7_L5_weekly_project.exceptions.BadRequestException;
import it.epicode.U4_S7_L5_weekly_project.payloads.UserLoginRequestDTO;
import it.epicode.U4_S7_L5_weekly_project.payloads.UserLoginResponseDTO;
import it.epicode.U4_S7_L5_weekly_project.payloads.UserRegisterRequestDTO;
import it.epicode.U4_S7_L5_weekly_project.payloads.UserRegisterResponseDTO;
import it.epicode.U4_S7_L5_weekly_project.services.AuthService;
import it.epicode.U4_S7_L5_weekly_project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    // POST login
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> login(@RequestBody UserLoginRequestDTO loginPayload) {
        String token = authService.authenticateUserAndGenerateToken(loginPayload);
        UserLoginResponseDTO response = new UserLoginResponseDTO(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // POST register

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseDTO> register(@RequestBody @Validated UserRegisterRequestDTO registerPayload, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }

        UserRegisterResponseDTO response = userService.saveUser(registerPayload);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
