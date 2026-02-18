package konradowc.shopping.backend_api.services;

import java.util.List;

import org.springframework.stereotype.Service;

import konradowc.shopping.backend_api.dtos.List.*;
import konradowc.shopping.backend_api.models.ListModel;
import konradowc.shopping.backend_api.repositories.ListRepo;

@Service
public class ListService {
    
    private final ListRepo repo;
    public ListService(ListRepo repo) { this.repo = repo; }

    ///
    ///
    /// 
    /// 

    public ListResponse getList(long id) {
        ListModel list = getListModelById(id);
        return listModelToResponse(list);
    }

    public ListResponse createList(CreateListRequest req) {
        ListModel list = ListModel.builder()
                            .list_name(req.list_name())
                            .owner_id(req.owner_id())
                            .editor_ids(List.of(req.owner_id())) // initially sets editors list to just owner (check here if error: immutable?)
                            .contents(req.contents()).build();

        ListModel model = repo.save(list);
        return listModelToResponse(model);
    }

    public ListResponse updateListContents(long id, UpdateListContentsRequest req) {
        ListModel list = getListModelById(id);
        
        list.getContents().addAll(req.contents_to_add());

        ListModel model = repo.save(list);
        return listModelToResponse(model);
    }

    public ListResponse updateListMetadata(long id, UpdateListMetadataRequest req) {
        ListModel list = getListModelById(id);

        //System.out.println("updating metadata // name: " + req.list_name() + " ownerid: " + req.owner_id());
        if(req.list_name() != null)
            list.setList_name(req.list_name());
        if(req.owner_id() != null)
            list.setOwner_id(req.owner_id());
        if(req.editor_ids() != null)
            list.setEditor_ids(req.editor_ids());

        ListModel model = repo.save(list);
        return listModelToResponse(model);
    }

    public long deleteList(long id) {
        repo.deleteById(id);
        return id; // return something else?
    }

    public ListofListsResponse getListsByUser(long userid) {
        Long[] ids = repo.findListIdsByUser(userid);
        return new ListofListsResponse(ids);
    }

    ///
    /// 
    /// 

    private ListResponse listModelToResponse(ListModel model) {
        return new ListResponse(
            model.getId(), 
            model.getList_name(), 
            model.getOwner_id(),
            model.getEditor_ids(),
            model.getContents());
    }

    private ListModel getListModelById(long id) {
        return repo.findById(id)
                    .orElseThrow(() -> new RuntimeException("List " + id + " not found"));
    }
}
