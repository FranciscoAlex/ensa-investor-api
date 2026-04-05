package ao.co.ensa.investor.repository;

import ao.co.ensa.investor.model.entity.InvestorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvestorRepository extends JpaRepository<InvestorProfile, Long> {

    Optional<InvestorProfile> findByUserId(Long userId);

    Optional<InvestorProfile> findByInvestorCode(String investorCode);

    boolean existsByInvestorCode(String investorCode);
}
