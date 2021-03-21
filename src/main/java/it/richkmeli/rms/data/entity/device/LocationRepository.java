package it.richkmeli.rms.data.entity.device;

import it.richkmeli.rms.data.entity.device.model.Device;
import it.richkmeli.rms.data.entity.device.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LocationRepository extends JpaRepository<Location, String> {

}
