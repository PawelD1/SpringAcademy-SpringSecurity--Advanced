package paweldziedzic.springsecurity.advanced.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import paweldziedzic.springsecurity.advanced.entity.VerificationToken;

@Repository
@Transactional
public interface VerificationTokenRepo extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByValue(String value);
    void deleteByValue(String value);
}
