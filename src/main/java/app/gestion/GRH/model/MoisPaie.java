// model/MoisPaie.java
package app.gestion.GRH.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.YearMonth;
import java.util.Date;

@Document("moisPaie")
@Data @NoArgsConstructor @AllArgsConstructor
@CompoundIndex(name = "uniq_societe_periode", def = "{'idSociete': 1, 'periode': 1}", unique = true)
public class MoisPaie {
    @Id
    private String id;

    private String idSociete;     // ⇐ NEW : cycle par société
    private YearMonth periode;    // ex: 2025-08

    private String statut;        // "OPEN" | "CLOSED"  ⇐ NEW
    private Date dateOuverture;   // ⇐ NEW
    private Date dateCloture;     // ⇐ NEW
}
