package konradowc.shopping.backend_api.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import konradowc.shopping.backend_api.dtos.List.*;
import konradowc.shopping.backend_api.dtos.Recipe.ChosenRecipeRequest;
import konradowc.shopping.backend_api.dtos.Recipe.RecipesResponse;
import konradowc.shopping.backend_api.services.ListService;
import konradowc.shopping.backend_api.services.SseService;


@CrossOrigin(origins = "http://localhost:5173",
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
        return ResponseEntity.ok(list);
    }

    @PostMapping("/list")
    ResponseEntity<ListResponse> postList(@RequestBody CreateListRequest req) {
        ListResponse list = listService.createList(req);

        return ResponseEntity.ok(list);
    }

    @PatchMapping("/list/{id}/contents") 
    ResponseEntity<ListResponse> patchListContents(@PathVariable("id") Long id, @RequestBody UpdateListContentsRequest req) {
        ListResponse list = listService.updateListContents(id, req);
        sseService.propogateUpdates(list.editor_ids(), id);

        return ResponseEntity.ok(list);
    }

    @PatchMapping("/list/{id}/metadata") 
    ResponseEntity<ListResponse> patchListMetadata(@PathVariable("id") Long id, @RequestBody UpdateListMetadataRequest req) {
        ListResponse list = listService.updateListMetadata(id, req);
        sseService.propogateUpdates(list.editor_ids(), id);

        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/list/{id}")
    ResponseEntity<Void> deleteList(@PathVariable("id") Long id) {
        List<Long> listEditors = listService.getList(id).editor_ids();

        listService.deleteList(id); // throws lazy-loading exception - but it works? and it doesn't throw it when this line is commented out?

        sseService.propogateDelete(listEditors, id); // TO-DO: consider user may still be typing (and making PATCHs to a deleted list; consider in error handling)
                                                            
        return ResponseEntity.ok().build();
    }

    // recipe stuff

    @GetMapping("/list/{id}/item/{itemId}/expandable-options")
    ResponseEntity<RecipesResponse> getListItemExpandableOptions(@PathVariable("id") Long id, @PathVariable("itemId") Long itemId) {
        RecipesResponse recipes = listService.getListItemExpandableOptions(id, itemId);

        return ResponseEntity.ok(recipes);
    }

    @PostMapping("/list/{id}/item/{itemId}/expanded")
    ResponseEntity<ListResponse> postListItemExpandedItems(@PathVariable("id") Long id, @PathVariable("itemId") Long itemId, @RequestBody ChosenRecipeRequest req) {
        ListResponse list = listService.createItemListExpandableItems(id, itemId, req);

        return ResponseEntity.ok(list);
    }
}
