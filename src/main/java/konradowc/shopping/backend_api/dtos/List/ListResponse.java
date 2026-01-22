package konradowc.shopping.backend_api.dtos.List;

import java.util.List;

public record ListResponse(

    long list_id,
    String list_name,
    List<String> contents,
    long owner_id

) {}
