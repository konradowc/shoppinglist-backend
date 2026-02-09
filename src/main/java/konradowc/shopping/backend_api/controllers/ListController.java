package konradowc.shopping.backend_api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import konradowc.shopping.backend_api.dtos.List.*;
import konradowc.shopping.backend_api.services.ListService;


@CrossOrigin(origins = "http://localhost:5173",  // TO-DO: temporary
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
@RestController
public class ListController {

    private final ListService listService;
    public ListController(ListService listService) { this.listService = listService; }

    ///
    /// 
    /// 

    @GetMapping("/list/{id}")
    ResponseEntity<ListResponse> getList(@PathVariable long id) {
        ListResponse list = listService.getList(id);
        return ResponseEntity.ok(list); // TO-DO: account for error handling in future
    }

    @PostMapping("/list")
    ResponseEntity<ListResponse> postList(@RequestBody CreateListRequest req) {
        ListResponse list = listService.createList(req);
        return ResponseEntity.ok(list); // TO-DO: ^
    }

    @PatchMapping("/list/{id}/contents") 
    ResponseEntity<ListResponse> patchListContents(@PathVariable long id, @RequestBody UpdateListContentsRequest req) {
        ListResponse list = listService.updateListContents(id, req);
        return ResponseEntity.ok(list); // TO-DO: ^
    }

    @PatchMapping("/list/{id}/metadata") 
    ResponseEntity<ListResponse> patchListMetadata(@PathVariable long id, @RequestBody UpdateListMetadataRequest req) {
        ListResponse list = listService.updateListMetadata(id, req);
        return ResponseEntity.ok(list); // TO-DO: ^
    }

    @DeleteMapping("/list/{id}")
    ResponseEntity<Void> deleteList(@PathVariable long id) {
        listService.deleteList(id);
        return ResponseEntity.ok().build(); // TO-DO: ^
    }
}
