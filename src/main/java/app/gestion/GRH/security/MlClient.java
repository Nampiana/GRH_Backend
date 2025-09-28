package app.gestion.GRH.security;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
@RequiredArgsConstructor
public class MlClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${ml.baseUrl:http://localhost:8008}")
    private String mlBaseUrl;

    /** Appelle /predict (batch) avec { rows: [...] } et renvoie la liste des scores */
    public List<Double> predictBatch(List<Map<String,Object>> rows) {
        String url = mlBaseUrl + "/predict";
        Map<String,Object> payload = new HashMap<>();
        payload.put("rows", rows);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String,Object>> req = new HttpEntity<>(payload, headers);
        ResponseEntity<Map> resp = restTemplate.exchange(url, HttpMethod.POST, req, Map.class);

        if (resp.getStatusCode().is2xxSuccessful() && resp.getBody()!=null) {
            Object scores = resp.getBody().get("scores");
            if (scores instanceof List<?> list) {
                List<Double> out = new ArrayList<>();
                for (Object o : list) out.add(o instanceof Number n ? n.doubleValue() : 0d);
                return out;
            }
        }
        return Collections.emptyList();
    }
}
