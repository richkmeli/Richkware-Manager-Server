package it.richkmeli.rms.data;


import it.richkmeli.jframework.util.RandomStringGenerator;
import it.richkmeli.rms.data.model.user.User;
import it.richkmeli.rms.data.repository.UserRepository;
import it.richkmeli.rms.data.repository.DeviceRepository;
import it.richkmeli.rms.data.model.device.Device;
import it.richkmeli.rms.data.repository.RmcRepository;
import it.richkmeli.rms.data.model.rmc.Rmc;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Annotating a class with the @Configuration annotation indicates
// that the class will be used by JavaConfig as a source of bean definitions.
@Configuration
class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(/*EntityManagerFactory emf,*/ UserRepository userRepository, DeviceRepository deviceRepository, RmcRepository rmcRepository) {
        return args -> {
            userRepository.save(new User(
                    RandomStringGenerator.generateAlphanumericString(4) + "@example.com", "00000000", false)).getEmail();
            userRepository.save(new User("richk@i.it", "00000000", true));

            if (!userRepository.existsById("richk2@i.it")) {
                userRepository.save(new User("richk2@i.it", "00000000", true));
            }

            deviceRepository.save(new Device(RandomStringGenerator.generateAlphanumericString(4), "43.34.43.34", "40", "20-10-18", "ckeroivervioeon", "richk@i.it", "", ""));
            deviceRepository.save(new Device(RandomStringGenerator.generateAlphanumericString(4), "43.34.43.34", "40", "20-10-18", "ckeroivervioeon", "richk2@i.it", "", ""));

            Rmc rmc = new Rmc("richk@i.it", "test_rmc_ID");
            Rmc rmc2 = new Rmc("richk2@i.it", "test_rmc_ID" + RandomStringGenerator.generateAlphanumericString(4));

            rmcRepository.save(rmc);
            rmcRepository.save(rmc2);
//            EntityManager em = emf.createEntityManager();
//            em.getTransaction().begin();
//            em.persist(rmc2);
//            em.getTransaction().commit();
//            em.close();

            userRepository.deleteById("richk2@i.it");


        };
    }

}