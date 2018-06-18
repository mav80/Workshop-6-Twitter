package pl.coderslab.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pl.coderslab.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	
	List<Comment> findAllByTweetIdOrderByCreatedDesc(long tweetId);
	List<Comment> findAllByTweetIdOrderByCreatedAsc(long tweetId);
	
	@Query(value = "SELECT * FROM `Warsztaty6Twitter`.Comment ORDER BY created DESC", nativeQuery = true)
	List<Comment> findAllOrderByCreatedDesc();
	
	@Query(value = "SELECT * FROM `Warsztaty6Twitter`.Comment ORDER BY created ASC", nativeQuery = true)
	List<Comment> findAllOrderByCreatedAsc();
	
	@Query(value = "SELECT * FROM Warsztaty6Twitter.Comment AS comments JOIN (SELECT Tweet.id FROM Warsztaty6Twitter.Tweet JOIN Warsztaty6Twitter.User ON Tweet.user_id=User.id WHERE User.deleted = 0 ORDER BY Tweet.created DESC) AS tweets ON comments.tweet_id = tweets.id ORDER BY comments.created ASC", nativeQuery = true)
	List<Comment> findAllOrderByCreatedAscOnlyForTweetsFromNotDeletedUsers();

}
