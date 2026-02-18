package konradowc.shopping.backend_api.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class SseService {
    /* mapping lists: {listId: {userId: emitter}} */   
    private final HashMap<Long, HashMap<Long, SseEmitter>> clients = new HashMap<>();
    private final Object clientsListLock = new Object(); // to prevent race conditions (SBoot is multi-threaded)

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    //private long sseIdCounter = 0; // maybe in future

    public SseEmitter createNewSseConnection(Long userId, Long listId) {
        SseEmitter emitter = new SseEmitter(Long.valueOf(1000 * 60 * 60 * 12)); // timeout 12 hours
        addSseConnection(userId, listId, emitter);

        emitter.onCompletion(() -> removeSseConnection(userId, listId));
        emitter.onTimeout(() -> removeSseConnection(userId, listId));
        emitter.onError((e) -> removeSseConnection(userId, listId));

        return emitter;
    }

    public void propogateUpdates(Long listId) {
        // TO-DO: race condition-safe? should be ok (regarding emitter loop)
        HashMap<Long, SseEmitter> singleList = clients.get(listId);
        if(singleList == null) return; // not an error, just no clients subscribed

        for(SseEmitter emitter : singleList.values()) {
            String name = String.valueOf(listId);
            String data = "data";
            emitUpdate(emitter, name, data);
        }
    }

    private void addSseConnection(Long userId, Long listId, SseEmitter emitter) {
        synchronized(clientsListLock) {
            if(!clients.containsKey(listId)) {
                clients.put(listId, new HashMap<>());
            }
            HashMap<Long, SseEmitter> innerMap = clients.get(listId);
            innerMap.put(userId, emitter);
        }

        propogateUpdates(Long.valueOf(listId));
    }

    private void removeSseConnection(Long userId, Long listId) {
       synchronized(clientsListLock) {
            clients.get(listId).remove(userId);
       }
    }

    private void emitUpdate(SseEmitter emitter, String name, String data) {
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
