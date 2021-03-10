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

	public static final String LOCAL_LOCATION = "spring.config.location="
			+ "classpath:application.yml,"
			+ "classpath:aws.yml";

	public static final String PROD_LOCATION = "spring.config.location="
			+ "classpath:application.yml,"
			+ "/home/ec2-user/app/config/real-application.yml";

	public static void main(String[] args) {
		try{
			new SpringApplicationBuilder(MoiraApplication.class)
					.properties(LOCAL_LOCATION)
					.run(args);
		} catch (ConfigDataResourceNotFoundException e){
			new SpringApplicationBuilder(MoiraApplication.class)
					.properties(PROD_LOCATION)
					.run(args);
		}
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}
