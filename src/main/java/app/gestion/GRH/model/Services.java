package app.gestion.GRH.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "service")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Services {
    private String id;
    private String nomService;
    private String idDepartement;
    private String idSociete;
}
