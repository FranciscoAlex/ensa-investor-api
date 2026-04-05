package ao.co.ensa.investor.service;

import ao.co.ensa.investor.exception.ResourceNotFoundException;
import ao.co.ensa.investor.model.dto.UserDTO;
import ao.co.ensa.investor.model.entity.User;
import ao.co.ensa.investor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return mapToDTO(user);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return mapToDTO(user);
    }

    private UserDTO mapToDTO(User user) {
        return UserDTO.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .fullName(user.getFullName())
            .phoneNumber(user.getPhoneNumber())
            .gender(user.getGender())
            .roles(user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet()))
            .emailVerified(user.isEmailVerified())
            .lastLoginAt(user.getLastLoginAt())
            .build();
    }
}
