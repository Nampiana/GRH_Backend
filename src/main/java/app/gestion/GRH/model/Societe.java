package app.gestion.GRH.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "societe")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Societe {
    @Id
    private String id;
    private String nom_societe;
    private String logo;
    private String siege;
    private String adresse;
    private String telephone;
    private String numero_fax;
    private String numero_cnaps;
    private String numero_banque;
    private String nom_banque;
    private String adresse_banque;
    private int cp_banque;
    private String ville_banque;
}
