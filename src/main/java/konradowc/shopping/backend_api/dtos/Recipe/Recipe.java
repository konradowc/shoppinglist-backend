package konradowc.shopping.backend_api.dtos.Recipe;

import java.util.List;

public record Recipe (

    Long id,
    List<Ingredient> missedIngredients, // all ingredients, as long as no specific ingredients
                                       // passed into the query (not happening at this point)
    String title,
    String image,       // for future
    String imageType    //
                                       
) {}
