package konradowc.shopping.backend_api.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import konradowc.shopping.backend_api.models.ListModel;

@Repository
public interface ListRepo extends JpaRepository<ListModel, String> {
}
