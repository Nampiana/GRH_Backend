package app.gestion.GRH.controller;

import app.gestion.GRH.model.TurnoverRisk;
import app.gestion.GRH.service.TurnoverService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/turnover")
@RequiredArgsConstructor
public class TurnoverController {
    private final TurnoverService svc;

   /* @PostMapping("/refresh/{idSociete}")
    public List<TurnoverRisk> refresh(@PathVariable String idSociete){
        return svc.refresh(idSociete);
    }

    @GetMapping("/top/{idSociete}")
    public List<TurnoverRisk> top(@PathVariable String idSociete, @RequestParam(defaultValue="10") int n){
        return svc.top(idSociete, n);
    }*/
}
