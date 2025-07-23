package app.gestion.GRH.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "soldeConge")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoldeConge {
    @Id
    private String id;
    private String idEmployerSociete;
    private Double solde;
}
