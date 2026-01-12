package konradowc.shopping.backend_api.repositories;

import org.springframework.stereotype.Repository;

import konradowc.shopping.backend_api.models.ListModel;

import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface ListRepo extends JpaRepository<ListModel, String> {
    
}
