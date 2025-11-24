package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.client.config.SwaggerTaskServiceProperties;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.client.config.SwaggerUserServiceProperties;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({SwaggerUserServiceProperties.class, SwaggerTaskServiceProperties.class})
public class TtOauth2AuthorizationServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TtOauth2AuthorizationServerApplication.class, args);
	}

}
