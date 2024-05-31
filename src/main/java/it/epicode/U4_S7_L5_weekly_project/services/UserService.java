package it.epicode.U4_S7_L5_weekly_project.services;

import it.epicode.U4_S7_L5_weekly_project.entities.User;
import it.epicode.U4_S7_L5_weekly_project.exceptions.BadRequestException;
import it.epicode.U4_S7_L5_weekly_project.exceptions.NotFoundException;
import it.epicode.U4_S7_L5_weekly_project.payloads.UserRegisterRequestDTO;
import it.epicode.U4_S7_L5_weekly_project.payloads.UserRegisterResponseDTO;
import it.epicode.U4_S7_L5_weekly_project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcrypt;

    // GET all
    @Transactional(readOnly = true)
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    // GET id
    @Transactional(readOnly = true)
    public User getUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    // POST id
    @Transactional
    public UserRegisterResponseDTO saveUser(UserRegisterRequestDTO userPayload) {
        userRepository.findByEmail(userPayload.email()).ifPresent(user -> {
            throw new BadRequestException("Email " + user.getEmail() + " already in use.");
        });
        User newUser = new User(
                userPayload.firstName(),
                userPayload.lastName(),
                userPayload.email(),
                bcrypt.encode(userPayload.password()),
                "https://ui-avatars.com/api/?name=" +
                        userPayload.firstName() +
                        "+" + userPayload.lastName()
        );
        userRepository.save(newUser);
        return new UserRegisterResponseDTO(newUser.getId());
    }

    // PUT
    @Transactional
    public User updateUser(long id, User updatedUser) {
        User userToBeUpdated = this.getUserById(id);
        userToBeUpdated.setFirstName(updatedUser.getFirstName());
        userToBeUpdated.setLastName(updatedUser.getLastName());
        userToBeUpdated.setGender(updatedUser.getGender());
        userToBeUpdated.setEmail(updatedUser.getEmail());
        if (!updatedUser.getPassword().isEmpty()) {
            userToBeUpdated.setPassword(bcrypt.encode(updatedUser.getPassword()));
        }
        userToBeUpdated.setAvatarUrl("https://ui-avatars.com/api/?name="
                + updatedUser.getFirstName() + "+"
                + updatedUser.getLastName());
        userToBeUpdated.setRole(updatedUser.getRole());
        userRepository.save(userToBeUpdated);
        return userToBeUpdated;
    }

    // DELETE
    @Transactional
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    // findByEmail(String email)
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Item with " + email + " not found."));
    }

    // loadByUsername(String email) overriding
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .build();
    }
}
