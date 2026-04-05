package ao.co.ensa.investor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username:noreply@ensa.ao}")
    private String fromEmail;

    @Value("${app.frontend-url:http://localhost:3000}")
    private String frontendUrl;

    /**
     * Send welcome email after account activation (async)
     */
    @Async
    public void sendWelcomeEmail(String toEmail, String fullName) {
        try {
            Context context = new Context();
            context.setVariable("name", fullName);
            context.setVariable("platformName", "Portal do Investidor ENSA");
            context.setVariable("loginUrl", frontendUrl + "/login");

            String htmlContent = templateEngine.process("welcome-email", context);
            sendHtmlEmail(toEmail, "Bem-vindo ao Portal do Investidor ENSA", htmlContent);
            log.info("Welcome email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send welcome email to {}: {}", toEmail, e.getMessage());
        }
    }

    /**
     * Send account activation / verification link (async)
     */
    @Async
    public void sendVerificationEmail(String toEmail, String fullName, String verificationLink) {
        try {
            Context context = new Context();
            context.setVariable("name", fullName);
            context.setVariable("verificationLink", verificationLink);
            context.setVariable("platformName", "ENSA Investor Portal");

            String htmlContent = templateEngine.process("verification-email", context);
            sendHtmlEmail(toEmail, "Ative a sua conta — ENSA Investidor", htmlContent);
            log.info("Verification email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send verification email to {}: {}", toEmail, e.getMessage());
        }
    }

    /**
     * Send password reset link (async)
     */
    @Async
    public void sendPasswordResetEmail(String toEmail, String fullName, String resetLink) {
        try {
            Context context = new Context();
            context.setVariable("name", fullName);
            context.setVariable("resetLink", resetLink);
            context.setVariable("platformName", "ENSA Investor Portal");

            String htmlContent = templateEngine.process("password-reset-email", context);
            sendHtmlEmail(toEmail, "Recuperação de palavra-passe — ENSA Investidor", htmlContent);
            log.info("Password reset email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send password reset email to {}: {}", toEmail, e.getMessage());
        }
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}
