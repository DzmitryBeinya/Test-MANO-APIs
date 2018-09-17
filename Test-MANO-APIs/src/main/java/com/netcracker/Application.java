package com.netcracker;

import com.netcracker.config.EmbeddedTomcatConfiguration;
import com.netcracker.config.FiltersConfiguration;
import com.netcracker.config.PortConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = { "com.netcracker.rest" })
@Import({ EmbeddedTomcatConfiguration.class, PortConfiguration.class, FiltersConfiguration.class })
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
