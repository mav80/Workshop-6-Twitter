package pl.coderslab.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import pl.coderslab.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	
	List<Comment> findAllByTweetIdOrderByCreatedDesc(long tweetId);
	List<Comment> findAllByTweetIdOrderByCreatedAsc(long tweetId);
	
	@Query(value = "SELECT * FROM `Warsztaty6Twitter`.Comment ORDER BY created DESC", nativeQuery = true)
	List<Comment> findAllOrderByCreatedDesc();
	
	@Query(value = "SELECT * FROM `Warsztaty6Twitter`.Comment ORDER BY created ASC", nativeQuery = true)
	List<Comment> findAllOrderByCreatedAsc();
	
	@Query(value = "SELECT comment.id, comment.text, comment.created, comment.receiver, comment.user_id, comment.tweet_id FROM Warsztaty6Twitter.Comment AS comment JOIN(SELECT Tweet.id, Tweet.created, receiver, text, user_id FROM Warsztaty6Twitter.Tweet JOIN Warsztaty6Twitter.User ON Tweet.user_id=User.id WHERE User.deleted = 0 ORDER BY Tweet.created DESC) AS tweet ON comment.tweet_id = tweet.id ORDER BY comment.created ASC", nativeQuery = true)
	List<Comment> findAllOrderByCreatedAscOnlyForTweetsFromNotDeletedUsers();
	
	
	@Query(value = "SELECT id, text, created, receiver, user_id, tweet_id FROM (SELECT comment.id, comment.text, comment.created, comment.receiver, comment.user_id, comment.tweet_id FROM Warsztaty6Twitter.Comment AS comment JOIN (SELECT Tweet.id, Tweet.created, receiver, text, user_id FROM Warsztaty6Twitter.Tweet JOIN Warsztaty6Twitter.User ON Tweet.user_id=User.id WHERE User.deleted = 0 ORDER BY Tweet.created DESC) AS tweet ON comment.tweet_id = tweet.id ORDER BY comment.created ASC) AS commentsFromNotDeletedUsers JOIN (SELECT id AS userid, deleted FROM Warsztaty6Twitter.User) AS user ON commentsFromNotDeletedUsers.user_id = user.userid WHERE user.deleted = 0 ORDER BY commentsFromNotDeletedUsers.created ASC", nativeQuery = true)
	List<Comment> findAllFromNotDeletedUsersOrderByCreatedAscOnlyForTweetsFromNotDeletedUsers();
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM Warsztaty6Twitter.Comment WHERE user_id = ?1", nativeQuery = true)
	void customDeleteAllUserComments(long id);

}
