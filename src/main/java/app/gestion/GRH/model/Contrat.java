package app.gestion.GRH.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "contrat")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contrat {
    @Id
    private String id;
    private String idEmployerSociete;
    private String typeContrat; // CDI, CDD; STAGE; periode d'essai
    private Date dateDebut;
    private Date dateFin;
    private Double salairedebase;
    private String fichierContrat;
    private String statu; //en cours terminer
}
