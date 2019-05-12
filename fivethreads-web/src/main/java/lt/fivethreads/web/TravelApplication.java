package lt.fivethreads.web;

import lt.fivethreads.services.UserCreationService;
import lt.fivethreads.services.SimpleUserCreationServiceImplementation;
import lt.fivethreads.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;

@SpringBootApplication
@ComponentScan({
        "lt.fivethreads"
})
@EntityScan({
        "lt.fivethreads"
})
@EnableJpaRepositories("lt.fivethreads")
@EnableAspectJAutoProxy
@EnableScheduling

public class TravelApplication implements CommandLineRunner {

    @Resource
    StorageService storageService;

    public static void main(String[] args) {
        SpringApplication.run(TravelApplication.class, args);
    }

    @Override
    public void run(String... arg) throws Exception {
        storageService.deleteAll();
        storageService.init();
    }

    @Bean
    public UserCreationService userCreationService() {
        return new SimpleUserCreationServiceImplementation();
    }
}