package MakeUs.Moira;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class MoiraApplication {

	public static final String APPLICATION_LOCATIONS = "spring.config.location="
			+ "classpath:application.yml,"
			+ "/home/ec2-user/app/config/real-application.yml";

	public static void main(String[] args) {
		try{
			new SpringApplicationBuilder(MoiraApplication.class)
					.properties(APPLICATION_LOCATIONS)
					.run(args);
		} catch (ConfigDataResourceNotFoundException e){
			SpringApplication.run(MoiraApplication.class, args);
		}
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}
