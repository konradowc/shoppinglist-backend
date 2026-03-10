package konradowc.shopping.backend_api.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import konradowc.shopping.backend_api.dtos.List.*;
import konradowc.shopping.backend_api.dtos.Recipe.ChosenRecipeRequest;
import konradowc.shopping.backend_api.dtos.Recipe.Ingredient;
import konradowc.shopping.backend_api.dtos.Recipe.Recipe;
import konradowc.shopping.backend_api.dtos.Recipe.RecipesResponse;
import konradowc.shopping.backend_api.models.ItemModel;
import konradowc.shopping.backend_api.models.ListModel;
import konradowc.shopping.backend_api.repositories.ListRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j // for logging
@Service
public class ListService {
    
    private final ListRepo repo;
    private final RecipeService recipeService;
    public ListService(ListRepo repo, RecipeService recipeService) { 
        this.repo = repo; this.recipeService = recipeService; 
    }

    ///
    ///
    /// 
    /// 

    public ListResponse getList(long id) {
        ListModel list = getListModelById(id);
        return listModelToResponse(list);
    }

    public ListResponse createList(CreateListRequest req) {
        log.atInfo().log("Creating list");
        List<Boolean> expandable = checkExpandability(req.contents());

        ListModel list = ListModel.builder()
                            .list_name(req.list_name())
                            .owner_id(req.owner_id())
                            .editor_ids(List.of(req.owner_id()))
                            .build();
        list.setContents(createItemModelsFromStrings(req.contents(), expandable, list, false)); // do it after so we have the list reference
        
        ListModel model = repo.save(list);
        return listModelToResponse(model);
    }

    public ListResponse updateListContents(long id, UpdateListContentsRequest req) {
        ListModel list = getListModelById(id);

        System.out.println("to_remove:" + req.item_ids_to_remove() + "\nto add:"
        + req.content_to_add());

        // remove given ids
        if(req.item_ids_to_remove() != null) {
            for(Long itemId : req.item_ids_to_remove()) {
                list.getContents().removeIf(item -> (item.getId() == itemId)); // TO-DO: can replace in future with more efficient
            }
        }

        // add given contents
        if(req.content_to_add() != null) {
            List<String> items = req.content_to_add();
            List<Boolean> expandable = checkExpandability(items);

            List<ItemModel> itemsToAdd = createItemModelsFromStrings(items, expandable, list, false);

            // add to inverse side (remember bidirectionality) - we already set the foreign key before, so maybe this is optional? or important if we're going to access it from this way
            list.getContents().addAll(itemsToAdd); // updating in-memory (won't do any database change)
        }

        ListModel model = repo.save(list);
        return listModelToResponse(model);
    }

    public ListResponse updateListMetadata(long id, UpdateListMetadataRequest req) {
        ListModel list = getListModelById(id);

        //System.out.println("updating metadata // name: " + req.list_name() + " ownerid: " + req.owner_id());
        if(req.list_name() != null)
            list.setList_name(req.list_name());
        if(req.owner_id() != null)
            list.setOwner_id(req.owner_id());
        if(req.editor_ids() != null)
            list.setEditor_ids(req.editor_ids());

        ListModel model = repo.save(list);
        return listModelToResponse(model);
    }

    @Transactional
    public long deleteList(long id) {
        repo.deleteById(id);
        return id; // return something else?
    }

    public ListofListsResponse getListsByUser(long userid) {
        Long[] ids = repo.findListIdsByUser(userid);
        return new ListofListsResponse(ids);
    }

    // expandable/recipe stuff TO-DO: make a separate service + controller?

    public RecipesResponse getListItemExpandableOptions(long id, long itemid) {
        ListModel list = getListModelById(id);
        List<ItemModel> items = list.getContents();

        ItemModel itemToExpand = null;
        for(ItemModel item : items) { 
            if(item.getId() == itemid) itemToExpand = item;
        }
        if(itemToExpand == null) throw new IllegalArgumentException("Item not found in list");

        // assuming all will be recipe
        RecipesResponse recipes = recipeService.getRecipesFromQuery(itemToExpand.getItemName());
        return recipes;
    }

    public ListResponse createItemListExpandableItems(long id, long itemId, ChosenRecipeRequest req) { // TO-DO: 'set'? also, Recipe data type? maybe make a wrapper?
        // TO-DO: in future when storing recipe in separate recipe repo, make sure the whole recipe is stored immediately, not post-user modification? since they might remove things they already have
        // counterpoint: they might modify it to their liking

        Recipe recipe = req.recipe();

        List<Ingredient> ingredients = recipe.missedIngredients(); // can convert this to stream (.map)
        List<String> ingredientNames = new ArrayList<>();
        for(Ingredient i : ingredients) { ingredientNames.add(i.name()); }

        ListModel list = getListModelById(id);
        ItemModel itemToExpand = null;

        // set itemToChange to whatever
        List<ItemModel> allItems = list.getContents();
        for(ItemModel item : allItems) { if(item.getId() == itemId) itemToExpand = item; }
        if(itemToExpand == null) throw new IllegalArgumentException("Item not found in list (2)");

        List<ItemModel> expandedItems = createItemModelsFromStrings(
            ingredientNames, 
            new ArrayList<>(Collections.nCopies(ingredientNames.size(), false)),
            list,
            true
        );
        for(ItemModel item : expandedItems) { item.setParentItem(itemToExpand); } // set foreign key

        // need to do this, rather than set, due to Hibernate not liking when replacing the entity it was originally tracking (even if it had nothing)
        itemToExpand.getExpandedItems().clear();
        itemToExpand.getExpandedItems().addAll(expandedItems);

        // last thing: set the original item's 'expanded' to true, since that is what just happened
        itemToExpand.setExpanded(true);

        ListModel model = repo.save(list);
        return listModelToResponse(model);
    }

    ///
    /// 
    /// 

    private ListResponse listModelToResponse(ListModel model) {
        return new ListResponse(
            model.getId(), 
            model.getList_name(), 
            model.getOwner_id(),
            model.getEditor_ids(),
            model.getContents());
    }

    private ListModel getListModelById(long id) {
        return repo.findById(id)
                    .orElseThrow(() -> new RuntimeException("List " + id + " not found"));
    }

    private List<Boolean> checkExpandability(List<String> itemStrings) {
        // TO-DO: can use a simple AI
        return new ArrayList<>(Collections.nCopies(itemStrings.size(), true));
    }

    private List<ItemModel> createItemModelsFromStrings(List<String> itemStrings, List<Boolean> expandables, ListModel associatedList, boolean areItemChildren) {
        List<ItemModel> items = new ArrayList<>();
        for(int i = 0; i < itemStrings.size(); i++) {
            String itemName = itemStrings.get(i);
            Boolean expandable = expandables.get(i);

            items.add(ItemModel.builder()
                                        .itemName(itemName)
                                        .isExpandable(expandable)
                                        .isExpanded(false)
                                        .expandedItems(new ArrayList<ItemModel>())
                                        .list(associatedList)
                                        .isAnItemChild(areItemChildren)
                                        .build());
        }

        return items;
    }
}
