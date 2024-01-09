package com.cbh.bankservice.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;


@Configuration
@Slf4j
public class WebclientConfig {

	 private String token;

	    /**
	     * Setter method for setting the OAuth token.
	     * 
	     * This method is used to set the OAuth token that will be included in the
	     * headers of outgoing requests made by the WebClient.
	     * 
	     * @param token The OAuth token to be set
	     */
	    public void setToken(String token) {
	        this.token = token;
	    }

	    /**
	     * Creates and returns an ObjectMapper bean.
	     * 
	     * This ObjectMapper bean is used for JSON serialization and deserialization.
	     * 
	     * @return ObjectMapper instance
	     */
	    @Bean
	    ObjectMapper objectMapper() {
	        return new ObjectMapper();
	    }

	    /**
	     * Creates a WebClient builder for making reactive HTTP requests.
	     * 
	     * @return WebClient.Builder instance
	     */
	    @Bean
	    WebClient.Builder webClientBuilder() {
	        return WebClient.builder()
	                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
	                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	                .filter(oauthTokenFilter());
	    }

	    /**
	     * Creates a WebClient for making reactive HTTP requests.
	     * 
	     * @return WebClient instance
	     */
	    @Bean
	    WebClient webclient() {
	        return webClientBuilder().build();
	    }

	    /**
	     * Creates an ExchangeFilterFunction for adding OAuth token to the request
	     * headers.
	     * 
	     * This function is used as a filter in the WebClient to include the OAuth token
	     * in the headers of outgoing requests.
	     * 
	     * @return ExchangeFilterFunction instance for processing requests
	     */
	    private ExchangeFilterFunction oauthTokenFilter() {
	        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
	            // Add OAuth token to the request headers
	            ClientRequest authorizedRequest = ClientRequest.from(clientRequest)
	                    .headers(headers -> headers.setBearerAuth(token)).build();
	            return Mono.just(authorizedRequest);
	        });
	    }

	    /**
	     * Performs a GET request to the specified URL and retrieves the response as the
	     * given response type.
	     * 
	     * @param url          URL to send the GET request to
	     * @param responseType Class representing the type of the response
	     * @param <T>          Type of the response
	     * @return Response of the specified type
	     */
	    public <T> T get(String url, Class<T> responseType) {
	        return webclient().get().uri(url)
	                .retrieve()
	                .bodyToMono(responseType)
	                .onErrorResume(throwable -> handleErrorResponse(throwable, responseType))
	                .block();
	    }
	    
	    private <T> Mono<? extends T> handleErrorResponse(Throwable throwable, Class<T> responseType) {
	        if (throwable instanceof WebClientResponseException) {
	            WebClientResponseException responseException = (WebClientResponseException) throwable;
	            HttpStatus statusCode = (HttpStatus) responseException.getStatusCode();

	            if (statusCode.is4xxClientError() && statusCode != HttpStatus.NOT_FOUND) {
	                log.warn("Client error: {} {}", statusCode, responseException.getHeaders());
	                return Mono.error(new RuntimeException("Client error: " + statusCode.value()));
	            } else if (statusCode == HttpStatus.NOT_FOUND) {
	                log.warn("404 Not Found error occurred. Returning Mono.empty()");
	                return Mono.empty();  // Handle 404 by returning an empty Mono or another default value
	            }
	        }

	        log.error("Unhandled error: {}", throwable.getMessage());
	        return Mono.error(throwable);
	    }

	    /**
	     * Performs a GET request to the specified URL and retrieves the response as a
	     * list of the given response type.
	     * 
	     * @param url          URL to send the GET request to
	     * @param responseType TypeReference representing the type of the response list
	     * @param <T>          Type of the response
	     * @return List of responses of the specified type
	     */
	    public <T> List<T> getList(String url, TypeReference<List<T>> responseType) {
	        String jsonResponse = webclient().get().uri(url).retrieve().bodyToMono(String.class).block();
	        return parseJsonResponse(jsonResponse, responseType);
	    }

	    /**
	     * Performs a POST request to the specified URL with the given request body and
	     * retrieves the response as the given response type.
	     * 
	     * @param url          URL to send the POST request to
	     * @param requestBody  Request body to include in the POST request
	     * @param responseType Class representing the type of the response
	     * @param <T>          Type of the response
	     * @return Response of the specified type
	     */
	    public <T> T post(String url, Object requestBody, Class<T> responseType) {
	        return webclient().post().uri(url).body(BodyInserters.fromValue(requestBody)).retrieve()
	                .bodyToMono(responseType).block();
	    }

	    /**
	     * Parses the JSON response into a list of the given response type.
	     * 
	     * @param jsonResponse JSON response as a string
	     * @param responseType TypeReference representing the type of the response list
	     * @param <T>          Type of the response
	     * @return List of responses of the specified type
	     */
	    private <T> List<T> parseJsonResponse(String jsonResponse, TypeReference<List<T>> responseType) {
	        List<T> responseList = new ArrayList<>();
	        try {
	            JsonNode jsonNode = objectMapper().readTree(jsonResponse).get("value");
	            responseList = objectMapper().convertValue(jsonNode, responseType);
	        } catch (JsonProcessingException e) {
	            log.error("Error mapping JSON response to List: {}", e.getMessage());
	        }
	        return responseList;
	    }
}

