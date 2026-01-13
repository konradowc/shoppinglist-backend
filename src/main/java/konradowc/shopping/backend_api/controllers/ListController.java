package konradowc.shopping.backend_api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import konradowc.shopping.backend_api.dtos.List.DeleteListDTO;
import konradowc.shopping.backend_api.dtos.List.PostListDTO;
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
    ResponseEntity<Void> postList(@RequestBody PostListDTO dto) {
        System.out.println("our DTO we get: " + dto.toString());

        long listId = listService.createList(dto);
        if(listId != -1) { // TO-DO: no mechanism exists to return -1
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping
    ResponseEntity<Void> deleteList(@RequestBody DeleteListDTO dto) {
        System.out.println("our DTO we get: " + dto.toString());

        long listId = listService.deleteList(dto);
        if(listId != -1) { // TO-DO: no mechanism exists to return -1
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }
}
