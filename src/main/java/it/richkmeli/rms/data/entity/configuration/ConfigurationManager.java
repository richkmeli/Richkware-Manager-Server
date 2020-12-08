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

            if (configurations == null || configurations.isEmpty()) {
                // DB is empty, load default values to DB
                Logger.info("DB is empty, load default values to DB");
                for (ConfigurationEnum configurationEnum : ConfigurationEnum.values()) {
                    //Logger.info(configurationEnum.getKey() + ":" +configurationEnum.getDefaultValue());
                    Configuration configuration = new Configuration(configurationEnum.getKey(), configurationEnum.getDefaultValue());
                    configurationDatabaseManager.saveValue(configuration);
                }
                // upload default values also in RAM
                configurations = configurationDatabaseManager.getAllConfigurations();
            }else {
                // DB contains already configurations, local memory just updated, nothing to do
            }
        } else {
            // configuration contains already configurations
            if(getCurrentTime().compareTo(timeWhenUpdate) > 0){
                // refresh RAM with DB
                configurations = null;
                init();
            }else {
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
        for(Configuration s: configurations){
            if(configurationEnum.getKey().equalsIgnoreCase(s.getKey())){
                return s.getValue();
            }
        }
        return null;
    }

    public static String getValue(ConfigurationEnum configurationEnum) {
        String value = ConfigurationDatabaseManager.getInstance().getValue(configurationEnum.getKey());
        if (value == null){
            // initialization of the database
            init();
            value = getValue(configurationEnum);
        }
        return value;
    }


}
