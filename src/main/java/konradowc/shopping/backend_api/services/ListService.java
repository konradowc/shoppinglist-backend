package konradowc.shopping.backend_api.services;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import konradowc.shopping.backend_api.dtos.List.DeleteListDTO;
import konradowc.shopping.backend_api.dtos.List.PostListDTO;
import konradowc.shopping.backend_api.models.ListModel;
import konradowc.shopping.backend_api.repositories.ListRepo;

@Service
public class ListService {
    
    private final ListRepo repo;

    public ListService(ListRepo repo) { 
        this.repo = repo;
    }

    public long createList(PostListDTO listDTO) {
        ListModel list = new ListModel();
        list.setList_name(listDTO.list_name());
        list.setContents(listDTO.contents());
        list.setOwner_id(1); // TO-DO
        // editor list ids TO-DO

        ListModel createdList = repo.save(list);

        System.out.println("new list: " + createdList.getList_name() + " " + createdList.getId());
        return createdList.getId();
    }

    public long deleteList(DeleteListDTO listDTO) {
        long id = listDTO.list_id();
        repo.deleteById(String.valueOf(id));
        
        return id;
    }

    public boolean updateListRow(String listName, int row, String rowContents) {
        ListModel list = new ListModel();
        


        System.out.println("row contents: " + rowContents);
        return true;
    }
}
