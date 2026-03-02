package konradowc.shopping.backend_api.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class SseService {
    // mapping: {userId : emitter}   
    private final HashMap<Long, SseEmitter> clients = new HashMap<>();
    private final Object clientsListLock = new Object(); // to prevent race conditions (SBoot is multi-threaded)

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    public SseEmitter createNewSseConnection(Long userId) {
        SseEmitter emitter = new SseEmitter(Long.valueOf(1000 * 60 * 60 * 12)); // timeout 12 hours
        addSseConnection(userId, emitter);

        emitter.onCompletion(() -> removeSseConnection(userId));
        emitter.onTimeout(() -> removeSseConnection(userId));
        emitter.onError((e) -> removeSseConnection(userId));

        return emitter;
    }
    
    ///

    public void propogateUpdates(List<Long> userIds, Long listIdThatsChanging) {
        for(Long userId : userIds) {
            emitUpdate(clients.get(userId), "changedList", listIdThatsChanging);
        }
    }

    public void propogateDelete(List<Long> userIds, Long listIdThatsDeleting) {
        for(Long userId : userIds) {
            emitUpdate(clients.get(userId), "deletedList", listIdThatsDeleting);
        }
    }

    private void addSseConnection(Long userId, SseEmitter emitter) {
        synchronized(clientsListLock) {
            if(clients.containsKey(userId)) System.out.println("Note: replacing existing emitter");
            
            clients.put(userId, emitter);
        }
    }

    private void removeSseConnection(Long userId) {
       synchronized(clientsListLock) {
            clients.remove(userId);
       }
    }

    private void emitUpdate(SseEmitter emitter, String name, Object data) { // data can be any type
        executor.submit(() -> {
            try {
                emitter.send(SseEmitter.event()
                    .name(name)
                    .data(data)
                );
            }
            catch (IOException e) { // emitter error
                System.out.println("Emitter error (!!)");
            }
        });
    }
}
