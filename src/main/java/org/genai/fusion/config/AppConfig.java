package org.genai.fusion.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // Add an interceptor to include the Authorization header
        ClientHttpRequestInterceptor interceptor = (request, body, execution) -> {
            // Get the Authorization header
            // String authorizationHeader = request.getHeader("Authorization");
            String authorizationHeader = request.getHeaders().getFirst("Authorization");
            System.out.println("Authorization:123 " + authorizationHeader);
            // request.getHeaders().set("Authorization",
            // "Basic aGFyaXNrdW1hci5wYW5ha2thbEB3aXByby5jb206TWFnaWNHZW5pZTE3MTA=");
            return execution.execute(request, body);
        };

        restTemplate.setInterceptors(Collections.singletonList(interceptor));

        return restTemplate;
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                // .setLenient()
                .create();
    }

}
