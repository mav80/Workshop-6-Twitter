package pl.coderslab.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.coderslab.entities.Tweet;

public interface TweetRepository extends JpaRepository<Tweet, Long>{
	List<Tweet> findByUserIdOrderByCreatedDesc(long id);
	
	@Query(value = "SELECT * FROM `Warsztaty6Twitter`.Tweet ORDER BY created DESC", nativeQuery = true)
	List<Tweet> findAllOrderByCreatedDesc();
	
	@Query(value = "SELECT Tweet.id, Tweet.created, receiver, text, user_id, editable FROM Warsztaty6Twitter.Tweet JOIN Warsztaty6Twitter.User ON Tweet.user_id=User.id WHERE User.deleted = 0 ORDER BY Tweet.id DESC", nativeQuery = true)
	List<Tweet> findAllFromNotDeletedUsersOrderByCreatedDesc();
	
	@Query(value = "SELECT Tweet.id, Tweet.created, receiver, text, user_id, editable FROM Warsztaty6Twitter.Tweet JOIN Warsztaty6Twitter.User ON Tweet.user_id=User.id WHERE User.deleted = 0 ORDER BY Tweet.id DESC LIMIT ?1 OFFSET ?2", nativeQuery = true)
	List<Tweet> findAllFromNotDeletedUsersOrderByCreatedDescLimitOffset(int limit, int offset);
	
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
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM Warsztaty6Twitter.Tweet WHERE user_id = ?1 ", nativeQuery = true)
	void customDeleteAllUserTweets(long id);
	
	
	@Query(value = "SELECT * FROM Tweet WHERE id NOT IN (SELECT Tweet.id FROM Tweet JOIN Comment ON Tweet.id = Comment.tweet_id)", nativeQuery = true)
	List<Tweet> findAllTweetsWithoutComments();
	

	

}
