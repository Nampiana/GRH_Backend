package app.gestion.GRH.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "individue")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Individu {
    @Id
    private String id;

    private String nom;
    private String prenom;
    private String adresse;
    private String email;
    private String password;
    private String telephone;
    private Date dateNaissance;
}
