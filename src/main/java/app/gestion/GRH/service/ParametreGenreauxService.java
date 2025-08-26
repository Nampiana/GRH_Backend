package app.gestion.GRH.service;

import app.gestion.GRH.model.ParametreGenreaux;
import app.gestion.GRH.repository.ParametreGenreauxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParametreGenreauxService {
    private final ParametreGenreauxRepository parametreGenreauxRepository;

    public List<ParametreGenreaux> getAll() {
        return parametreGenreauxRepository.findAll();
    }

    public ParametreGenreaux create(ParametreGenreaux parametreGenreaux) {
        return parametreGenreauxRepository.save(parametreGenreaux);
    }

    public Optional<ParametreGenreaux> findById(String id) {
        return parametreGenreauxRepository.findById(id);
    }

    public void delete(String id) {
        parametreGenreauxRepository.deleteById(id);
    }

    public Optional<ParametreGenreaux> update(String id, ParametreGenreaux newParametre) {
        return parametreGenreauxRepository.findById(id).map(p -> {
            p.setNomParametre(newParametre.getNomParametre());
            p.setIdSociete(newParametre.getIdSociete());
            p.setPourcentage(newParametre.getPourcentage());
            return parametreGenreauxRepository.save(p);
        });
    }
}
