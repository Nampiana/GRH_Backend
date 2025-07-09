package app.gestion.GRH.service;

import app.gestion.GRH.model.Societe;
import app.gestion.GRH.repository.SocieteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SocieteService {
    private final SocieteRepository societeRepository;

    public List<Societe> getAll(){
        return societeRepository.findAll();
    }

    public Societe create(Societe societe){
        return societeRepository.save(societe);
    }

    public Optional<Societe> findById(String id){
        return societeRepository.findById(id);
    }

    public void delete(String id){
        societeRepository.deleteById(id);
    }

    public Optional<Societe> update(String id, Societe newSociete){
        return societeRepository.findById(id).map(s -> {
            s.setNomSociete(newSociete.getNomSociete());
            s.setLogo(newSociete.getLogo());
            s.setSiege(newSociete.getSiege());
            s.setAdresse(newSociete.getAdresse());
            s.setTelephone(newSociete.getTelephone());
            s.setNumero_fax(newSociete.getNumero_fax());
            s.setNumero_cnaps(newSociete.getNumero_cnaps());
            s.setNumero_banque(newSociete.getNumero_banque());
            s.setNom_banque(newSociete.getNom_banque());
            s.setAdresse_banque(newSociete.getAdresse_banque());
            s.setCp_banque(newSociete.getCp_banque());
            s.setVille_banque(newSociete.getVille_banque());
            return societeRepository.save(s);
        });
    }

    public Page<Societe> getAllPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return societeRepository.findAll(pageable);
    }

    public Page<Societe> searchSociete(String keyword, Pageable pageable) {
        return societeRepository.findByNomSocieteContainingIgnoreCase(keyword, pageable);
    }

}
