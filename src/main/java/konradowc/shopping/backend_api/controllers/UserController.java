package konradowc.shopping.backend_api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import konradowc.shopping.backend_api.dtos.List.ListofListsResponse;
import konradowc.shopping.backend_api.dtos.User.*;
import konradowc.shopping.backend_api.services.UserService;
import konradowc.shopping.backend_api.services.ListService;


@CrossOrigin(origins = "http://localhost:5173",
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
@RestController
public class UserController {

    private final UserService userService;
    private final ListService listService;
    public UserController(UserService userService, ListService listService) { this.userService = userService;
                                                                              this.listService = listService;
                                                                            }

    ///
    /// 
    /// 

    @PostMapping("/user")
    ResponseEntity<UserResponse> postUser(@RequestBody CreateUserRequest req) {
        UserResponse user = userService.createUser(req);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/user/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}/lists")
    ResponseEntity<ListofListsResponse> getListsByUser(@PathVariable("userId") Long userid) {
        ListofListsResponse ids = listService.getListsByUser(userid);
        return ResponseEntity.ok(ids);
    }
}
