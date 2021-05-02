package pl.bartoszbulaj.moonrock;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MoonrockApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoonrockApplication.class, args);
	}

}
