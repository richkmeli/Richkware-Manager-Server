package it.richkmeli.rms.data.model.rmc;

import it.richkmeli.rms.data.model.user.User;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity
@IdClass(RmcId.class)
public class Rmc {
    @Id
    @ManyToOne (fetch = FetchType.LAZY)
    private User associatedUser;
    @Id
    @Length(max = 68)
    private String rmcId;

    public Rmc() {
    }

    public Rmc(String associatedUser, String rmcId) {
        this.associatedUser = new User(associatedUser);
        this.rmcId = rmcId;
    }

    public String getAssociatedUser() {
        return associatedUser.getEmail();
    }

    public void setAssociatedUser(String associatedUser) {
        this.associatedUser = new User(associatedUser);
    }

    public String getRmcId() {
        return rmcId;
    }

    public void setRmcId(String rmcId) {
        this.rmcId = rmcId;
    }
}
