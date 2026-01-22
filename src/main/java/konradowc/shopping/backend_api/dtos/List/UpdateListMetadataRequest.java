package konradowc.shopping.backend_api.dtos.List;

public record UpdateListMetadataRequest(

    String list_name,
    long owner_id

) {}
