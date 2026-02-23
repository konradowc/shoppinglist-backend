package konradowc.shopping.backend_api.dtos.List;

import java.util.List;

public record UpdateListContentsRequest (

    List<String> content_to_add,
    List<Long> item_ids_to_remove

) {}
