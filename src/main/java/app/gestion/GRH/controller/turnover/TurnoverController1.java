package app.gestion.GRH.controller.turnover;

import app.gestion.GRH.dto.turnover.PredictFromMongoResponse;
import app.gestion.GRH.service.turnover.TurnoverService1;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/turnoverss")
@RequiredArgsConstructor
public class TurnoverController1 {
    private final TurnoverService1 turnoverService;

    /**
     * GET /api/turnover/predict?idSociete=68cd15b...&threshold=0.6
     */
    @GetMapping("/predict")
    public PredictFromMongoResponse predict(
            @RequestParam(required = false) String idSociete,
            @RequestParam(required = false) Double threshold
    ) {
        return turnoverService.predictFromMongo(idSociete, threshold);
    }
}
