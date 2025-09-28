package app.gestion.GRH.service.turnover;

import app.gestion.GRH.dto.turnover.PredictFromMongoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TurnoverService1 {

    private final @Qualifier("mlWebClient") WebClient mlWebClient;

    @Value("${turnover.ml.base-url:http://localhost:8008}")
    private String mlBaseUrl; // on reconstruit l’URL absolue ici

    public PredictFromMongoResponse predictFromMongo(String idSociete, Double threshold){
        String url = UriComponentsBuilder
                .fromHttpUrl(mlBaseUrl)              // <- base absolue
                .path("/predict_from_mongo")
                .queryParamIfPresent("idSociete", idSociete == null || idSociete.isBlank() ? java.util.Optional.empty() : java.util.Optional.of(idSociete))
                .queryParamIfPresent("threshold", threshold == null ? java.util.Optional.empty() : java.util.Optional.of(threshold))
                .toUriString();

        // LOG pour vérifier l’URL finale
        System.out.println("[TurnoverService1] ML URL = " + url);

        return mlWebClient.get()
                .uri(url) // <- absolue, plus de /predict_from_mongo tout seul
                .retrieve()
                .bodyToMono(PredictFromMongoResponse.class)
                .onErrorResume(ex -> {
                    ex.printStackTrace();
                    return Mono.just(new PredictFromMongoResponse());
                })
                .block();
    }
}
