package it.richkmeli.rms.data.rmc.model;

import it.richkmeli.jframework.orm.annotation.Id;

public class RMC {

    @Id
    private String associatedUser;
    @Id
    private String rmcId;

    public RMC(String associatedUser, String rmcId) {
        this.associatedUser = associatedUser;
        this.rmcId = rmcId;
    }

    public String getAssociatedUser() {
        return associatedUser;
    }

    public void setAssociatedUser(String associatedUser) {
        this.associatedUser = associatedUser;
    }

    public String getRmcId() {
        return rmcId;
    }

    public void setRmcId(String rmcId) {
        this.rmcId = rmcId;
    }
}
