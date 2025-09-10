package app.gestion.GRH.dto;
import lombok.Data;

@Data
public class TurnoverPredDto {
    private String idEmployerSociete;
    private Double risk_score;
    private String risk_level;
}
