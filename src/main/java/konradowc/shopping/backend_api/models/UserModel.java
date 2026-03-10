package konradowc.shopping.backend_api.models;

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
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String password;

    @Column(nullable = true)
    private String nickname; // not necessarily unique
}
