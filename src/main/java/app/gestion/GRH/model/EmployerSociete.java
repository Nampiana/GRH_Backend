package app.gestion.GRH.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "employerSociete")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployerSociete {
    @Id
    private String id;
    private String idUtilisateur;
    private String idSociete;
    private String idPoste;
    private String idCategorie;
    private Date dateEmbauche;
    private Date dateDebauche;
}
