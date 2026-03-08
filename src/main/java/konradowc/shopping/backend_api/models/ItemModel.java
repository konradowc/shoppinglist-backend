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

public class ItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // begins at 1
    private long id;

    private String itemName;

    private boolean isExpandable;
    private boolean isExpanded;

    //@OneToMany(cascade = CascadeType.ALL)
    //private List<ItemModel> expandedItems;
}
