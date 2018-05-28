package pl.coderslab.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.coderslab.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	
	List<Comment> findAllByTweetIdOrderByCreatedDesc(long tweetId);
	List<Comment> findAllByTweetIdOrderByCreatedAsc(long tweetId);

}
