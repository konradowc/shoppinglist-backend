package konradowc.shopping.backend_api;

import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import konradowc.shopping.backend_api.models.ListModel;
import konradowc.shopping.backend_api.repositories.ListRepo;
import konradowc.shopping.backend_api.services.RecipeService;

@ExtendWith(MockitoExtension.class)
public class ListServiceTest {
    
    @Mock
    private ListRepo repo;

    @Mock
    private RecipeService recipeService;

    private ListModel list;
    @BeforeEach
    void setup() {
        list = new ListModel(1, "name", 1, List.of(1L, 2L), List.of());
    }

    @Test
    void should_getList_when_listExists() {
        when(repo.findById(1L)).thenReturn(Optional.of(list));
    }
}
