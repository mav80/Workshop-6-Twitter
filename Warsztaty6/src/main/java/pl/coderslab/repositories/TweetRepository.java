package pl.coderslab.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.coderslab.entities.Tweet;

public interface TweetRepository extends JpaRepository<Tweet, Long>{
	List<Tweet> FindAllByUserIdOrderByCreated(long id);

}
