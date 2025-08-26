// model/RubriqueCategorie.java
package app.gestion.GRH.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("rubriqueCategorie")
@Data @NoArgsConstructor @AllArgsConstructor
public class RubriqueCategorie {
    @Id
    private String id;
    private String idCategorie;
    private String idRubriquePaie; // lie la rubrique à une catégorie (employés de cette cat auront cette rubrique)
}
