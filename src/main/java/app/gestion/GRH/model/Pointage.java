package app.gestion.GRH.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pointage")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pointage {
    @Id
    private String id;

    private String idEmployerSociete;
    private String dateArriver;
    private String dateDepart;
}
