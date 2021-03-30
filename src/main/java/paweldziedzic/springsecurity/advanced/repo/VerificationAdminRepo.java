package paweldziedzic.springsecurity.advanced.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import paweldziedzic.springsecurity.advanced.entity.VerificationAdmin;

@Repository
@Transactional
public interface VerificationAdminRepo extends JpaRepository<VerificationAdmin, Long> {

    VerificationAdmin findByValue(String value);
    void deleteByValue(String value);
}