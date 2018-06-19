package pl.coderslab.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.coderslab.entities.Tweet;

public interface TweetRepository extends JpaRepository<Tweet, Long>{
	List<Tweet> findByUserIdOrderByCreatedDesc(long id);
	
	@Query(value = "SELECT * FROM `Warsztaty6Twitter`.Tweet ORDER BY created DESC", nativeQuery = true)
	List<Tweet> findAllOrderByCreatedDesc();
	
	@Query(value = "SELECT Tweet.id, Tweet.created, receiver, text, user_id FROM Warsztaty6Twitter.Tweet JOIN Warsztaty6Twitter.User ON Tweet.user_id=User.id WHERE User.deleted = 0 ORDER BY Tweet.created DESC", nativeQuery = true)
	List<Tweet> findAllFromNotDeletedUsersOrderByCreatedDesc();
	
	Tweet findFirstById(long id);
	
	@Query(value = "SELECT * FROM Warsztaty6Twitter.Tweet JOIN Warsztaty6Twitter.User ON Tweet.user_id = User.id where Tweet.id = ?1 AND User.deleted = 0", nativeQuery = true)
	Tweet findFirstFromNotDeletedUserById(long id);
	
	@Query(value = "SELECT COUNT(*) FROM `Warsztaty6Twitter`.Comment WHERE tweet_id = ?1", nativeQuery = true)
	int findCommentCountById(long id);
	
	@Query(value = "SELECT COUNT(*) FROM Warsztaty6Twitter.Comment JOIN Warsztaty6Twitter.User ON Comment.user_id = User.id WHERE tweet_id = ?1 AND User.deleted = 0;", nativeQuery = true)
	int findCommentCountFromNotDeletedUsersById(long id);
	
	@Query(value = "SELECT COUNT(*) FROM `Warsztaty6Twitter`.Tweet", nativeQuery = true)
	int tweetCount();
	
	@Query(value = "SELECT COUNT(*) FROM Warsztaty6Twitter.Tweet JOIN Warsztaty6Twitter.User ON Tweet.user_id=User.id WHERE User.deleted = 0 ORDER BY Tweet.created DESC", nativeQuery = true)
	int tweetCountFromNotDeletedUsers();
	
	@Query(value = "SELECT * FROM `Warsztaty6Twitter`.Tweet WHERE user_id = ?1 ORDER BY created DESC", nativeQuery = true)
	List<Tweet> findAllByUserIdOrderByCreatedDesc(long id);
	
	

	

}
