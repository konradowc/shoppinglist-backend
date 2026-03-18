package konradowc.shopping.backend_api.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import konradowc.shopping.backend_api.services.SseService;

import org.springframework.stereotype.Controller;

@CrossOrigin(origins = "http://localhost:5173")
@Controller
public class SseController {

    private final SseService sseService;
    public SseController(SseService sseService) { this.sseService = sseService; }

    @GetMapping("/sse/{userId}")
    public SseEmitter initialConnect(@PathVariable("userId") Long userId) {
        // TO-DO: when authentication implemented, get userId from that instead
        SseEmitter emitter = sseService.createNewSseConnection(userId);

        return emitter;
    }
}