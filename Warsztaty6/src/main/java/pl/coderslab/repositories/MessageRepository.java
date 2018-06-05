package pl.coderslab.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pl.coderslab.entities.Message;

public interface MessageRepository extends JpaRepository<Message, Long>{
	
	List<Message> findAllByReceiverIdOrderByCreatedDesc(long id);
	List<Message> findAllByReceiverIdOrderByCreatedAsc(long id);
	
	List<Message> findAllBySenderIdOrderByCreatedDesc(long id);
	List<Message> findAllBySenderIdOrderByCreatedAsc(long id);
	
	Message findFirstById(long id);
	
	@Query(value = "SELECT COUNT(*) FROM `Warsztaty6Twitter`.Message WHERE receiver_id = ?1 AND viewed = FALSE", nativeQuery = true)
	int howManyUnreadMessagesByUserId(long id);

}
