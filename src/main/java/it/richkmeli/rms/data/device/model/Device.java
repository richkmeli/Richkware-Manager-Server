package it.richkmeli.rms.data.device.model;

import it.richkmeli.jframework.orm.annotation.Id;

public class Device {
    @Id
    private String name;
    private String ip;
    private String serverPort;
    private String lastConnection;
    private String encryptionKey;
    private String associatedUser;
    private String commands;
    private String commandsOutput;

    public Device(String name, String ip, String serverPort, String lastConnection, String encryptionKey, String associatedUser, String commands, String commandsOutput) {
        this.name = name;
        this.ip = ip;
        this.serverPort = serverPort;
        this.lastConnection = lastConnection;
        this.encryptionKey = encryptionKey;
        this.associatedUser = associatedUser;
        this.commands = commands;
        this.commandsOutput = commandsOutput;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getLastConnection() {
        return lastConnection;
    }

    public void setLastConnection(String lastConnection) {
        this.lastConnection = lastConnection;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public String getAssociatedUser() {
        return associatedUser;
    }

    public void setAssociatedUser(String associatedUser) {
        this.associatedUser = associatedUser;
    }

    public String getCommands() {
        return commands;
    }

    public void setCommands(String commands) {
        this.commands = commands;
    }

    public String getCommandsOutput() {
        return commandsOutput;
    }

    public void setCommandsOutput(String commandsOutput) {
        this.commandsOutput = commandsOutput;
    }

    @Override
    public String toString() {
        String output = "";
        output = "{" + getName() + ", "
                + getIp() + ", "
                + getServerPort() + ", "
                + getLastConnection() + ", "
                + getEncryptionKey() + ", "
                + getAssociatedUser() + ", "
                + getCommands() + ", "
                + getCommandsOutput()
                + "}";

        return output;
    }
}
