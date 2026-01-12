package konradowc.shopping.backend_api.services;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import konradowc.shopping.backend_api.models.ListModel;
import konradowc.shopping.backend_api.repositories.ListRepo;

@Service
public class ListService {
    
    private final ListRepo repo;

    public ListService(ListRepo repo) { 
        this.repo = repo;
    }

    public boolean createNewList(String listName) {
        ListModel list = new ListModel();
        list.setList_name(listName);
        list.setContents(new ArrayList<String>());
        //newList.setOwner_id(); // TO-DO
        // editor list ids TO-DO

        repo.save(list);
            

        System.out.println("new list: " + listName);
        return true;
    }

    public boolean updateListRow(String listName, int row, String rowContents) {
        ListModel list = new ListModel();
        


        System.out.println("row contents: " + rowContents);
        return true;
    }
}
