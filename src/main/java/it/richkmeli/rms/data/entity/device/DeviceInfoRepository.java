package it.richkmeli.rms.data.entity.device;

import it.richkmeli.rms.data.entity.device.model.Device;
import it.richkmeli.rms.data.entity.device.model.DeviceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DeviceInfoRepository extends JpaRepository<DeviceInfo, String> {

}
