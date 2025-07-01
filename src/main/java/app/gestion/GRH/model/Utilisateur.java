package app.gestion.GRH.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "utilisateur")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Utilisateur {
    @Id
    private String id;

    private String idIndividu;

    private String idSociete;

    //1 compte active et 2 compte desactiver
    private Integer etat;
    //roles 3 employer et roles 2 RH // 1 admin
    private Integer roles;
}
