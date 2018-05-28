package pl.coderslab.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.coderslab.entities.Tweet;

public interface TweetRepository extends JpaRepository<Tweet, Long>{
	List<Tweet> findByUserIdOrderByCreatedDesc(long id);
	
	@Query(value = "SELECT * FROM `Warsztaty6Twitter`.Tweet ORDER BY created DESC", nativeQuery = true)
	List<Tweet> findAllOrderByCreatedDesc();
	
	Tweet findFirstById(long id);

}
