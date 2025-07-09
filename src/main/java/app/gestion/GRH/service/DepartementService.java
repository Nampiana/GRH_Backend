package app.gestion.GRH.service;

import app.gestion.GRH.model.Departement;
import app.gestion.GRH.model.Societe;
import app.gestion.GRH.repository.DepartementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartementService {
    private final DepartementRepository departementRepository;

    public List<Departement> getAll(){
        return departementRepository.findAll();
    }

    public Departement create(Departement departement){
        return departementRepository.save(departement);
    }

    public Optional<Departement> findById(String id){
        return departementRepository.findById(id);
    }

    public void delete(String id){
        departementRepository.deleteById(id);
    }

    public Optional<Departement> update(String id, Departement newDepartement){
        return departementRepository.findById(id).map(d -> {
            d.setNomDepartement(newDepartement.getNomDepartement());
            return departementRepository.save(d);
        });
    }
}
