package it.richkmeli.rms.data.entity.device;

import it.richkmeli.jframework.auth.data.exception.AuthDatabaseException;
import it.richkmeli.rms.data.entity.device.model.Device;
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
    public List<Device> getAllDevices() throws AuthDatabaseException {
        return deviceRepository.findAll();
    }

    @Override
    public List<Device> getUserDevices(String user) throws AuthDatabaseException {
        return deviceRepository.findDevicesByAssociatedUser_Email(user);
    }

    @Override
    public Device addDevice(Device device) throws AuthDatabaseException {
        return deviceRepository.save(device);
    }

    @Override
    public Device editDevice(Device device) throws AuthDatabaseException {
        return deviceRepository.save(device);
    }

    @Override
    public void removeDevice(String device) throws AuthDatabaseException {
        deviceRepository.deleteById(device);
    }

    @Override
    public Device getDevice(String name) throws AuthDatabaseException {
        return deviceRepository.findById(name).orElse(null);
    }

    @Override
    public String getEncryptionKey(String name) throws AuthDatabaseException {
        Device device = deviceRepository.findById(name).orElse(null);
        if (device != null) {
            return device.getEncryptionKey();
        } else {
            return null;
        }
    }

    @Override
    public boolean editCommands(String deviceName, String commands) throws AuthDatabaseException {
        return false;
    }

    @Override
    public String getCommands(String deviceName) throws AuthDatabaseException {
        return null;
    }

    @Override
    public boolean setCommandsOutput(String deviceName, String commandsOutput) throws AuthDatabaseException {
        return false;
    }

    @Override
    public String getCommandsOutput(String deviceName) throws AuthDatabaseException {
        return null;
    }
}