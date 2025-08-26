package app.gestion.GRH.controller;

import app.gestion.GRH.service.SanctionSseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/sanction")
@RequiredArgsConstructor
public class SanctionSseController {
    private final SanctionSseService sanctionSseService;

    @GetMapping(path = "/stream/{employerId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@PathVariable String employerId) {
        return sanctionSseService.subscribe(employerId);
    }
}
