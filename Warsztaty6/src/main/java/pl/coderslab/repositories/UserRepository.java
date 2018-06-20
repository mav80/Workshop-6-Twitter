package pl.coderslab.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pl.coderslab.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findFirstByUsername(String username);
	User findFirstByEmail(String email);
	User findFirstById(long id);
	
	@Query(value = "SELECT * FROM User WHERE username LIKE %?1%", nativeQuery = true)
	List<User> findByUsernameLike(String name);
	
	@Query(value = "SELECT * FROM User WHERE email LIKE %?1%", nativeQuery = true)
	List<User> findByEmailLike(String email);

}
