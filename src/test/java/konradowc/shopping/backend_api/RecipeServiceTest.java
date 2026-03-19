package konradowc.shopping.backend_api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import konradowc.shopping.backend_api.clients.RecipeClient;
import konradowc.shopping.backend_api.dtos.Recipe.Recipe;
import konradowc.shopping.backend_api.dtos.Recipe.RecipesResponse;
import konradowc.shopping.backend_api.services.RecipeService;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {
    
    @Mock
    private RecipeClient recipeClient;

    @InjectMocks
    private RecipeService recipeService;

    @Test
    void should_getRecipesFromQuery() {
        String query = "Banana cake";
        RecipesResponse actualQueryResponse = new RecipesResponse(List.of(new Recipe(1L, List.of(), "Banana cake recipe", "null", "png")));
        when(recipeClient.getRecipeOptionsFromQuery(query)).thenReturn(actualQueryResponse);

        RecipesResponse response = recipeService.getRecipesFromQuery(query);

        assertEquals(actualQueryResponse, response);
    }
}
