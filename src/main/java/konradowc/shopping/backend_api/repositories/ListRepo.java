package konradowc.shopping.backend_api.repositories;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import konradowc.shopping.backend_api.models.ListModel;

@Repository
public interface ListRepo extends JpaRepository<ListModel, Long> {

    @Query("SELECT l.id FROM ListModel l WHERE :userid MEMBER OF l.editor_ids")
    Long[] findListIdsByUser(@Param("userid") long userid);

}
