package app.gestion.GRH.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document("turnover_risk")
@Data @NoArgsConstructor @AllArgsConstructor
public class TurnoverRisk {
    @Id private String id;
    private String idEmployerSociete;
    private Double riskScore;
    private String riskLevel;
    private Date computedAt;
}
