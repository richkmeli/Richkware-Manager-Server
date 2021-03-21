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

            // add login entry
            User adminUser2 = new User("richk3@i.it", "00000000", true);
            if (!authDatabaseSpringManager.isUserPresent(adminUser2.getEmail())) {
                authDatabaseSpringManager.addUser(adminUser2);
            }


            // test
            User user = new User(RandomStringGenerator.generateAlphanumericString(4) + "@example.com", "00000000", false);
            User user1 = new User("richk2@i.it", "00000000", false);
            authDatabaseSpringManager.addUser(user);
            authDatabaseSpringManager.removeUser(user.getEmail());
            if(!authDatabaseSpringManager.isUserPresent(user1.getEmail())) {
                authDatabaseSpringManager.addUser(user1);
            }
            // user1 will be deleted later in RMC test phase

            User u1 = authDatabaseSpringManager.findUserByEmail("richk@i.it");
            User u2 = authDatabaseSpringManager.findUserByEmail("richk2@i.it");
            Device device = new Device(RandomStringGenerator.generateAlphanumericString(4), "43.34.43.34", "40", "20-10-18", "ckeroivervioeon", u1, "", "", "iid", null, null);
            Device device1 = new Device(RandomStringGenerator.generateAlphanumericString(4), "43.34.43.34", "40", "20-10-18", "ckeroivervioeon", u2, "", "", "iid", null, null);
            deviceDatabaseSpringManager.addDevice(device);
            deviceDatabaseSpringManager.addDevice(device1);
            deviceDatabaseSpringManager.removeDevice(device.getName());
            deviceDatabaseSpringManager.removeDevice(device1.getName());

            Rmc rmc = new Rmc(u1, "test_rmc_ID");
            Rmc rmc2 = new Rmc(u2, "test_rmc_ID" + RandomStringGenerator.generateAlphanumericString(4));

            rmcDatabaseSpringManager.addRMC(rmc);
            rmcDatabaseSpringManager.addRMC(rmc2);

            authDatabaseSpringManager.removeUser(rmc.getAssociatedUser().getEmail());
            assert rmcDatabaseSpringManager.checkRmc(rmc.getRmcId());
            authDatabaseSpringManager.removeUser(user1.getEmail());
            assert rmcDatabaseSpringManager.checkRmc(rmc2.getRmcId());
            authDatabaseSpringManager.addUser(adminUser);

            //configurationDatabaseManager.getValue(ConfigurationEnum.DEFAULT_CONFIGURATION_1.getKey());
            ConfigurationManager.getValue(ConfigurationEnum.DEFAULT_CONFIGURATION_3);

        };
    }

}