package konradowc.shopping.backend_api.dtos.List;

import java.util.List;
//import jakarta.validation.constraints.NotBlank;

public record PostListDTO(

    //@NotBlank TO-DO
    String list_name,
    List<String> contents

) {}
