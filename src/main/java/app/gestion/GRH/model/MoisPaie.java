// model/MoisPaie.java
package app.gestion.GRH.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.YearMonth;

@Document("moisPaie")
@Data @NoArgsConstructor @AllArgsConstructor
@CompoundIndex(name = "uniq_periode", def = "{'periode': 1}", unique = true)
public class MoisPaie {
    @Id
    private String id;
    private YearMonth periode; // 2025-08
}
