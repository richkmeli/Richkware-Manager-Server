package it.richkmeli.rms.data.entity.device;

import it.richkmeli.rms.data.entity.device.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DeviceRepository extends JpaRepository<Device, String> {

    List<Device> findDevicesByAssociatedUser_Email(String email);
}
