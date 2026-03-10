package konradowc.shopping.backend_api.dtos.Recipe;

public record Ingredient ( // based off Spoonacular API

    String name,
    //String aisle, // for future maybe?
    Double amount,
    String unitShort // will be 'cup' instead of 'cups'

) {}
