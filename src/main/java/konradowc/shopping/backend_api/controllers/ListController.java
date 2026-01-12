package konradowc.shopping.backend_api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import konradowc.shopping.backend_api.dtos.ListDTO;
import konradowc.shopping.backend_api.models.ListModel;
import konradowc.shopping.backend_api.services.ListService;

@RestController
@CrossOrigin(origins = "http://localhost:5173") // TO-DO: temporary
public class ListController {

    private final ListService listService;
    
    public ListController(ListService listService) {
        this.listService = listService;
    }

    @PostMapping("/list")
    ResponseEntity<Void> postList(@RequestBody ListDTO list) {
        if(listService.createList(list)) {
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }
}
