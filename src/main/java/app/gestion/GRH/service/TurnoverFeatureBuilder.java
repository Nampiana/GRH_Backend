package app.gestion.GRH.service;

import app.gestion.GRH.dto.TurnoverFeatureDto;
import app.gestion.GRH.model.*;
import app.gestion.GRH.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service @RequiredArgsConstructor
public class TurnoverFeatureBuilder {
    private final EmployerSocieteRepository empRepo;
    private final PointageRepository pointRepo;
    private final SanctionRepository sancRepo;
    private final CongeRepository congeRepo;

    private Date parseDate(String s){
        if (s==null) return null;
        List<String> pats = List.of("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'","yyyy-MM-dd HH:mm:ss","yyyy-MM-dd");
        for (String p: pats){
            try { return new SimpleDateFormat(p).parse(s); } catch(Exception ignore){}
        }
        return null;
    }

    public TurnoverFeatureDto build(String idEmployerSociete, Date ref){
        TurnoverFeatureDto f = new TurnoverFeatureDto();
        f.setIdEmployerSociete(idEmployerSociete);

        var e = empRepo.findById(idEmployerSociete).orElse(null);
        if (e!=null){
            if (e.getDateEmbauche()!=null){
                long d = (ref.getTime()-e.getDateEmbauche().getTime())/(1000L*3600L*24L);
                f.setAnciennete_j((double)d);
            }
            f.setSalaireBase(e.getSalaireBase());
            f.setIdCategorie(e.getIdCategorie());
            f.setIdPoste(e.getIdPoste());
            f.setIdService(e.getIdService());
        }

        Calendar c90 = Calendar.getInstance(); c90.setTime(ref); c90.add(Calendar.DAY_OF_YEAR,-90);
        Calendar c365= Calendar.getInstance(); c365.setTime(ref); c365.add(Calendar.DAY_OF_YEAR,-365);

        // POINTAGE 90j (dates en String)
        var pts = pointRepo.findByIdEmployerSociete(idEmployerSociete);
        Set<String> days=new HashSet<>(), lateDays=new HashSet<>();
        for (var p: pts){
            Date d = parseDate(p.getDateArriver());
            if (d==null || d.before(c90.getTime()) || !d.before(ref)) continue;
            Calendar dc = Calendar.getInstance(); dc.setTime(d);
            String key = dc.get(Calendar.YEAR)+"-"+dc.get(Calendar.DAY_OF_YEAR);
            days.add(key);
            int minutes = dc.get(Calendar.HOUR_OF_DAY)*60 + dc.get(Calendar.MINUTE);
            if (minutes > (8*60+5)) lateDays.add(key);
        }
        if (!days.isEmpty()){
            f.setRetard_jours_90((double)lateDays.size());
            f.setRetard_pct_jours_90((double)lateDays.size()/days.size());
        }

        // SANCTIONS 12m (idEmployer == EmployerSociete.id)
        var sansAll = sancRepo.findByIdEmployer(idEmployerSociete);
        Date lower = c365.getTime();
        var sans = sansAll.stream()
                .filter(s -> s.getDateSanction()!=null && !s.getDateSanction().before(lower) && s.getDateSanction().before(ref))
                .toList();
        f.setSanctions_12m((double) sans.size());
        if (!sans.isEmpty()){
            Date last = sans.stream().map(Sanction::getDateSanction).max(Date::compareTo).get();
            long dd = (ref.getTime()-last.getTime())/(1000L*3600L*24L);
            f.setLast_sanction_j((double)dd);
        }

        // CONGES 90j
        var csAll = congeRepo.findByIdEmployerSociete(idEmployerSociete);
        var cs = csAll.stream()
                .filter(c -> c.getDateDebut()!=null && c.getDateFin()!=null &&
                        c.getDateFin().after(c90.getTime()) && c.getDateDebut().before(ref))
                .toList();
        double j = cs.stream().mapToDouble(c -> c.getDuree()!=null? c.getDuree():0d).sum();
        f.setJours_conge_90(j);

        return f;
    }
}
