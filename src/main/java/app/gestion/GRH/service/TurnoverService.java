package app.gestion.GRH.service;

import app.gestion.GRH.dto.TurnoverFeatureDto;
import app.gestion.GRH.dto.TurnoverPredDto;
import app.gestion.GRH.model.EmployerSociete;
import app.gestion.GRH.model.TurnoverRisk;
import app.gestion.GRH.repository.EmployerSocieteRepository;
import app.gestion.GRH.repository.TurnoverRiskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service @RequiredArgsConstructor
public class TurnoverService {
    private final EmployerSocieteRepository empRepo;
    private final TurnoverFeatureBuilder featBuilder;
    private final TurnoverMlClient mlClient;
    private final TurnoverRiskRepository riskRepo;

   /* public List<TurnoverRisk> refresh(String idSociete){
        Date now = new Date();
        var emps = empRepo.findByIdSociete(idSociete);
        var feats = emps.stream().map(e -> featBuilder.build(e.getId(), now)).toList();
        var preds = mlClient.predict(feats);

        List<TurnoverRisk> out = new ArrayList<>();
        for (var p: preds){
            var r = Optional.ofNullable(riskRepo.findByIdEmployerSociete(p.getIdEmployerSociete()))
                    .orElse(new TurnoverRisk());
            r.setIdEmployerSociete(p.getIdEmployerSociete());
            r.setRiskScore(p.getRisk_score());
            r.setRiskLevel(p.getRisk_level());
            r.setComputedAt(now);
            out.add(riskRepo.save(r));
        }
        return out;
    }

    public List<TurnoverRisk> top(String idSociete, int n){
        var ids = empRepo.findByIdSociete(idSociete).stream().map(EmployerSociete::getId).toList();
        var list = riskRepo.findByIdEmployerSocieteInOrderByRiskScoreDesc(ids);
        return list.size()>n ? list.subList(0,n) : list;
    }*/
}
