package MakeUs.Moira;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
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
}
