package app.gestion.GRH.service;

import app.gestion.GRH.model.Departement;
import app.gestion.GRH.model.Services;
import app.gestion.GRH.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceService {
    private final ServiceRepository serviceRepository;

    public List<Services> getAll(){
        return serviceRepository.findAll();
    }

    public Services create(Services services){
        return serviceRepository.save(services);
    }

    public Optional<Services> findById(String id){
        return serviceRepository.findById(id);
    }

    public void delete(String id){
        serviceRepository.deleteById(id);
    }

    public Optional<Services> update(String id, Services newServices){
        return serviceRepository.findById(id).map(s -> {
            s.setNomService(newServices.getNomService());
            s.setIdDepartement(newServices.getIdDepartement());
            s.setIdSociete(newServices.getIdSociete());
            return serviceRepository.save(s);
        });
    }
}
