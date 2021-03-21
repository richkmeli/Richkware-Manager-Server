package it.richkmeli.rms.data.entity.device;

import it.richkmeli.rms.data.entity.device.model.Device;
import it.richkmeli.rms.data.entity.device.model.Location;
import it.richkmeli.rms.data.entity.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;


@Repository
public interface DeviceRepository extends JpaRepository<Device, String> {

    List<Device> findDevicesByAssociatedUser_Email(String email);

    boolean existsDeviceByName(String name);
    void deleteDeviceByName(String name);
    Device findDeviceByName(String name);
}
