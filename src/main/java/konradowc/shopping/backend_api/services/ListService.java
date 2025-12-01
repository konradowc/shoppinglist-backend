package konradowc.shopping.backend_api.services;

import org.springframework.stereotype.Service;

@Service
public class ListService {
    
    public ListService() {}

    public boolean createNewList(String listName) {
        System.out.println("new list: " + listName);
        return true;
    }
}
