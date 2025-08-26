// model/PaieMois.java
package app.gestion.GRH.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("paieMois")
@Data @NoArgsConstructor @AllArgsConstructor
public class PaieMois {
    @Id
    private String id;

    private String idEmployer;
    private String moisPaieId;     // réf -> MoisPaie.id (au lieu d’un string libre)
    private String idRubriqueCat;  // réf -> RubriqueCategorie.id
    private Double valeur;         // Montant en MGA (Ariary)
    private String note;           // optionnel (ex: "HS 12h x taux 1.3")
}
