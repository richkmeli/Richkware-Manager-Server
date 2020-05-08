package it.richkmeli.rms.data.model.device;

import it.richkmeli.jframework.orm.DatabaseException;
import it.richkmeli.rms.data.model.user.AuthDatabaseSpringManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Using Spring/Hibernate ORM
 */

@Component
public class DeviceDatabaseSpringManager implements DeviceDatabaseModel {
    private static DeviceRepository deviceRepository;

    public static DeviceDatabaseSpringManager getInstance() {
        return new DeviceDatabaseSpringManager(deviceRepository);
    }

    @Autowired
    public DeviceDatabaseSpringManager(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public List<Device> getAllDevices() throws DatabaseException {
        return deviceRepository.findAll();
    }

    @Override
    public List<Device> getUserDevices(String user) throws DatabaseException {
        return deviceRepository.findDevicesByAssociatedUser(user);
    }

    @Override
    public Device addDevice(Device device) throws DatabaseException {
        return deviceRepository.save(device);
    }

    @Override
    public Device editDevice(Device device) throws DatabaseException {
        return deviceRepository.save(device);
    }

    @Override
    public void removeDevice(String device) throws DatabaseException {
        deviceRepository.deleteById(device);
    }

    @Override
    public Device getDevice(String name) throws DatabaseException {
        return deviceRepository.findById(name).orElse(null);
    }

    @Override
    public String getEncryptionKey(String name) throws DatabaseException {
        Device device = deviceRepository.findById(name).orElse(null);
        if (device != null) {
            return device.getEncryptionKey();
        } else {
            return null;
        }
    }

    @Override
    public boolean editCommands(String deviceName, String commands) throws DatabaseException {
        return false;
    }

    @Override
    public String getCommands(String deviceName) throws DatabaseException {
        return null;
    }

    @Override
    public boolean setCommandsOutput(String deviceName, String commandsOutput) throws DatabaseException {
        return false;
    }

    @Override
    public String getCommandsOutput(String deviceName) throws DatabaseException {
        return null;
    }
}