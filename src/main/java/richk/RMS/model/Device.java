package richk.RMS.model;

public class Device {
    private String name;
    private String IP;
    private String serverPort;
    private String lastConnection;
    private String encryptionKey;
    private String userAssociated;

    public Device(String name, String iP, String serverPort, String lastConnection, String encryptionKey, String userAssociated) {
        super();
        this.name = name;
        IP = iP;
        this.serverPort = serverPort;
        this.lastConnection = lastConnection;
        this.encryptionKey = encryptionKey;
        this.userAssociated = userAssociated;

    }

    public String getIP() {
        return IP;
    }

    public String getLastConnection() {
        return lastConnection;
    }

    public String getServerPort() {
        return serverPort;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public String getName() {
        return name;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public void setIP(String iP) {
        IP = iP;
    }

    public void setLastConnection(String lastConnection) {
        this.lastConnection = lastConnection;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public String getUserAssociated() {
        return userAssociated;
    }

    public void setUserAssociated(String userAssociated) {
        this.userAssociated = userAssociated;
    }
}
