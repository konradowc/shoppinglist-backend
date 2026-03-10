package konradowc.shopping.backend_api.services;

import org.springframework.stereotype.Service;

import konradowc.shopping.backend_api.clients.RecipeClient;
import konradowc.shopping.backend_api.dtos.Recipe.RecipesResponse;

@Service
public class RecipeService {
    private final RecipeClient recipeClient;
    public RecipeService(RecipeClient recipeClient) { this.recipeClient = recipeClient; }

    public RecipesResponse getRecipesFromQuery(String query) {
        return recipeClient.getRecipeOptionsFromQuery(query);
    }

    
}
