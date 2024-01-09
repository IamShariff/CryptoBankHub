package com.cbh.apigateway.config;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;

@OpenAPIDefinition(info = @Info(title = "API Gateway", description = "This API documentation provides information about the Notification Service API, which is responsible for sending OTP and various notifications to users.", version = "1.0", contact = @Contact(name = "CryptoBankHub", email = "cryptobankhub@gmail.com", url = "https://www.xyz.com"), license = @License(name = "No License Required", url = "https://www.example.com/licenses/sharif-license")))
public class OpenApiConfig {

	@Bean
	public GroupedOpenApi api1() {
		return GroupedOpenApi.builder().group("microservice1").pathsToMatch("/user/**").build();
	}

	@Bean
	public GroupedOpenApi api2() {
		return GroupedOpenApi.builder().group("microservice2").pathsToMatch("/crypto/**").build();
	}

	@Bean
	public GroupedOpenApi api3() {
		return GroupedOpenApi.builder().group("microservice3").pathsToMatch("/bankAccount/**").build();
	}

	@Bean
	public GroupedOpenApi api4() {
		return GroupedOpenApi.builder().group("microservice4").pathsToMatch("/payment/**").build();
	}

}
