package it.richkmeli.rms.data.entity.configuration;


import it.richkmeli.jframework.util.log.Logger;
import it.richkmeli.rms.data.entity.configuration.model.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ConfigurationDatabaseManager {
    private static ConfigurationRepository configurationRepository;

    @Autowired
    public ConfigurationDatabaseManager(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    public static ConfigurationDatabaseManager getInstance() {
        return new ConfigurationDatabaseManager(configurationRepository);
    }

    public String getValue(String key) {
        return configurationRepository.findById(key).map(Configuration::getValue).orElse(null);
    }

    public Configuration saveValue(String key, String value) {
        Configuration configuration = new Configuration(key,value);
        return saveValue(configuration);
    }

    public Configuration saveValue(Configuration configuration) {
        return configurationRepository.save(configuration);
    }

    public List<Configuration> getAllConfigurations() {
        return configurationRepository.findAll();
    }


}