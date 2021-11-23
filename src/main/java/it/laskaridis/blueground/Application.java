package it.laskaridis.blueground;

import it.laskaridis.blueground.reminders.model.CountBasedDispatchBackoffStrategy;
import it.laskaridis.blueground.reminders.model.ReminderDispatchBackoffStrategy;
import it.laskaridis.blueground.units.model.ImageStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableConfigurationProperties(ImageStorageProperties.class)
@EnableScheduling
@EnableTransactionManagement
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public ReminderDispatchBackoffStrategy getReminderDispatchBackoffStrategy() {
	    // try at most 3 times to send a booking reminder:
		return new CountBasedDispatchBackoffStrategy(3);
	}

}
