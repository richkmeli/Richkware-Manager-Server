package it.richkmeli.rms.data.model.device;

import it.richkmeli.rms.data.model.user.User;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Device {
    @Id
    @NotNull
    @Length(max = 50)
    private String name;
    @NotNull
    @Length(max = 25)
    private String ip;
    @Length(max = 10)
    private String serverPort;
    @Length(max = 25)
    private String lastConnection;
    @Length(max = 32)
    private String encryptionKey;
    @ManyToOne(fetch = FetchType.LAZY)
    private User associatedUser;
    @Length(max = 1000)
    private String commands;
    @Length(max = 1000)
    private String commandsOutput;

    public Device() {
    }

    public Device(String name, String ip, String serverPort, String lastConnection, String encryptionKey, String associatedUser, String commands, String commandsOutput) {
        this.name = name;
        this.ip = ip;
        this.serverPort = serverPort;
        this.lastConnection = lastConnection;
        this.encryptionKey = encryptionKey;
        this.associatedUser = new User(associatedUser);
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
        return associatedUser.getEmail();
    }

    public void setAssociatedUser(String associatedUser) {
        this.associatedUser = new User(associatedUser);
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
