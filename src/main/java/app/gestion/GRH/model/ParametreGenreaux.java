package app.gestion.GRH.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "parametreGenereaux")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParametreGenreaux {
    @Id
    private String id;
    private String nomParametre; //exemple CNaPS
    private String idSociete;
    private Double pourcentage;
}
