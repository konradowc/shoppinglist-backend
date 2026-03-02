package konradowc.shopping.backend_api.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import konradowc.shopping.backend_api.dtos.Recipe.*;

@Service
public class RecipeClient {
    private final RestClient restClient;
    
    @Value("${spoonacular.api_key}")
    private String apiKey; // TO-DO: not safe?

    public RecipeClient(RestClient restClient) { this.restClient = restClient; }

    public RecipesResponse getRecipeIngredientsFromQuery(String recipeName) {
        RecipesResponse recipes = restClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("https://api.spoonacular.com/recipes/complexSearch")
                .queryParam("query", recipeName)
                .queryParam("number", 1) // so reduce cost
                .queryParam("fillIngredients", true) // to get ingredients
                .queryParam("apiKey", apiKey)
                .build())
            .retrieve()
            .body(RecipesResponse.class);

        return recipes;
    }
}
