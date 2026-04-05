package ao.co.ensa.investor.controller;

import ao.co.ensa.investor.model.dto.*;
import ao.co.ensa.investor.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Login, Register, Activation, and Password recovery")
public class AuthController {

    private final AuthService authService;

    @Value("${app.frontend-url:http://localhost:3000}")
    private String frontendUrl;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate with username/email and password. Returns JWT tokens.")
    public ResponseEntity<JwtAuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        JwtAuthResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @Operation(summary = "Register", description = "Create a new investor account. Sends activation email.")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterRequest registerRequest) {
        UserDTO user = authService.register(registerRequest);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/activate")
    @Operation(summary = "Activate account", description = "Activate account using the token from the activation email link. Redirects to the frontend login page.")
    public ResponseEntity<Void> activateAccount(@RequestParam String token) {
        try {
            authService.activateAccount(token);
            return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(frontendUrl + "/login?activated=true"))
                .build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(frontendUrl + "/login?activationError=true"))
                .build();
        }
    }

    @PostMapping("/resend-activation")
    @Operation(summary = "Resend activation email", description = "Send a new activation link to the given email.")
    public ResponseEntity<Map<String, String>> resendActivation(@Valid @RequestBody ResendActivationRequest request) {
        authService.resendActivationEmail(request.getEmail());
        return ResponseEntity.ok(Map.of("message", "If an account exists for this email, a new activation link has been sent."));
    }

    @PostMapping("/request-password-reset")
    @Operation(summary = "Request password reset", description = "Send a password reset link to the given email.")
    public ResponseEntity<Map<String, String>> requestPasswordReset(@Valid @RequestBody RequestPasswordResetRequest request) {
        authService.requestPasswordReset(request.getEmail());
        return ResponseEntity.ok(Map.of("message", "If an account exists for this email, a password reset link has been sent."));
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password", description = "Set a new password using the token from the password reset email link.")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(Map.of("message", "Password has been reset successfully. You can now log in."));
    }

    @GetMapping("/health")
    @Operation(summary = "Health Check", description = "Check if auth service is running")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("{\"status\": \"UP\", \"service\": \"ENSA Investor Auth\"}");
    }
}
