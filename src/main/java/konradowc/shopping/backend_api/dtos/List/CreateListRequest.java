package konradowc.shopping.backend_api.dtos.List;

import java.util.List;

public record CreateListRequest(

    String list_name,
    List<String> contents,
    long owner_id

) {}
