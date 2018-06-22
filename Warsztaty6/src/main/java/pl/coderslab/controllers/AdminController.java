package pl.coderslab.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.coderslab.app.Cookies;
import pl.coderslab.app.MessageUtils;
import pl.coderslab.entities.Comment;
import pl.coderslab.entities.Tweet;
import pl.coderslab.entities.User;
import pl.coderslab.repositories.CommentRepository;
import pl.coderslab.repositories.MessageRepository;
import pl.coderslab.repositories.TweetRepository;
import pl.coderslab.repositories.UserRepository;

@Controller
public class AdminController {
	
	@Autowired
	TweetRepository tweetRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	MessageRepository messageRepository;
	@Autowired
	CommentRepository commentRepository;
	
	@GetMapping("/panelAdmin")
	public String adminPanel(Model model, HttpSession session, HttpServletRequest request,
			@RequestParam(defaultValue="") String userSearchNameLike,
			@RequestParam(defaultValue="") String userSearchEmailLike,
			@RequestParam(defaultValue="-1") long userSearchId,
			@RequestParam(defaultValue="false") boolean userSearchShowAll) {
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(user != null && user.isAdmin()) {
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
			
			if(!userSearchNameLike.isEmpty()) {
				model.addAttribute("users", userRepository.findByUsernameLike(userSearchNameLike));
				System.out.println("Wyniki wyszukiwania:");
				System.out.println(userRepository.findByUsernameLike(userSearchNameLike));
			}
			if(!userSearchEmailLike.isEmpty()) {
				model.addAttribute("users", userRepository.findByEmailLike(userSearchEmailLike));
				System.out.println("Wyniki wyszukiwania:");
				System.out.println(userRepository.findByEmailLike(userSearchEmailLike));
			}
			if(userSearchId > 0) {
				model.addAttribute("users", userRepository.findFirstById(userSearchId));
				System.out.println("Wyniki wyszukiwania:");
				System.out.println(userRepository.findFirstById(userSearchId));
			}
			if(userSearchShowAll) {
				model.addAttribute("users", userRepository.findByEmailLike(userSearchEmailLike));
				System.out.println("Wyniki wyszukiwania:");
				System.out.println(userRepository.findByEmailLike(userSearchEmailLike));
			}
			
			
			
			if(!userSearchNameLike.isEmpty() || !userSearchEmailLike.isEmpty() || userSearchId > 0 || userSearchShowAll) {
				model.addAttribute("searchResultMessage", "Oto wyniki wyszukiwania:");
			}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			return "panelAdmin";
		} else {
			model.addAttribute("infoError", "Musisz mieć uprawnienia administratora aby wejść do panelu admina!");
			return "userLoginForm";
		}
		
	}
	
	
	
	
	
	
	
	@GetMapping("/adminToggleBanUser/{id}")
	public String adminPanelBanUser(Model model, HttpSession session, HttpServletRequest request,
			@PathVariable long id) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(user != null && user.isAdmin()) {
			
			User userToBan = userRepository.findFirstById(id);
			
			if(userToBan != null) {
				if(userToBan.isEnabled()) {
					userToBan.setEnabled(false);
					userRepository.save(userToBan);
					model.addAttribute("operationInfo", "Użytkownika " + userToBan.getUsername() + " zbanowano pomyślnie.");
				} else {
					userToBan.setEnabled(true);
					userRepository.save(userToBan);
					model.addAttribute("operationInfo", "Użytkownika " + userToBan.getUsername() + " odbanowano pomyślnie.");
				}
			} else {
				model.addAttribute("operationInfo", "Użytkownik o takim id nie istnieje.");
			}
			
			
			
			
			return "redirect:/panelAdmin";
		} else {
			model.addAttribute("infoError", "Musisz mieć uprawnienia administratora aby wejść do panelu admina!");
			return "userLoginForm";
		}
		
		
	}
	
	
	@GetMapping("/adminToggleDeleteUser/{id}")
	public String adminToggleDeleteUser(Model model, HttpSession session, HttpServletRequest request,
			@PathVariable long id) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(user != null && user.isAdmin()) {
			
			User userToDelete = userRepository.findFirstById(id);
			
			if(userToDelete != null) {
				if(userToDelete.isDeleted()) {
					userToDelete.setDeleted(false);
					userRepository.save(userToDelete);
					model.addAttribute("operationInfo", "Status konta użytkownika " + userToDelete.getUsername() + " pomyślnie ustawiono na skasowany.");
				} else {
					userToDelete.setDeleted(true);
					userRepository.save(userToDelete);
					model.addAttribute("operationInfo", "Status konta użytkownika " + userToDelete.getUsername() + " pomyślnie ustawiono na aktywny.");
				}
			} else {
				model.addAttribute("operationInfo", "Użytkownik o takim id nie istnieje.");
			}
			
			
			
			
			return "redirect:/panelAdmin";
		} else {
			model.addAttribute("infoError", "Musisz mieć uprawnienia administratora aby wejść do panelu admina!");
			return "userLoginForm";
		}
		
		
	}
	
	
	
	
	
	
	@GetMapping("/adminHardDeleteUserAndData/{id}")
	public String adminHardDeleteUserAndData(Model model, HttpSession session, HttpServletRequest request,
			@PathVariable long id) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(user != null && user.isAdmin()) {
			
			User userToDelete = userRepository.findFirstById(id);
			
			if(userToDelete != null) {
				//make sure user cannot add any more tweets or comments
				userToDelete.setDeleted(true);
				userRepository.save(userToDelete);
				
				commentRepository.customDeleteAllUserComments(id);
				tweetRepository.customDeleteAllUserTweets(id);
				userRepository.customDeleteUser(id);
				model.addAttribute("operationInfo", "Konto użytkownika " + userToDelete.getUsername() + " oraz wszystkie jego tweety i komentarze pomyślnie trwale usunięto z bazy.");
			} else {
				model.addAttribute("operationInfo", "Użytkownik o takim id nie istnieje.");
			}
			
			return "redirect:/panelAdmin";
		} else {
			model.addAttribute("infoError", "Musisz mieć uprawnienia administratora aby wejść do panelu admina!");
			return "userLoginForm";
		}
		
		
	}
	
	
	

	
	
	
	
	@GetMapping("/adminShowUserTweets/{id}")
	public String adminShowUserTweets(Model model, HttpSession session, HttpServletRequest request,
			@PathVariable long id) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(user != null && user.isAdmin()) {
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
			
			User userToViewTweets = userRepository.findFirstById(id);
			
			if(userToViewTweets != null) {
				List<Tweet> userTweets = tweetRepository.findAllByUserIdOrderByCreatedDesc(id);
				model.addAttribute("userTweets", userTweets);
				
				//comment count section
				Map<Integer, Integer> commentCountMap = new HashMap<>();
				for(Tweet tweet: userTweets) {
					commentCountMap.put((int) tweet.getId(), tweetRepository.findCommentCountFromNotDeletedUsersById(tweet.getId()));
				}
				model.addAttribute("commentCountMap", commentCountMap);
				//end of comment count section
				
				model.addAttribute("operationInfo", "Oto wszystkie tweety użytkownika " + userToViewTweets.getUsername() + " (razem: " + tweetRepository.count() + "):");
				
			} else {
				model.addAttribute("operationInfo", "Użytkownik o takim id nie istnieje.");
			}
			
			
			
			
			return "panelAdmin";
		} else {
			model.addAttribute("infoError", "Musisz mieć uprawnienia administratora aby wejść do panelu admina!");
			return "userLoginForm";
		}
		
		
	}
	
	
	
	
	@GetMapping("/adminShowUserComments/{id}")
	public String adminShowUserComments(Model model, HttpSession session, HttpServletRequest request,
			@PathVariable long id) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(user != null && user.isAdmin()) {
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
			
			User userToViewComments = userRepository.findFirstById(id);
			
			if(userToViewComments != null) {
				List<Comment> userComments = commentRepository.findAllByUserIdOrderByCreatedDesc(id);
				model.addAttribute("userComments", userComments);
				
				model.addAttribute("operationInfo", "Oto wszystkie komentarze użytkownika " + userToViewComments.getUsername() + " (razem: " + commentRepository.count() + "):");
				
			} else {
				model.addAttribute("operationInfo", "Użytkownik o takim id nie istnieje.");
			}
			
			
			
			
			return "panelAdmin";
		} else {
			model.addAttribute("infoError", "Musisz mieć uprawnienia administratora aby wejść do panelu admina!");
			return "userLoginForm";
		}
		
		
	}
	
	
	
	
	
	
	@GetMapping("/adminHardDeleteUserTweet/{id}")
	public String adminHardDeleteUserTweet(Model model, HttpSession session, HttpServletRequest request,
			@PathVariable long id) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(user != null && user.isAdmin()) {
			Tweet tweetToDelete = tweetRepository.findFirstById(id);
			if(tweetToDelete != null) {
				tweetRepository.deleteById(id);
				model.addAttribute("operationInfo", "Tweeta należącego do użytkownika " + tweetToDelete.getUser().getUsername() + " oraz wszystkie komentarze do niego skasowano pomyślnie.");
			} else {
				model.addAttribute("operationInfo", "Tweet o takim id nie istnieje.");
			}
			
			
			return "redirect:/panelAdmin";
		} else {
			model.addAttribute("infoError", "Musisz mieć uprawnienia administratora aby wejść do panelu admina!");
			return "userLoginForm";
		}
		
		
	}
	
	
	
	@GetMapping("/adminHardDeleteUserComment/{id}")
	public String adminHardDeleteUserComment(Model model, HttpSession session, HttpServletRequest request,
			@PathVariable long id) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(user != null && user.isAdmin()) {
			Comment commentToDelete = commentRepository.findFirstById(id);
			if(commentToDelete != null) {
				commentRepository.deleteCommentById(id);
				model.addAttribute("operationInfo", "Komentarz należący do użytkownika " + commentToDelete.getUser().getUsername() + " oraz wszystkie komentarze do niego skasowano pomyślnie.");
			} else {
				model.addAttribute("operationInfo", "Komentarz o takim id nie istnieje.");
			}
			
			
			return "redirect:/panelAdmin";
		} else {
			model.addAttribute("infoError", "Musisz mieć uprawnienia administratora aby wejść do panelu admina!");
			return "userLoginForm";
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
