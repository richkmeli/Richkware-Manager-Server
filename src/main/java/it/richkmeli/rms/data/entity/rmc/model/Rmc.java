package it.richkmeli.rms.data.entity.rmc.model;

import it.richkmeli.rms.data.entity.user.model.User;
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

    public Rmc(User associatedUser, String rmcId) {
        this.associatedUser = associatedUser;
        this.rmcId = rmcId;
    }

    public User getAssociatedUser() {
        return associatedUser;
    }

    public void setAssociatedUser(User associatedUser) {
        this.associatedUser = associatedUser;
    }

    public String getRmcId() {
        return rmcId;
    }

    public void setRmcId(String rmcId) {
        this.rmcId = rmcId;
    }
}
