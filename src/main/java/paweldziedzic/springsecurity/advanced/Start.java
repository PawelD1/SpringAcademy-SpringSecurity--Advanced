//package paweldziedzic.springsecurity.advanced;
//
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import paweldziedzic.springsecurity.advanced.entity.AppUser;
//import paweldziedzic.springsecurity.advanced.repo.AppUserRepo;
//import paweldziedzic.springsecurity.advanced.service.MailSenderService;
//
//
//import javax.mail.MessagingException;
//
//@Component
//public class Start {
//
//    private PasswordEncoder passwordEncoder;
//    private AppUserRepo appUserRepo;
//
//    private MailSenderService mailSenderService;
//
//
//    public Start(PasswordEncoder passwordEncoder, AppUserRepo appUserRepo, MailSenderService mailSenderService) throws MessagingException {
//        this.passwordEncoder = passwordEncoder;
//        this.appUserRepo = appUserRepo;
//        this.mailSenderService = mailSenderService;
//
//        this.passwordEncoder = passwordEncoder;
//        this.appUserRepo = appUserRepo;
//
//        AppUser appUser = new AppUser();
//        appUser.setUsername("pawel@mail");
//        appUser.setEnabled(true);
//        appUser.setPassword(passwordEncoder.encode("pawel"));
//        appUserRepo.save(appUser);
//    }
//}
