package app.gestion.GRH.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String nom;
    private String prenom;
    private String adresse;
    private String email;
    private String telephone;
    private Integer roles;
    private String idSociete;
    private Integer etat;
}
