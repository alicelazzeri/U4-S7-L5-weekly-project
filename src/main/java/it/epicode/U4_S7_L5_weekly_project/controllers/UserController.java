package it.epicode.U4_S7_L5_weekly_project.controllers;

import it.epicode.U4_S7_L5_weekly_project.entities.User;
import it.epicode.U4_S7_L5_weekly_project.exceptions.NoContentException;
import it.epicode.U4_S7_L5_weekly_project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // GET all

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    public ResponseEntity<Page<User>> getAllUsers(Pageable pageable) {
        Page<User> users = userService.getAllUsers(pageable);
        if (users.isEmpty()) {
            throw new NoContentException("No users were found.");
        } else {
            ResponseEntity<Page<User>> responseEntity = new ResponseEntity<>(users, HttpStatus.OK);
            return responseEntity;
        }
    }

    // GET profile

    @GetMapping("/me")
    @PreAuthorize("hasAnyAuthority('BASIC_USER', 'EVENT_ORGANIZER_USER', 'ADMIN_ROLE')")
    public ResponseEntity<User> getProfile(@AuthenticationPrincipal User authenticatedUser) {
        return ResponseEntity.ok(authenticatedUser);
    }

    // PUT profile

    @PutMapping("/me")
    @PreAuthorize("hasAnyAuthority('BASIC_USER', 'EVENT_ORGANIZER_USER', 'ADMIN_ROLE')")
    public ResponseEntity<User> updateUser(@AuthenticationPrincipal User authenticatedUser, @RequestBody User updatedUser) {
        User profileToBeUpdated = userService.updateUser(authenticatedUser.getId(), updatedUser);
        return ResponseEntity.ok(profileToBeUpdated);
    }

    // DELETE profile

    @DeleteMapping("/me")
    @PreAuthorize("hasAnyAuthority('BASIC_USER', 'EVENT_ORGANIZER_USER', 'ADMIN_ROLE')")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal User authenticatedUser) {
        userService.deleteUser(authenticatedUser.getId());
        return ResponseEntity.noContent().build();
    }

    // GET id

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    public ResponseEntity<User> getUserById(@PathVariable long id){
        User user = userService.getUserById(id);
        ResponseEntity<User> responseEntity = new ResponseEntity<>(user, HttpStatus.OK);
        return responseEntity;
    }

    // PUT id

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    public ResponseEntity<User> updateUser(@PathVariable long id, @RequestBody User profilePayload) {
        User userToBeUpdated = userService.updateUser(id, profilePayload);
        ResponseEntity<User> responseEntity = new ResponseEntity<>(userToBeUpdated, HttpStatus.OK);
        return responseEntity;
    }

    // DELETE id

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
