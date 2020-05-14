package it.richkmeli.rms.data;


import it.richkmeli.jframework.util.RandomStringGenerator;
import it.richkmeli.rms.data.entity.device.model.Device;
import it.richkmeli.rms.data.entity.device.DeviceDatabaseSpringManager;
import it.richkmeli.rms.data.entity.rmc.model.Rmc;
import it.richkmeli.rms.data.entity.rmc.RmcDatabaseSpringManager;
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
    CommandLineRunner initDatabase(/*EntityManagerFactory emf,*/ AuthDatabaseSpringManager authDatabaseSpringManager, DeviceDatabaseSpringManager deviceDatabaseSpringManager, RmcDatabaseSpringManager rmcDatabaseSpringManager) {
        return args -> {
            authDatabaseSpringManager.addUser(new User(
                    RandomStringGenerator.generateAlphanumericString(4) + "@example.com", "00000000", false));

            if (!authDatabaseSpringManager.isUserPresent("richk@i.it")) {
                authDatabaseSpringManager.addUser(new User("richk@i.it", "00000000", true));
            }

            authDatabaseSpringManager.addUser(new User("richk2@i.it", "00000000", false));

            deviceDatabaseSpringManager.addDevice(new Device(RandomStringGenerator.generateAlphanumericString(4), "43.34.43.34", "40", "20-10-18", "ckeroivervioeon", "richk@i.it", "", ""));
            deviceDatabaseSpringManager.addDevice(new Device(RandomStringGenerator.generateAlphanumericString(4), "43.34.43.34", "40", "20-10-18", "ckeroivervioeon", "richk2@i.it", "", ""));

            Rmc rmc = new Rmc("richk@i.it", "test_rmc_ID");
            Rmc rmc2 = new Rmc("richk2@i.it", "test_rmc_ID" + RandomStringGenerator.generateAlphanumericString(4));

            rmcDatabaseSpringManager.addRMC(rmc);
            rmcDatabaseSpringManager.addRMC(rmc2);
//            EntityManager em = emf.createEntityManager();
//            em.getTransaction().begin();
//            em.persist(rmc2);
//            em.getTransaction().commit();
//            em.close();

            authDatabaseSpringManager.removeUser("richk2@i.it");


        };
    }

}