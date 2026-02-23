package konradowc.shopping.backend_api.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE) // necessary for builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) // necessary for Hibernate

@Embeddable // TO-DO: maybe temporary?
public class ItemModel {

    /*
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // begins at 1
    private long id; */

    private String itemName;

    private boolean expandable;
    //private String expandableText; // TO-DO
}
