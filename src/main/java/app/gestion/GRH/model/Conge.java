package app.gestion.GRH.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "conge")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Conge {
    @Id
    private String id;
    private String idEmployerSociete;
    private Date dateDebut;
    private Date dateFin;
    private String motif;
    // 1 = en attente
    // 2 = Valider
    //3 = non valider
    private Integer statut;
}
