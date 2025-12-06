package konradowc.shopping.backend_api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import konradowc.shopping.backend_api.services.ListService;

@RestController
public class ListController {

    private final ListService listService;
    
    public ListController(ListService listService) {
        this.listService = listService;
    }

    @CrossOrigin(origins = "http://localhost:5173") // TO-DO: temporary
    @PostMapping("/list")
    ResponseEntity<Void> postList(@RequestBody String listName) {
        if(listService.createNewList(listName)) {
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }
}
