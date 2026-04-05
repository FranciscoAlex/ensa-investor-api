package ao.co.ensa.investor.service;

import ao.co.ensa.investor.exception.BadRequestException;
import ao.co.ensa.investor.exception.ResourceNotFoundException;
import ao.co.ensa.investor.model.dto.*;
import ao.co.ensa.investor.model.entity.Role;
import ao.co.ensa.investor.model.entity.User;
import ao.co.ensa.investor.model.enums.RoleType;
import ao.co.ensa.investor.repository.RoleRepository;
import ao.co.ensa.investor.repository.UserRepository;
import ao.co.ensa.investor.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private static final int ACTIVATION_TOKEN_VALID_HOURS = 24;
    private static final int PASSWORD_RESET_TOKEN_VALID_HOURS = 1;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final EmailService emailService;

    /**
     * Authenticate user and return JWT tokens
     */
    public JwtAuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsernameOrEmail(),
                loginRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication);

        // Update last login only; account stays disabled until activated via email link
        userRepository.findByUsernameOrEmail(
            loginRequest.getUsernameOrEmail(),
            loginRequest.getUsernameOrEmail()
        ).ifPresent(user -> {
            user.setLastLoginAt(LocalDateTime.now());
            userRepository.save(user);
        });

        User user = userRepository.findByUsernameOrEmail(
            loginRequest.getUsernameOrEmail(),
            loginRequest.getUsernameOrEmail()
        ).orElseThrow();

        return JwtAuthResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .user(mapToUserDTO(user))
            .build();
    }

    /**
     * Register a new investor user
     */
    @Transactional
    public UserDTO register(RegisterRequest registerRequest) {
        // Check for existing username
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new BadRequestException("Username already exists: " + registerRequest.getUsername());
        }

        // Check for existing email
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException("Email already registered: " + registerRequest.getEmail());
        }

        // Check for existing NIF
        if (registerRequest.getNif() != null && userRepository.existsByNif(registerRequest.getNif())) {
            throw new BadRequestException("NIF already registered: " + registerRequest.getNif());
        }

        // Get default INVESTOR role
        Role investorRole = roleRepository.findByName(RoleType.INVESTOR)
            .orElseThrow(() -> new BadRequestException("Default role INVESTOR not found. Please seed the database."));

        String activationToken = UUID.randomUUID().toString().replace("-", "");
        LocalDateTime activationExpires = LocalDateTime.now().plusHours(ACTIVATION_TOKEN_VALID_HOURS);

        // Build user entity: enabled so user can login immediately; activation link only sets emailVerified
        User user = User.builder()
            .fullName(registerRequest.getFullName())
            .username(registerRequest.getUsername())
            .email(registerRequest.getEmail())
            .password(passwordEncoder.encode(registerRequest.getPassword()))
            .phoneNumber(registerRequest.getPhoneNumber())
            .nif(registerRequest.getNif())
            .gender(registerRequest.getGender())
            .roles(Collections.singleton(investorRole))
            .enabled(true)
            .emailVerified(false)
            .activationToken(activationToken)
            .activationTokenExpiresAt(activationExpires)
            .build();

        User savedUser = userRepository.save(user);
        String activationLink = baseUrl + "/api/v1/auth/activate?token=" + activationToken;
        emailService.sendVerificationEmail(savedUser.getEmail(), savedUser.getFullName(), activationLink);
        log.info("New investor registered: {} ({}). Activation email sent.", savedUser.getUsername(), savedUser.getEmail());

        return mapToUserDTO(savedUser);
    }

    /**
     * Activate account using the token sent by email.
     */
    @Transactional
    public void activateAccount(String token) {
        User user = userRepository.findByActivationToken(token)
            .orElseThrow(() -> new BadRequestException("Invalid or expired activation link."));
        if (user.getActivationTokenExpiresAt() == null || user.getActivationTokenExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Activation link has expired. Request a new one.");
        }
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setActivationToken(null);
        user.setActivationTokenExpiresAt(null);
        userRepository.save(user);
        emailService.sendWelcomeEmail(user.getEmail(), user.getFullName());
        log.info("Account activated for user: {}", user.getUsername());
    }

    /**
     * Resend activation email for an unverified account.
     */
    @Transactional
    public void resendActivationEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        if (user.isEmailVerified()) {
            throw new BadRequestException("Account is already activated.");
        }
        String activationToken = UUID.randomUUID().toString().replace("-", "");
        user.setActivationToken(activationToken);
        user.setActivationTokenExpiresAt(LocalDateTime.now().plusHours(ACTIVATION_TOKEN_VALID_HOURS));
        userRepository.save(user);
        String activationLink = baseUrl + "/api/v1/auth/activate?token=" + activationToken;
        emailService.sendVerificationEmail(user.getEmail(), user.getFullName(), activationLink);
        log.info("Activation email resent to: {}", user.getEmail());
    }

    /**
     * Request password reset; sends email with link.
     */
    @Transactional
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            // Do not reveal whether email exists
            log.debug("Password reset requested for unknown email: {}", email);
            return;
        }
        String resetToken = UUID.randomUUID().toString().replace("-", "");
        user.setPasswordResetToken(resetToken);
        user.setPasswordResetTokenExpiresAt(LocalDateTime.now().plusHours(PASSWORD_RESET_TOKEN_VALID_HOURS));
        userRepository.save(user);
        String resetLink = baseUrl + "/api/v1/auth/reset-password?token=" + resetToken;
        emailService.sendPasswordResetEmail(user.getEmail(), user.getFullName(), resetLink);
        log.info("Password reset email sent to: {}", user.getEmail());
    }

    /**
     * Reset password using the token from the email link.
     */
    @Transactional
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByPasswordResetToken(token)
            .orElseThrow(() -> new BadRequestException("Invalid or expired password reset link."));
        if (user.getPasswordResetTokenExpiresAt() == null || user.getPasswordResetTokenExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Password reset link has expired. Request a new one.");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiresAt(null);
        userRepository.save(user);
        log.info("Password reset completed for user: {}", user.getUsername());
    }

    private UserDTO mapToUserDTO(User user) {
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
