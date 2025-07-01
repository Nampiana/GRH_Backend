package app.gestion.GRH.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "societe")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Societe {
    @Id
    private String id;
    private String nom_societe;
}
