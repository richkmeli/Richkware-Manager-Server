package it.richkmeli.rms.data.rmc.model;

public class RMC {

    public String account;
    public String rmcId;

    public RMC(String user, String clientID) {
        this.account = user;
        this.rmcId = clientID;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String user) {
        this.account = user;
    }

    public String getRmcId() {
        return rmcId;
    }

    public void setRmcId(String clientID) {
        this.rmcId = clientID;
    }
}
