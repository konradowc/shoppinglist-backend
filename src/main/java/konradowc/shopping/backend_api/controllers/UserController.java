package konradowc.shopping.backend_api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import konradowc.shopping.backend_api.dtos.User.*;
import konradowc.shopping.backend_api.services.UserService;


@CrossOrigin(origins = "http://localhost:5173",  // TO-DO: temporary
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
@RestController
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) { this.userService = userService; }

    ///
    /// 
    /// 

    @PostMapping("/user")
    ResponseEntity<UserResponse> postUser(@RequestBody CreateUserRequest req) {
        UserResponse user = userService.createUser(req);
        return ResponseEntity.ok(user); // TO-DO: ^
    }

    @DeleteMapping("/user/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build(); // TO-DO: ^
    }
}
