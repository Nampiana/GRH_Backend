package app.gestion.GRH.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "poste")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Poste {
    private String id;
    private String nomPoste;
    private String idSociete;
}
