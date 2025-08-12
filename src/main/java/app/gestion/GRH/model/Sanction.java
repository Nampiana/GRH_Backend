package app.gestion.GRH.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "sanction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sanction {
    private String id;
    private String idEmployer;
    private String typeSanction; //positive=1 , negative=2
    private String motif;
    private Date dateSanction;
}
