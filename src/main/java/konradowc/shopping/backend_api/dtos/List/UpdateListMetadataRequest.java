package konradowc.shopping.backend_api.dtos.List;

import java.util.List;

public record UpdateListMetadataRequest(

    String list_name,
    Long owner_id,
    List<Long> editor_ids

) {}
