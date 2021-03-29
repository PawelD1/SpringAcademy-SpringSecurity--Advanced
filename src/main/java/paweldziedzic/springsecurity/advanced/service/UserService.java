package paweldziedzic.springsecurity.advanced.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import paweldziedzic.springsecurity.advanced.entity.AppUser;
import paweldziedzic.springsecurity.advanced.entity.VerificationToken;
import paweldziedzic.springsecurity.advanced.repo.AppUserRepo;
import paweldziedzic.springsecurity.advanced.repo.VerificationTokenRepo;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Service
public class UserService {

    private AppUserRepo appUserRepo;
    private PasswordEncoder passwordEncoder;
    private VerificationTokenRepo verificationTokenRepo;
    private MailSenderService mailSenderService;

    public UserService(AppUserRepo appUserRepo, PasswordEncoder passwordEncoder, VerificationTokenRepo verificationTokenRepo, MailSenderService mailSenderService) {
        this.appUserRepo = appUserRepo;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenRepo = verificationTokenRepo;
        this.mailSenderService = mailSenderService;
    }

    public void addNewUser(AppUser user, HttpServletRequest request) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAdmin(user.isAdmin());
        System.out.println("AAAAAAADmin:" + user.isAdmin());
        appUserRepo.save(user);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepo.save(verificationToken);

        String url = "http://" + request.getServerName() +
                ":" +
                request.getServerPort() +
                request.getContextPath() +
                "/verify-token?token="+token;

        try {
            mailSenderService.sendMail(user.getUsername(), "Verification Token", url, false);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void verifyToken(String token) {
        AppUser appUser = verificationTokenRepo.findByValue(token).getAppUser();
        appUser.setEnabled(true);
        appUserRepo.save(appUser);
    }

    public AppUser findSavedUsersInDatabase(String username) {
        return appUserRepo.findAllByUsername(username);
    }

//    public boolean signInUser(AppUser appUser) {
//        AppUser user = appUserRepo.findAllByUsername(appUser.getUsername());
//        if(user != null) {
//            return appUser.getPassword().equals(user.getPassword());
//        }
//        return false;
//    }
public String signInUser(String username) {
    AppUser user = findSavedUsersInDatabase(username);
    if(user!=null) {
        return "OK!";
    } else {
        return "Username: "+ username + " does not exist, first you need to sign up!";
    }

}
}
