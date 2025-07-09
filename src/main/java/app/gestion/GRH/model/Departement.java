package app.gestion.GRH.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "departement")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Departement {
    private String id;
    private String nomDepartement;
}
