package konradowc.shopping.backend_api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import konradowc.shopping.backend_api.dtos.List.*;
import konradowc.shopping.backend_api.services.ListService;
import konradowc.shopping.backend_api.services.SseService;


@CrossOrigin(origins = "http://localhost:5173",  // TO-DO: temporary
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
@RestController
public class ListController {

    private final ListService listService;
    private final SseService sseService;
    public ListController(ListService listService, SseService sseService) { this.listService = listService; this.sseService = sseService; }

    ///
    /// 
    /// 

    @GetMapping("/list/{id}")
    ResponseEntity<ListResponse> getList(@PathVariable("id") Long id) {
        ListResponse list = listService.getList(id);
        return ResponseEntity.ok(list); // TO-DO: account for error handling in future
    }

    @PostMapping("/list")
    ResponseEntity<ListResponse> postList(@RequestBody CreateListRequest req) {
        ListResponse list = listService.createList(req);
        // TO-DO: SSE

        return ResponseEntity.ok(list); // TO-DO: ^
    }

    @PatchMapping("/list/{id}/contents") 
    ResponseEntity<ListResponse> patchListContents(@PathVariable("id") Long id, @RequestBody UpdateListContentsRequest req) {
        ListResponse list = listService.updateListContents(id, req);
        sseService.propogateUpdates(list.editor_ids(), id); // TO-DO: good, architecturally?

        return ResponseEntity.ok(list); // TO-DO: ^
    }

    @PatchMapping("/list/{id}/metadata") 
    ResponseEntity<ListResponse> patchListMetadata(@PathVariable("id") Long id, @RequestBody UpdateListMetadataRequest req) {
        ListResponse list = listService.updateListMetadata(id, req);
        sseService.propogateUpdates(list.editor_ids(), id); // TO-DO: good, architecturally?

        return ResponseEntity.ok(list); // TO-DO: ^
    }

    @DeleteMapping("/list/{id}")
    ResponseEntity<Void> deleteList(@PathVariable("id") Long id) {
        listService.deleteList(id);
        // TO-DO: SSE

        return ResponseEntity.ok().build(); // TO-DO: ^
    }
}
