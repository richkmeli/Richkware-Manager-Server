package it.richkmeli.rms.data.rmc.model;

public class RMC {

    public String user;
    public String rmcId;

    public RMC(String user, String clientID) {
        this.user = user;
        this.rmcId = clientID;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRmcId() {
        return rmcId;
    }

    public void setRmcId(String clientID) {
        this.rmcId = clientID;
    }
}
