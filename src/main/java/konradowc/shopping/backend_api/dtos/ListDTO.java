package konradowc.shopping.backend_api.dtos;

import java.util.List;
//import jakarta.validation.constraints.NotBlank;

public record ListDTO(

    //@NotBlank TO-DO
    String list_name,
    List<String> contents

) {}
