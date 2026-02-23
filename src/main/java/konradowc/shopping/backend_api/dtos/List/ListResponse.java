package konradowc.shopping.backend_api.dtos.List;

import java.util.List;

import konradowc.shopping.backend_api.models.ItemModel;

public record ListResponse(

    long list_id,
    String list_name,
    long owner_id,
    List<Long> editor_ids,
    List<ItemModel> contents

) {}
