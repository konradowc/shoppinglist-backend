package konradowc.shopping.backend_api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import konradowc.shopping.backend_api.dtos.List.*;
import konradowc.shopping.backend_api.dtos.Recipe.ChosenRecipeRequest;
import konradowc.shopping.backend_api.dtos.Recipe.Recipe;
import konradowc.shopping.backend_api.dtos.Recipe.RecipesResponse;
import konradowc.shopping.backend_api.models.ItemModel;
import konradowc.shopping.backend_api.models.ListModel;
import konradowc.shopping.backend_api.repositories.ListRepo;
import konradowc.shopping.backend_api.services.ListService;
import konradowc.shopping.backend_api.services.RecipeService;

@ExtendWith(MockitoExtension.class)
public class ListServiceTest {
    
    @Mock
    private ListRepo repo;

    @Mock
    private RecipeService recipeService;

    @InjectMocks
    private ListService listService;

    private ListModel list;
    @BeforeEach
    void setup() {
        list = new ListModel(1, "Sample List", 1, List.of(1L, 2L), new ArrayList<ItemModel>());
        ItemModel itemForList = new ItemModel(1L, list, "Sample Item", false, false, new ArrayList<>(), null, false);
        list.setContents(new ArrayList<>(List.of(itemForList)));
    }

    // saved methods
    void whenRepoSave() {
        when(repo.save(any(ListModel.class))).thenAnswer(invocation -> {
            return invocation.getArgument(0); // custom implementation of repo.save()
        });
    }

    @Test
    void should_getList_when_listExists() {
        when(repo.findById(1L)).thenReturn(Optional.of(list));

        ListResponse response = listService.getList(1);

        assertEquals(1, response.list_id());
    }

    @Test
    void should_notGetList_when_listDoesntExist() {
        when(repo.findById(1L)).thenReturn(Optional.of(list));

        assertThrows(RuntimeException.class, () -> listService.getList(2));
    }

    @Test
    void should_createList() {
        String name = "name";
        Long owner_id = 1L;
        List<String> contents = List.of();
        CreateListRequest req = new CreateListRequest(name, contents, owner_id);
        whenRepoSave();

        ListResponse response = listService.createList(req);

        assertEquals(name, response.list_name());
        assertEquals(owner_id, response.owner_id());
        assertEquals(contents, response.contents());
    }

    @Test
    void should_addListContents() {
        List<String> contentToAdd = List.of("Item");
        List<String> initialContentStrings = list.getContents().stream().map(item -> item.getItemName()).toList();
        UpdateListContentsRequest req = new UpdateListContentsRequest(contentToAdd, null);
        when(repo.findById(1L)).thenReturn(Optional.of(list));
        whenRepoSave();
        
        ListResponse response = listService.updateListContents(1L, req);

        List<String> fullList = new ArrayList<>();
        fullList.addAll(initialContentStrings); fullList.addAll(contentToAdd);
        assertEquals(fullList, response.contents().stream().map(item -> item.getItemName()).toList());
    }

    @Test
    void should_removeListContents() {
        // first, add one
        List<String> contentToAdd = List.of("Item");
        List<String> initialContentStrings = list.getContents().stream().map(item -> item.getItemName()).toList();
        UpdateListContentsRequest req = new UpdateListContentsRequest(contentToAdd, null);
        when(repo.findById(1L)).thenReturn(Optional.of(list));
        whenRepoSave();
        
        ListResponse response = listService.updateListContents(1L, req);

        List<String> fullList = new ArrayList<>();
        fullList.addAll(initialContentStrings); fullList.addAll(contentToAdd);
        assertEquals(fullList, response.contents().stream().map(item -> item.getItemName()).toList());

        // then, try to remove one
        Long anItemId = Long.valueOf(response.contents().stream().map(item -> item.getId()).findFirst().orElseThrow(() -> new RuntimeException("Item was not inserted")));
        List<Long> itemIdsToRemove = List.of(anItemId); // may not be same one
        UpdateListContentsRequest req2 = new UpdateListContentsRequest(null, itemIdsToRemove);

        ListResponse response2 = listService.updateListContents(1L, req2);

        assertEquals(initialContentStrings.size(), response2.contents().size());
    }

    @Test
    void should_updateListName() {
        String newName = "Trader Joe's";
        when(repo.findById(1L)).thenReturn(Optional.of(list));
        whenRepoSave();
        UpdateListMetadataRequest req = new UpdateListMetadataRequest(newName, null, null);

        ListResponse response = listService.updateListMetadata(1, req);

        assertEquals(newName, response.list_name());
    }

    @Test
    void should_updateListOwnerId() {
        Long newOwnerId = 2L;
        when(repo.findById(1L)).thenReturn(Optional.of(list));
        whenRepoSave();
        UpdateListMetadataRequest req = new UpdateListMetadataRequest(null, newOwnerId, null);

        ListResponse response = listService.updateListMetadata(1, req);

        assertEquals(newOwnerId, response.owner_id());
    }

    @Test
    void should_updateListEditorIds() {
        List<Long> newEditorIds = List.of(1L, 2L);
        when(repo.findById(1L)).thenReturn(Optional.of(list));
        whenRepoSave();
        UpdateListMetadataRequest req = new UpdateListMetadataRequest(null, null, newEditorIds);

        ListResponse response = listService.updateListMetadata(1, req);

        assertEquals(newEditorIds, response.editor_ids());
    }

    @Test
    void should_deleteList_when_listExists() {
        // don't need to mock deleteById because it returns void
        Long listId = 1L;

        long id = listService.deleteList(listId);

        assertEquals(listId, id);
    }

    @Test 
    void should_getListsByUser_when_userHasLists() { // when has none, just returns empty array
        Long[] lists = new Long[]{1L, 2L};
        when(repo.findListIdsByUser(1)).thenReturn(lists);

        ListofListsResponse response = listService.getListsByUser(1L);

        assertEquals(lists, response.listIds());
    }

    // expandable / recipe stuff
 
    @Test
    void should_getListItemExpandableOptions_when_itemExists() {
        Long itemId = 1L;
        RecipesResponse actualQueryResponse = new RecipesResponse(List.of(new Recipe(1L, List.of(), "A Sample Item Recipe", "null", "png")));
        when(repo.findById(1L)).thenReturn(Optional.of(list));
        when(recipeService.getRecipesFromQuery("Sample Item")).thenReturn(actualQueryResponse);

        RecipesResponse response = listService.getListItemExpandableOptions(1L, itemId);

        assertEquals(actualQueryResponse, response);
    } 

    @Test
    void should_notGetListItemExpandableOptions_when_itemDoesNotExist() {
        Long itemId = -1L;
        when(repo.findById(1L)).thenReturn(Optional.of(list));

        assertThrows(IllegalArgumentException.class, () -> listService.getListItemExpandableOptions(1L, itemId));
    } 
/* 
    @Test
    void should_createItemListExpandableItems() {
        when(repo.findById(1L)).thenReturn(Optional.of(list));
        long itemId = 1L;
        ChosenRecipeRequest req = new ChosenRecipeRequest(new Recipe(1L, new ArrayList<>(), "A Sample Item Recipe", "null", "png"));

        ListResponse response = listService.createItemListExpandableItems(1L, itemId, req);

        assertEquals(response.contents().stream().filter(item -> item.getId() == itemId).findFirst().orElse(null).isExpanded(), true);
    } */

}
