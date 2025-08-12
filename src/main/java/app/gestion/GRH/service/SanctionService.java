package app.gestion.GRH.service;

import app.gestion.GRH.model.Sanction;
import app.gestion.GRH.model.Services;
import app.gestion.GRH.repository.SanctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SanctionService {
    private final SanctionRepository sanctionrepository;

    public List<Sanction> getAll(){
        return sanctionrepository.findAll();
    }

    public Sanction create(Sanction sanction){
        return sanctionrepository.save(sanction);
    }

    public Optional<Sanction> findById(String id){
        return sanctionrepository.findById(id);
    }

    public void delete(String id){
        sanctionrepository.deleteById(id);
    }

    public Optional<Sanction> update(String id, Sanction newSanction){
        return sanctionrepository.findById(id).map(s -> {
            s.setIdEmployer(newSanction.getIdEmployer());
            s.setTypeSanction(newSanction.getTypeSanction());
            s.setMotif(newSanction.getMotif());
            s.setDateSanction(newSanction.getDateSanction());
            return sanctionrepository.save(s);
        });
    }


}
