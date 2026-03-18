package konradowc.shopping.backend_api.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import konradowc.shopping.backend_api.dtos.List.*;
import konradowc.shopping.backend_api.dtos.Recipe.ChosenRecipeRequest;
import konradowc.shopping.backend_api.dtos.Recipe.RecipesResponse;
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

        return ResponseEntity.ok(list); // TO-DO: ^
    }

    @PatchMapping("/list/{id}/contents") 
    ResponseEntity<ListResponse> patchListContents(@PathVariable("id") Long id, @RequestBody UpdateListContentsRequest req) {
        ListResponse list = listService.updateListContents(id, req);
        sseService.propogateUpdates(list.editor_ids(), id);

        return ResponseEntity.ok(list); // TO-DO: ^
    }

    @PatchMapping("/list/{id}/metadata") 
    ResponseEntity<ListResponse> patchListMetadata(@PathVariable("id") Long id, @RequestBody UpdateListMetadataRequest req) {
        ListResponse list = listService.updateListMetadata(id, req);
        sseService.propogateUpdates(list.editor_ids(), id); // TO-DO: good, architecturally? 
                                                            // will propogate new lists that were just shared

        return ResponseEntity.ok(list); // TO-DO: ^
    }

    @DeleteMapping("/list/{id}")
    ResponseEntity<Void> deleteList(@PathVariable("id") Long id) {
        List<Long> listEditors = listService.getList(id).editor_ids(); // need to get editors *before* deletion

        listService.deleteList(id); // TO-DO: throws lazy-loading exception - but it works? and it doesn't throw it when this line is commented out?

        sseService.propogateDelete(listEditors, id); // TO-DO: potential edge case: editor added exactly right after someone deletes - won't propogate to them
                                                            // TO-DO: consider user may still be typing (and making PATCHs to a deleted list; consider in error handling)
        return ResponseEntity.ok().build(); // TO-DO: ^
    }

    // recipe stuff

    @GetMapping("/list/{id}/item/{itemId}/expandable-options")
    ResponseEntity<RecipesResponse> getListItemExpandableOptions(@PathVariable("id") Long id, @PathVariable("itemId") Long itemId) {
        RecipesResponse recipes = listService.getListItemExpandableOptions(id, itemId);

        return ResponseEntity.ok(recipes); // TO-DO: ^
    }

    @PostMapping("/list/{id}/item/{itemId}/expanded")
    ResponseEntity<ListResponse> postListItemExpandedItems(@PathVariable("id") Long id, @PathVariable("itemId") Long itemId, @RequestBody ChosenRecipeRequest req) {
        ListResponse list = listService.createItemListExpandableItems(id, itemId, req);

        return ResponseEntity.ok(list); // TO-DO: ^
    }
}
