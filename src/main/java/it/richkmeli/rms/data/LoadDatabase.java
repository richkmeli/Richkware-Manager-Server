package it.richkmeli.rms.data;


import it.richkmeli.jframework.util.RandomStringGenerator;
import it.richkmeli.rms.data.entity.configuration.ConfigurationDatabaseManager;
import it.richkmeli.rms.data.entity.configuration.ConfigurationEnum;
import it.richkmeli.rms.data.entity.configuration.ConfigurationManager;
import it.richkmeli.rms.data.entity.device.DeviceDatabaseSpringManager;
import it.richkmeli.rms.data.entity.device.model.Device;
import it.richkmeli.rms.data.entity.rmc.RmcDatabaseSpringManager;
import it.richkmeli.rms.data.entity.rmc.model.Rmc;
import it.richkmeli.rms.data.entity.user.AuthDatabaseSpringManager;
import it.richkmeli.rms.data.entity.user.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Annotating a class with the @Configuration annotation indicates
// that the class will be used by JavaConfig as a source of bean definitions.
@Configuration
class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(AuthDatabaseSpringManager authDatabaseSpringManager, DeviceDatabaseSpringManager deviceDatabaseSpringManager, RmcDatabaseSpringManager rmcDatabaseSpringManager, ConfigurationDatabaseManager configurationDatabaseManager) {
        return args -> {

            // add login entry
            User adminUser = new User("richk@i.it", "00000000", true);
            if (!authDatabaseSpringManager.isUserPresent(adminUser.getEmail())) {
                authDatabaseSpringManager.addUser(adminUser);
            }


            // test
            User user = new User(RandomStringGenerator.generateAlphanumericString(4) + "@example.com", "00000000", false);
            User user1 = new User("richk2@i.it", "00000000", false);
            authDatabaseSpringManager.addUser(user);
            authDatabaseSpringManager.removeUser(user.getEmail());
            authDatabaseSpringManager.addUser(user1);
            // user1 will be deleted later in RMC test phase


            Device device = new Device(RandomStringGenerator.generateAlphanumericString(4), "43.34.43.34", "40", "20-10-18", "ckeroivervioeon", "richk@i.it", "", "","iid","loc");
            Device device1 = new Device(RandomStringGenerator.generateAlphanumericString(4), "43.34.43.34", "40", "20-10-18", "ckeroivervioeon", "richk2@i.it", "", "","iid","loc");
            deviceDatabaseSpringManager.addDevice(device);
            deviceDatabaseSpringManager.addDevice(device1);
            deviceDatabaseSpringManager.removeDevice(device.getName());
            deviceDatabaseSpringManager.removeDevice(device1.getName());

            Rmc rmc = new Rmc(adminUser.getEmail(), "test_rmc_ID");
            Rmc rmc2 = new Rmc("richk2@i.it", "test_rmc_ID" + RandomStringGenerator.generateAlphanumericString(4));

            rmcDatabaseSpringManager.addRMC(rmc);
            rmcDatabaseSpringManager.addRMC(rmc2);

            authDatabaseSpringManager.removeUser(rmc.getAssociatedUser());
            assert rmcDatabaseSpringManager.checkRmc(rmc.getRmcId());
            authDatabaseSpringManager.removeUser(user1.getEmail());
            assert rmcDatabaseSpringManager.checkRmc(rmc2.getRmcId());
            authDatabaseSpringManager.addUser(adminUser);

            //configurationDatabaseManager.getValue(ConfigurationEnum.DEFAULT_CONFIGURATION_1.getKey());
            ConfigurationManager.getValue(ConfigurationEnum.DEFAULT_CONFIGURATION_3);

        };
    }

}