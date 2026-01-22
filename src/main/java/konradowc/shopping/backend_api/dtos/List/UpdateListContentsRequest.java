package konradowc.shopping.backend_api.dtos.List;

import java.util.List;

public record UpdateListContentsRequest (

    List<String> contents_to_add

) {}
