package pl.coderslab.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.coderslab.entities.Message;

public interface MessageRepository extends JpaRepository<Message, Long>{
	
	List<Message> findAllByReceiverIdOrderByCreatedDesc(long id);
	List<Message> findAllByReceiverIdOrderByCreatedAsc(long id);
	
	List<Message> findAllBySenderIdOrderByCreatedDesc(long id);
	List<Message> findAllBySenderIdOrderByCreatedAsc(long id);
	
	Message findFirstById(long id);

}
