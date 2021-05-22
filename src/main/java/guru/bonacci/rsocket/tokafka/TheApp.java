package guru.bonacci.rsocket.tokafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TheApp {

	public static final String TOPIC = "foo4";
	
	public static void main(String[] args) {
		SpringApplication.run(TheApp.class, args);
	}
}
