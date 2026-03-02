package konradowc.shopping.backend_api.services;

import konradowc.shopping.backend_api.clients.RecipeClient;
import konradowc.shopping.backend_api.dtos.Recipe.RecipesResponse;

public class RecipeService {
    private final RecipeClient recipeClient;
    public RecipeService(RecipeClient recipeClient) { this.recipeClient = recipeClient; }

    public RecipesResponse getRecipeFromQuery(String query) {
        return recipeClient.getRecipeIngredientsFromQuery(query);
    }

    
}
