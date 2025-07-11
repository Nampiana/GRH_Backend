package app.gestion.GRH.dto;

import app.gestion.GRH.model.EmployerSociete;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployerSocieteRequestDTO {
    private EmployerSociete employerSociete;

    private String nom;
    private String prenom;
    private String adresse;
    private String email;
    private String password;
    private String telephone;
    private String idSociete;
    private Integer role;
}
