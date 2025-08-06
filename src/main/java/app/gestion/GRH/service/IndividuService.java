package app.gestion.GRH.service;

import app.gestion.GRH.model.Individu;
import app.gestion.GRH.model.Poste;
import app.gestion.GRH.repository.IndividuRepository;
import app.gestion.GRH.repository.PosteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IndividuService {
    private final IndividuRepository individuRepository;

    public List<Individu> getAll(){
        return individuRepository.findAll();
    }

    public Individu create(Individu individu){
        return individuRepository.save(individu);
    }

    public Optional<Individu> findById(String id){
        return individuRepository.findById(id);
    }

    public void delete(String id){
        individuRepository.deleteById(id);
    }

    public Optional<Individu> update(String id, Individu newIndividu) {
        return individuRepository.findById(id).map(i -> {
            i.setNom(newIndividu.getNom());
            i.setPrenom(newIndividu.getPrenom());
            i.setAdresse(newIndividu.getAdresse());
            i.setEmail(newIndividu.getEmail());
            i.setPassword(newIndividu.getPassword());
            i.setTelephone(newIndividu.getTelephone());
            i.setDateNaissance(newIndividu.getDateNaissance());
            return individuRepository.save(i);
        });
    }

}
