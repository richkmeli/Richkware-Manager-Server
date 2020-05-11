package it.richkmeli.rms.data.model.device;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DeviceRepository extends JpaRepository<Device, String> {

    List<Device> findDevicesByAssociatedUser(String userEmail);
}
