package it.richkmeli.RMS.data.device.model;

public class Device {
    private String name;
    private String ip;
    private String serverPort;
    private String lastConnection;
    private String encryptionKey;
    private String userAssociated;
    private String commands;

    public Device(String name, String ip, String serverPort, String lastConnection, String encryptionKey, String userAssociated, String commands) {
        this.name = name;
        this.ip = ip;
        this.serverPort = serverPort;
        this.lastConnection = lastConnection;
        this.encryptionKey = encryptionKey;
        this.userAssociated = userAssociated;
        this.commands = commands;
    }

    public String getIp() {
        return ip;
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

    public void setIp(String iP) {
        ip = iP;
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

    public String getCommands() {
        return commands;
    }

    public void setCommands(String commands) {
        this.commands = commands;
    }
}
