package konradowc.shopping.backend_api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import konradowc.shopping.backend_api.dtos.User.CreateUserRequest;
import konradowc.shopping.backend_api.dtos.User.UserResponse;
import konradowc.shopping.backend_api.models.UserModel;
import konradowc.shopping.backend_api.repositories.UserRepo;
import konradowc.shopping.backend_api.services.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepo repo;

    @InjectMocks
    private UserService userService;
    
    private UserModel user;
    @BeforeEach
    void setup() {
        user = new UserModel(1L, "password123", "John");
    }

    // saved methods
    void whenRepoSave() {
        when(repo.save(any(UserModel.class))).thenAnswer(invocation -> {
            return invocation.getArgument(0); // custom implementation of repo.save()
        });
    }

    @Test
    void should_createUser() {
        whenRepoSave();
        CreateUserRequest req = new CreateUserRequest("mypswrd123");

        UserResponse response = userService.createUser(req);

        assertNotNull(response.user_id()); // can't compare password because UserResponse doesn't return it
    }

    @Test
    void should_deleteUser() {
        // don't need to mock deleteById because it returns void
        Long userId = 1L;

        long id = userService.deleteUser(userId);

        assertEquals(userId, id);
    }
}
