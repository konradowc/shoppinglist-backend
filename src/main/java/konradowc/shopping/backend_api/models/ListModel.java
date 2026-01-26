package konradowc.shopping.backend_api.models;

import java.util.List;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE) // necessary for builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) // necessary for Hibernate
public class ListModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String list_name;
    private long owner_id;
    //private List<long> editors_id_list; // TO-DO

    @ElementCollection
    private List<String> contents;
}
