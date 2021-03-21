package it.richkmeli.rms.data.entity.device.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import it.richkmeli.rms.data.entity.user.model.User;
import it.richkmeli.rms.util.GeoLocation;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Device {
    @Id
    @NotNull
    @Length(max = 64)
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
    @JsonBackReference
    @JoinColumn(name="user_email")
    private User associatedUser;
    @Length(max = 1000)
    private String commands;
    @Length(max = 1000)
    private String commandsOutput;
    @Length(max = 64)
    private String installationId;
    @OneToOne(mappedBy = "device", fetch = FetchType.LAZY, cascade = {CascadeType.ALL}/*, orphanRemoval = true*/)
    @PrimaryKeyJoinColumn
    @JsonBackReference
    private Location location;
    @OneToOne(mappedBy = "device", fetch = FetchType.LAZY, cascade = {CascadeType.ALL}/*, orphanRemoval = true*/)
    @PrimaryKeyJoinColumn
    @JsonBackReference
    private DeviceInfo deviceInfo;

    public Device() {
    }

    public Device(String name, String ip, String serverPort, String lastConnection, String encryptionKey, User associatedUser, String commands, String commandsOutput, String installationId, Location location, DeviceInfo deviceInfo) {
        this.name = name;
        this.ip = ip;
        this.serverPort = serverPort;
        this.lastConnection = lastConnection;
        this.encryptionKey = encryptionKey;
        this.associatedUser = associatedUser;
        this.commands = commands;
        this.commandsOutput = commandsOutput;
        this.installationId = installationId;
        this.location = location;
        if (this.location != null) {
            this.location.setId(name);
            this.location.setDevice(this);
        }
        this.deviceInfo = deviceInfo;
        if (this.deviceInfo != null) {
            this.deviceInfo.setId(name);
            this.deviceInfo.setDevice(this);
        }
    }

    public String getInstallationId() {
        return installationId;
    }

    public void setInstallationId(String installationId) {
        this.installationId = installationId;
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

    public User getAssociatedUser() {
        return associatedUser;
    }

    public void setAssociatedUser(User associatedUser) {
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    // called by the serialization, location is not serialized due to JsonBackReference
    public String getLocationAsPosition() {
        return GeoLocation.getPositionFromCoordinates(location.getLongitude(), location.getLatitude(), location.getAltitude());
    }

    // called by the serialization, location is not serialized due to JsonBackReference
    public String getAssociatedUserEmail() {
        return associatedUser.getEmail();
    }

    // called by the serialization, location is not serialized due to JsonBackReference
    public String getDeviceInfoDevName() {
        return deviceInfo.getDevName();
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
                + getCommandsOutput() + ", "
                + getInstallationId() + ", "
                + getLocation() + ", "
                + getDeviceInfo()
                + "}";

        return output;
    }
}
