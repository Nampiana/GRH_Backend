package app.gestion.GRH.dto;
import lombok.Data;

@Data
public class TurnoverFeatureDto {
    private String idEmployerSociete;
    private Double anciennete_j;
    private Double salaireBase;
    private String idCategorie;
    private String idPoste;
    private String idService;
    private Double retard_jours_90;
    private Double retard_pct_jours_90;
    private Double sanctions_12m;
    private Double last_sanction_j;
    private Double jours_conge_90;
}
