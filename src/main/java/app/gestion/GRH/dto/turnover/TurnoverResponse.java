package app.gestion.GRH.dto.turnover;

import lombok.*;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TurnoverResponse {
    private double threshold;          // 0..1
    private int total;
    private int nbAlerts;
    private List<TurnoverRow> all;     // toutes les prédictions
    private List<TurnoverRow> alerts;  // uniquement celles >= threshold
}
