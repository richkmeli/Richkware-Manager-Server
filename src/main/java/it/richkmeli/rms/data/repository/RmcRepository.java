package it.richkmeli.rms.data.repository;

import it.richkmeli.rms.data.model.rmc.Rmc;
import it.richkmeli.rms.data.model.rmc.RmcId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RmcRepository extends JpaRepository<Rmc, RmcId> {
}
