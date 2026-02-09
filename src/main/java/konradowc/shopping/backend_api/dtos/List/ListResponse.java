package konradowc.shopping.backend_api.dtos.List;

import java.util.List;

public record ListResponse(

    long list_id,
    String list_name,
    long owner_id,
    List<Long> editor_ids,
    List<String> contents

) {}
