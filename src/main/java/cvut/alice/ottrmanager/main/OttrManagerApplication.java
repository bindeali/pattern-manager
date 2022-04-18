package cvut.alice.ottrmanager.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class OttrManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OttrManagerApplication.class, args);
    }

    @PostConstruct
    private void setup() {

    }
}
