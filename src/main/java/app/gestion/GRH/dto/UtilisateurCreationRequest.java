package app.gestion.GRH.dto;

import lombok.Data;

@Data
public class UtilisateurCreationRequest {
    private String nom;
    private String prenom;
    private String adresse;
    private String email;
    private String password;
    private String telephone;
    private Integer roles;
    private String idSociete;
}
