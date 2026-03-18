package konradowc.shopping.backend_api.services;

import org.springframework.stereotype.Service;

import konradowc.shopping.backend_api.dtos.User.*;
import konradowc.shopping.backend_api.models.UserModel;
import konradowc.shopping.backend_api.repositories.UserRepo;

@Service
public class UserService {
    
    private final UserRepo repo;
    public UserService(UserRepo repo) { this.repo = repo; }

    ///
    ///
    /// 
    /// 

    public UserResponse createUser(CreateUserRequest req) {
        UserModel user = UserModel.builder()
                            .password(req.password()).build();

        UserModel model = repo.save(user);
        return userModelToResponse(model);
    }

    public long deleteUser(long id) {
        repo.deleteById(id);
        return id;
    }

    ///
    /// 
    /// 

    private UserResponse userModelToResponse(UserModel model) {
        return new UserResponse(
            model.getId());
    }
}
