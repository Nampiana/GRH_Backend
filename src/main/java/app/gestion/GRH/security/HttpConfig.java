// @Configuration pour WebClient
package app.gestion.GRH.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class HttpConfig {

    @Bean(name = "mlWebClient")
    @Primary // <- au cas où un autre WebClient est défini ailleurs
    public WebClient mlWebClient(
            @Value("${turnover.ml.base-url:http://localhost:8008}") String baseUrl
    ) {
        if (baseUrl.endsWith("/")) baseUrl = baseUrl.substring(0, baseUrl.length()-1);
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}

