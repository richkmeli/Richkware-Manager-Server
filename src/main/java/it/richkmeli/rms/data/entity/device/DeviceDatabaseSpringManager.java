package it.richkmeli.rms.data.entity.device;

import it.richkmeli.jframework.auth.data.exception.AuthDatabaseException;
import it.richkmeli.rms.data.entity.device.model.Device;
import it.richkmeli.rms.data.entity.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Using Spring/Hibernate ORM
 */

@Component
public class DeviceDatabaseSpringManager implements DeviceDatabaseModel {
    private static DeviceRepository deviceRepository;
    private static LocationRepository locationRepository;
    private static DeviceInfoRepository deviceInfoRepository;

    @Autowired
    public DeviceDatabaseSpringManager(DeviceRepository deviceRepository, LocationRepository locationRepository, DeviceInfoRepository deviceInfoRepository) {
        this.deviceRepository = deviceRepository;
        this.locationRepository = locationRepository;
        this.deviceInfoRepository = deviceInfoRepository;
    }

    public static DeviceDatabaseSpringManager getInstance() {
        return new DeviceDatabaseSpringManager(deviceRepository, locationRepository, deviceInfoRepository);
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
    public void removeDevice(String name) throws AuthDatabaseException {
        Device device = deviceRepository.findDeviceByName(name);
        if(device != null) {
            deviceRepository.delete(device);
        }
    }

    @Override
    public Device getDevice(String name) throws AuthDatabaseException {
        return deviceRepository.findDeviceByName(name);
    }

    @Override
    public String getEncryptionKey(String name) throws AuthDatabaseException {
        Device device = deviceRepository.findDeviceByName(name);
        if (device != null) {
            return device.getEncryptionKey();
        } else {
            return null;
        }
    }

    @Override
    public boolean editCommands(String deviceName, String commands) throws AuthDatabaseException {
        Device device = deviceRepository.findDeviceByName(deviceName);
        if (device != null) {
            device.setCommands(commands);
            deviceRepository.save(device);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getCommands(String deviceName) throws AuthDatabaseException {
        Device device = deviceRepository.findDeviceByName(deviceName);
        if (device != null) {
            return device.getCommands();
        } else {
            return "";
        }
    }

    @Override
    public boolean setCommandsOutput(String deviceName, String commandsOutput) throws AuthDatabaseException {
        Device device = deviceRepository.findDeviceByName(deviceName);
        if (device != null) {
            device.setCommandsOutput(commandsOutput);
            deviceRepository.save(device);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getCommandsOutput(String deviceName) throws AuthDatabaseException {
        Device device = deviceRepository.findDeviceByName(deviceName);
        if (device != null) {
            return device.getCommandsOutput();
        } else {
            return "";
        }
    }
}