package app.gestion.GRH.service;

import app.gestion.GRH.model.Departement;
import app.gestion.GRH.model.MoisPaie;
import app.gestion.GRH.repository.MoisPaieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MoisPaieService {
    private final MoisPaieRepository moisPaieRepository;

    public List<MoisPaie> getAll(){
        return moisPaieRepository.findAll();
    }

    public MoisPaie create(MoisPaie moisPaie){
        return moisPaieRepository.save(moisPaie);
    }

    public Optional<MoisPaie> findById(String id){
        return moisPaieRepository.findById(id);
    }

    public void delete(String id){
        moisPaieRepository.deleteById(id);
    }

    public Optional<MoisPaie> update(String id, MoisPaie newMoisPaie){
        return moisPaieRepository.findById(id).map(m -> {
            m.setPeriode(newMoisPaie.getPeriode());
            return moisPaieRepository.save(m);
        });
    }
}
