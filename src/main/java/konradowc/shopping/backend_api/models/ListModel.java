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
    @GeneratedValue(strategy = GenerationType.AUTO) // begins at 1
    private long id;

    private String list_name;
    private long owner_id;

    @ElementCollection
    private List<Long> editor_ids;

    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemModel> contents;
}
