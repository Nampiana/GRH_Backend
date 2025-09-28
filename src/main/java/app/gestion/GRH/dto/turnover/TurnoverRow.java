package app.gestion.GRH.dto.turnover;

import lombok.Data;
import java.util.List;

@Data
public class TurnoverRow {
    private String employeeId;
    private String employeeName;
    private String departement;
    private String fonction;
    private String contrat_type;
    private String site;
    private String categorie_poste;
    private Double salaire_base;
    private Integer age;
    private Integer anciennete_mois;
    private Integer nb_absences_6m;
    private Integer nb_absences_12m;
    private Double  jours_absences_6m;
    private Integer nb_retards_3m;
    private Integer nb_retards_6m;
    private Double  moy_retard_minutes_3m;
    private Integer nb_sanctions_12m;
    private Integer nb_sanctions_graves_12m;
    private String  type_sanction_top1;

    private Double score;
    private List<String> reasons;
}
