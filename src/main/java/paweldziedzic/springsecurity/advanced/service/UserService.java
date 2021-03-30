package paweldziedzic.springsecurity.advanced.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import paweldziedzic.springsecurity.advanced.entity.AppUser;
import paweldziedzic.springsecurity.advanced.entity.VerificationAdmin;
import paweldziedzic.springsecurity.advanced.entity.VerificationToken;
import paweldziedzic.springsecurity.advanced.repo.AppUserRepo;
import paweldziedzic.springsecurity.advanced.repo.VerificationAdminRepo;
import paweldziedzic.springsecurity.advanced.repo.VerificationTokenRepo;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Service
public class UserService {

    @Value("${admin.mail}")
    private String adminMail;

    public static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private AppUserRepo appUserRepo;
    private PasswordEncoder passwordEncoder;
    private VerificationTokenRepo verificationTokenRepo;
    private MailSenderService mailSenderService;
    private VerificationAdminRepo verificationAdminRepo;

    public UserService(AppUserRepo appUserRepo, PasswordEncoder passwordEncoder,
                       VerificationTokenRepo verificationTokenRepo, VerificationAdminRepo verificationAdminRepo,
                       MailSenderService mailSenderService) {
        this.appUserRepo = appUserRepo;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenRepo = verificationTokenRepo;
        this.verificationAdminRepo = verificationAdminRepo;
        this.mailSenderService = mailSenderService;
    }

    public void addNewUser(AppUser newUser, HttpServletRequest request) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        if(appUserRepo.findByUsername(newUser.getUsername()).isPresent())
            LOGGER.error("USER WITH GIVEN EMAIL: " + newUser.getUsername()+ " ALREADY HAS AN ACCOUNT");

        appUserRepo.save(newUser);
        LOGGER.info("OK, NEW ACCOUNT HAS BEEN CREATED");

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(newUser, token);
        verificationTokenRepo.save(verificationToken);

        String url = "http://" + request.getServerName() +
                ":" +
                request.getServerPort() +
                request.getContextPath() +
                "/verify-token?token="+token;

        try {
            mailSenderService.sendMail(newUser.getUsername(), "Verification Token", url, false);
            if(newUser.getRoles().contains("ROLE_ADMIN")) {
                VerificationAdmin verificationAdmin = new VerificationAdmin(newUser, token);
                verificationAdminRepo.save(verificationAdmin);

                String confirmationUrlNewAdmin = "http://" + request.getServerName() + ":" + request.getServerPort() +
                        request.getContextPath() + "/verify-token-new-admin?token=" + token;

                mailSenderService.sendMail(adminMail,
                        "Verification Token: NEW REQUEST to add a NEW additional ADMIN",
                        confirmationUrlNewAdmin,
                        false);

                appUserRepo.findByUsername(newUser.getUsername()).get().getRoles().remove("ROLE_ADMIN");
                appUserRepo.save(newUser);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void verifyToken(String token) {
        AppUser appUser = verificationTokenRepo.findByValue(token).getAppUser();
        appUser.setEnabled(true);
        appUserRepo.save(appUser);
        LOGGER.info("ROLE USER HAS BEEN ACTIVATED. IN CASE OF ROLE ADMIN YOU NEED TO WAIT ON ACCEPTANCE");
        verificationTokenRepo.deleteByValue(token);
        LOGGER.info("USED TOKEN IS NOT NECESSARY ANYMORE");
    }

    public void verifyAdmin(String token){
        AppUser newUser = verificationAdminRepo.findByValue(token).getAppUser();
        newUser.getRoles().add("ROLE_ADMIN");
        newUser.setEnabled(true);
        appUserRepo.save(newUser);
        LOGGER.info("ROLE ADMIN HAS BEEN CONFIRMED AND ACTIVATED");
        verificationAdminRepo.deleteByValue(token);
        LOGGER.info("USED TOKEN IS NOT NECESSARY ANYMORE");
    }
}