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
    private String code;           // ex: "SB", "CNAPS", "OSTIE", "IRSA", "PRIME", "AVANCE"

    private String nomRubrique;    // "Salaire de base", "CNAPS", ...
    private Integer operation;     // 1 = +, 0 = -

    private Boolean imposable;     // ⇐ NEW: true/false (SB=true, PRIME? selon config)
    private String typeRubrique;   // (optionnel) I/C/N si tu veux garder ton code historique

    private String idParametreGenereaux;   // null si manuel
    private String idSociete;
}
