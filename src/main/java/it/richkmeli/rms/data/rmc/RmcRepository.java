package it.richkmeli.rms.data.rmc;

import it.richkmeli.rms.data.rmc.model.Rmc;
import it.richkmeli.rms.data.rmc.model.RmcId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RmcRepository extends JpaRepository<Rmc, RmcId> {
}
