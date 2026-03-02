package konradowc.shopping.backend_api.dtos.Recipe;

import java.util.List;

public record RecipesResponse (

    List<Recipe> results

) {}
