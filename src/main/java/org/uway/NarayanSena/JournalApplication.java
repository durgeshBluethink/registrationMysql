package org.uway.NarayanSena;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.availability.ApplicationAvailabilityAutoConfiguration;
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration;
import org.springframework.boot.autoconfigure.context.LifecycleAutoConfiguration;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration;
import org.springframework.boot.autoconfigure.ssl.SslAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = {
		ConfigurationPropertiesAutoConfiguration.class,
		SslAutoConfiguration.class,
		LifecycleAutoConfiguration.class,
		PropertyPlaceholderAutoConfiguration.class,
		ApplicationAvailabilityAutoConfiguration.class,
		ProjectInfoAutoConfiguration.class
})
@EnableTransactionManagement
public class JournalApplication {

	public static void main(String[] args) {
		SpringApplication.run(JournalApplication.class, args);

		// After the application has started, open the home page
		//openHomePage();
	}


}
