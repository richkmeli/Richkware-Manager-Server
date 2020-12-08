package it.richkmeli.rms.data.entity.configuration;

import it.richkmeli.rms.data.entity.configuration.model.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, String> {

    //Configuration findConfigurationByCKey(String cKey);
}