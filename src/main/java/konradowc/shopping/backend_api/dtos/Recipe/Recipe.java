package konradowc.shopping.backend_api.dtos.Recipe;

import java.util.List;

public record Recipe (

    Long id,
    List<Ingredient> ingredients

) {}
