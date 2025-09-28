package app.gestion.GRH.dto.turnover;

import lombok.Data;
import java.util.List;

@Data
public class PredictFromMongoResponse {
    private List<TurnoverRow> rows_at_risk;
    private List<TurnoverRow> all_rows;
    private double threshold;
}

