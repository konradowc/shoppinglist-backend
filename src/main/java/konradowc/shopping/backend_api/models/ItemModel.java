package konradowc.shopping.backend_api.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @ManyToOne
    @JsonIgnore
    private ListModel list; // foreign key to its corresponding list

    private String itemName;

    private boolean isExpandable;
    private boolean isExpanded;

    // expandedItems fields

    @OneToMany(mappedBy = "parentItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemModel> expandedItems;

    @ManyToOne(optional = true)
    @JsonIgnore
    private ItemModel parentItem; // can be null if has no parent
}
