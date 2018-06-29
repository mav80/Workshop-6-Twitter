package pl.coderslab.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import pl.coderslab.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findFirstByUsername(String username);
	User findFirstByEmail(String email);
	User findFirstById(long id);
	List<User> findAllByEmail(String email);
	List<User> findAllByUsername(String username);
	
	@Query(value = "SELECT * FROM User WHERE username LIKE %?1%", nativeQuery = true)
	List<User> findByUsernameLike(String name);
	
	@Query(value = "SELECT * FROM User WHERE email LIKE %?1%", nativeQuery = true)
	List<User> findByEmailLike(String email);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM Warsztaty6Twitter.User WHERE id = ?1 ; ", nativeQuery = true)
	void customDeleteUser(long id);
	
	@Query(value = "SELECT * FROM User WHERE admin = 0", nativeQuery = true)
	List<User>findAllWithoutAdmins();

}
