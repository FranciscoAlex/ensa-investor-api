package ao.co.ensa.investor.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "investor_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "investor_code", unique = true, length = 30)
    private String investorCode;  // Unique investor identifier

    @Column(name = "document_type", length = 50)
    private String documentType;  // BI, Passport, etc.

    @Column(name = "document_number", length = 50)
    private String documentNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(length = 100)
    private String nationality;

    @Column(length = 100)
    private String province;  // Angola province

    @Column(length = 255)
    private String address;

    @Column(name = "risk_profile", length = 30)
    private String riskProfile;  // CONSERVATIVE, MODERATE, AGGRESSIVE

    @Column(name = "total_invested", precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal totalInvested = BigDecimal.ZERO;

    @Column(name = "account_status", length = 30)
    @Builder.Default
    private String accountStatus = "PENDING_VERIFICATION";

    @Column(name = "kyc_verified")
    @Builder.Default
    private boolean kycVerified = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
