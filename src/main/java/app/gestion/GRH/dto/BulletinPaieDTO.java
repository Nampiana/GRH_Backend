package app.gestion.GRH.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulletinPaieDTO {
    private String idEmployer;
    private String moisPaieId;
    private List<LignePaieDTO> lignes;
    private Double totalPlus;
    private Double totalMoins;
    private Double brut;
    private Double netAPayer;
}
