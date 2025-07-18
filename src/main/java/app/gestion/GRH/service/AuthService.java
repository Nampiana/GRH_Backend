package app.gestion.GRH.service;

import app.gestion.GRH.dto.LoginResponse;
import app.gestion.GRH.model.Individu;
import app.gestion.GRH.model.JwtUtil;
import app.gestion.GRH.model.Utilisateur;
import app.gestion.GRH.repository.IndividuRepository;
import app.gestion.GRH.repository.UtilisateurRepository;
import app.gestion.GRH.security.TokenBlacklist;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final IndividuRepository individuRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TokenBlacklist tokenBlacklist;

    public LoginResponse login(Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        Individu individu = individuRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email invalide"));

        if (!passwordEncoder.matches(password, individu.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        Utilisateur utilisateur = utilisateurRepository.findAll().stream()
                .filter(u -> u.getIdIndividu().equals(individu.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        String token = jwtUtil.generateToken(email);

        return new LoginResponse(token, individu.getNom(), individu.getPrenom(), individu.getAdresse(), individu.getEmail(), individu.getTelephone(), utilisateur.getRoles(), utilisateur.getIdSociete(), utilisateur.getEtat(), utilisateur.getId());
    }

    public String logout(String token) {
        tokenBlacklist.add(token);
        return "Déconnexion réussie";
    }

    public Map<String, Object> checkToken(String token) {
        String email = jwtUtil.extractUsername(token);

        Individu individu = individuRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Aucun individu trouvé avec cet email."));

        Utilisateur utilisateur = utilisateurRepository.findAll().stream()
                .filter(u -> u.getIdIndividu().equals(individu.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Aucun utilisateur trouvé pour cet individu."));

        // Créer la structure de réponse
        Map<String, Object> data = new HashMap<>();
        data.put("id", utilisateur.getId());
        data.put("idIndividu", utilisateur.getIdIndividu());
        data.put("idSociete", utilisateur.getIdSociete());
        data.put("etat", utilisateur.getEtat());
        data.put("roles", utilisateur.getRoles());

        // Ajouter info individu
        Map<String, Object> individuData = new HashMap<>();
        individuData.put("nom", individu.getNom());
        individuData.put("prenom", individu.getPrenom());
        individuData.put("adresse", individu.getAdresse());
        individuData.put("email", individu.getEmail());
        individuData.put("telephone", individu.getTelephone());
        data.put("individu", individuData);

        return data;
    }
}
