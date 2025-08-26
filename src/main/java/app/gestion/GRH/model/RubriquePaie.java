// model/RubriquePaie.java
package app.gestion.GRH.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("rubriquePaie")
@Data @NoArgsConstructor @AllArgsConstructor
public class RubriquePaie {
    @Id
    private String id;

    @Indexed(unique = true)
    private String code;           // ex: "SB", "CNAPS", "OSTIE", "IRSA", "HS", "PRIME", "AVANCE"

    private String nomRubrique;    // "Salaire de base", "CNAPS"...
    private String typeRubrique;   // I / C / N (facultatif, pour l’imposable, etc.)
    private Integer operation;     // 1 = +, 0 = -
    private String idParametreGenereaux;   // null si manuel (PRIME, HS, AVANCE), valeur si calculé % du SB
    private String idSociete;
}
