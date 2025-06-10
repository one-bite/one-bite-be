package code.rice.bowl.spaghetti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class SpaghettiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpaghettiApplication.class, args);
	}

	@PostConstruct
	public void setTimeZone() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
		System.out.println("Successfully set time zone: Asia/Seoul");
	}
}
