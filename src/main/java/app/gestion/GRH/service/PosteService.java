package app.gestion.GRH.service;

import app.gestion.GRH.model.Poste;
import app.gestion.GRH.model.Services;
import app.gestion.GRH.repository.PosteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PosteService {
    private final PosteRepository posteRepository;

    public List<Poste> getAll(){
        return posteRepository.findAll();
    }

    public Poste create(Poste poste){
        return posteRepository.save(poste);
    }

    public Optional<Poste> findById(String id){
        return posteRepository.findById(id);
    }

    public void delete(String id){
        posteRepository.deleteById(id);
    }

    public Optional<Poste> update(String id, Poste newPoste){
        return posteRepository.findById(id).map(p -> {
            p.setNomPoste(newPoste.getNomPoste());
            p.setIdService(newPoste.getIdService());
            return posteRepository.save(p);
        });
    }
}
