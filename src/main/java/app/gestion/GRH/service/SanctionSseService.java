package app.gestion.GRH.service;

import app.gestion.GRH.model.Sanction;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class SanctionSseService {
    private final Map<String, CopyOnWriteArrayList<SseEmitter>> emittersByEmployer = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String employerId) {
        SseEmitter emitter = new SseEmitter(0L); // pas d’expiration
        emittersByEmployer.computeIfAbsent(employerId, k -> new CopyOnWriteArrayList<>()).add(emitter);

        // ping initial (optionnel)
        try {
            emitter.send(SseEmitter.event().name("connected").data("ok"));
        } catch (IOException ignored) {}

        emitter.onCompletion(() -> removeEmitter(employerId, emitter));
        emitter.onTimeout(() -> removeEmitter(employerId, emitter));
        emitter.onError((ex) -> removeEmitter(employerId, emitter));

        return emitter;
    }

    public void publish(Sanction sanction) {
        if (sanction == null || sanction.getIdEmployer() == null) return;
        String employerId = sanction.getIdEmployer();
        List<SseEmitter> emitters = emittersByEmployer.getOrDefault(employerId, new CopyOnWriteArrayList<>());

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("sanction")
                        .data(sanction));
            } catch (IOException e) {
                removeEmitter(employerId, emitter);
            }
        }
    }

    private void removeEmitter(String employerId, SseEmitter emitter) {
        List<SseEmitter> list = emittersByEmployer.get(employerId);
        if (list != null) {
            list.remove(emitter);
            if (list.isEmpty()) {
                emittersByEmployer.remove(employerId);
            }
        }
    }
}
