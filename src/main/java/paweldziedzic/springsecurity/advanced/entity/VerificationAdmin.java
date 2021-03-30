package paweldziedzic.springsecurity.advanced.entity;

import javax.persistence.*;

@Entity
public class VerificationAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value;

    @OneToOne
    private AppUser appUser;

    public VerificationAdmin(AppUser appUser, String value) {
        this.appUser = appUser;
        this.value = value;
    }

    public VerificationAdmin() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }
}