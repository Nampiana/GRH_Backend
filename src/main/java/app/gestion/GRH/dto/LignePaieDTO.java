package app.gestion.GRH.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class LignePaieDTO {
    private String code;       // ex "SB", "CNAPS"
    private String libelle;    // "Salaire de base"
    private Integer operation; // 1=+, 0=-
    private Double montant;    // MGA
    private Double taux;       // % si applicable, sinon null
}
