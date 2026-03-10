package konradowc.shopping.backend_api.dtos.Recipe;

import java.util.List;

public record RecipesResponse ( // based off Spoonacular API

    List<Recipe> results

) {}
