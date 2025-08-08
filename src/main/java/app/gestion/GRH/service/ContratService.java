package app.gestion.GRH.service;

import app.gestion.GRH.model.Contrat;
import app.gestion.GRH.model.Services;
import app.gestion.GRH.repository.ContratRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContratService {
    private final ContratRepository contratRepository;

    public List<Contrat> getAll(){
        return contratRepository.findAll();
    }

    public Contrat create(Contrat contrat) {
        if ("CDI".equalsIgnoreCase(contrat.getTypeContrat())) {
            contrat.setDateFin(null);
        }
        return contratRepository.save(contrat);
    }

    public Optional<Contrat> findById(String id){
        return contratRepository.findById(id);
    }

    public void delete(String id){
        contratRepository.deleteById(id);
    }

    public Optional<Contrat> update(String id, Contrat newContrat){
        return contratRepository.findById(id).map(c -> {
            c.setIdEmployerSociete(newContrat.getIdEmployerSociete());
            c.setTypeContrat(newContrat.getTypeContrat());
            c.setDateDebut(newContrat.getDateDebut());
            c.setDateFin(newContrat.getDateFin());
            c.setSalairedebase(newContrat.getSalairedebase());
            c.setFichierContrat(newContrat.getFichierContrat());
            c.setStatu(newContrat.getStatu());
            return contratRepository.save(c);
        });
    }
}
