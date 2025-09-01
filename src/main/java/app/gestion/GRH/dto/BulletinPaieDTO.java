package app.gestion.GRH.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulletinPaieDTO {
    private String idEmployer;
    private String moisPaieId;
    private List<LignePaieDTO> lignes;

    // Totaux existants
    private Double totalPlus;
    private Double totalMoins;
    private Double brut;
    private Double netAPayer;

    // ✅ Nouveaux totaux/indicateurs
    private Double plusImposable;     // somme des + avec typeRubrique = 'I'
    private Double plusNonImposable;  // somme des + avec typeRubrique = 'N'
    private Double cotisations;       // somme des - (CNAPS, OSTIE, IRSA, AVANCE, etc.)
    private Double brutImposable;     // = plusImposable (arrondi)
    private Double irsa;              // montant IRSA (si configurée)
}
