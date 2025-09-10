package app.gestion.GRH.service;

import app.gestion.GRH.dto.TurnoverFeatureDto;
import app.gestion.GRH.dto.TurnoverPredDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service @RequiredArgsConstructor
public class TurnoverMlClient {
    private final WebClient.Builder builder;

    @Value("${turnover.ml.base-url}") private String baseUrl;
    @Value("${turnover.ml.api-key}")  private String apiKey;

    private WebClient web(){
        return builder.baseUrl(baseUrl).defaultHeader("X-API-KEY", apiKey).build();
    }

    public List<TurnoverPredDto> predict(List<TurnoverFeatureDto> items){
        var body = Map.of("items", items);
        var type = new ParameterizedTypeReference<List<TurnoverPredDto>>(){};
        return web().post().uri("/predict").bodyValue(body).retrieve().bodyToMono(type).block();
    }
}
