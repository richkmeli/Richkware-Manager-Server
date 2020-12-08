package it.richkmeli.rms.data.entity.configuration;

import it.richkmeli.jframework.util.log.Logger;
import it.richkmeli.rms.data.entity.configuration.model.Configuration;

import java.time.Clock;
import java.util.Date;
import java.util.List;

public class ConfigurationManager {
    private static final int REFRESH_CONFIGURATIONS_TIMEOUT = 60;
    private static List<Configuration> configurations;
    private static Date timeWhenUpdate;

    private static void init() {
        if (configurations == null) {
            // load configuration in RAM from DB
            ConfigurationDatabaseManager configurationDatabaseManager = ConfigurationDatabaseManager.getInstance();
            configurations = configurationDatabaseManager.getAllConfigurations();
            timeWhenUpdate = getTimeWhenUpdate();

            if (configurations == null || configurations.size() < ConfigurationEnum.values().length) {
                Logger.info("DB is empty or is missing some entries, load default values to DB");
                for (ConfigurationEnum configurationEnum : ConfigurationEnum.values()) {
                    // check if the DB contains a specific entry
                    if (configurations.contains(configurationEnum.getKey())) {
                        Configuration dbConfiguration = configurations.get(configurations.indexOf(configurationEnum.getKey()));
                        // check differences about the entry value
                        if (dbConfiguration.getValue().equalsIgnoreCase(configurationEnum.getDefaultValue())) {
                            // same value, OK
                        } else {
                            // different value, keep value of DB to avoid restore to default setting
                        }
                    } else {
                        // DB doesn't contain the entry, same default value to DB
                        Configuration defaultConfiguration = new Configuration(configurationEnum.getKey(), configurationEnum.getDefaultValue());
                        configurationDatabaseManager.saveValue(defaultConfiguration);
                    }
                }
                // upload default values also in RAM
                configurations = configurationDatabaseManager.getAllConfigurations();
            } else if (configurations.size() > ConfigurationEnum.values().length) {
                // not possible get a key present in DB and not present in ConfigurationEnum. (init is call when DB return null)
                Logger.error("DB contains more keys than application need, the entries present only in the DB have to be deleted or inserted in the application");
            } else {
                // not possible get a key present in DB and not present in ConfigurationEnum. (init is call when DB return null)
                Logger.info("DB and ConfigurationEnum have the same size");
            }
        } else {
            // configuration contains already configurations
            if (getCurrentTime().compareTo(timeWhenUpdate) > 0) {
                // refresh RAM with DB
                configurations = null;
                init();
            } else {
                // timeout not elapsed
            }
        }
    }

    private static Date getCurrentTime() {
        return Date.from(Clock.systemUTC().instant());
    }

    private static Date getTimeWhenUpdate() {
        return Date.from(Clock.systemUTC().instant().plusSeconds(REFRESH_CONFIGURATIONS_TIMEOUT));
    }

    public static String getValueWithCacheSystem(ConfigurationEnum configurationEnum) {
        init();
        for (Configuration s : configurations) {
            if (configurationEnum.getKey().equalsIgnoreCase(s.getKey())) {
                return s.getValue();
            }
        }
        return null;
    }

    public static String getValue(ConfigurationEnum configurationEnum) {
        String value = ConfigurationDatabaseManager.getInstance().getValue(configurationEnum.getKey());
        if (value == null) {
            // initialization of the database
            init();
            value = getValue(configurationEnum);
        }
        return value;
    }


}
