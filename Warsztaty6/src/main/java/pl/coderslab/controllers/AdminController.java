package pl.coderslab.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.coderslab.app.Cookies;
import pl.coderslab.app.MessageUtils;
import pl.coderslab.entities.Comment;
import pl.coderslab.entities.Message;
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
			}
			if(!userSearchEmailLike.isEmpty()) {
				model.addAttribute("users", userRepository.findByEmailLike(userSearchEmailLike));
			}
			if(userSearchId > 0) {
				//create user list from single user for iterator in adminPanel view
				List<User> userList = new ArrayList<>();
				userList.add(userRepository.findFirstById(userSearchId));
				model.addAttribute("users", userList);
			}
			if(userSearchShowAll) {
				model.addAttribute("users", userRepository.findByEmailLike(userSearchEmailLike));
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
					model.addAttribute("operationInfo", "Status konta użytkownika " + userToDelete.getUsername() + " pomyślnie ustawiono na aktywny - pamiętaj aby odbanować użytkownika w razie potrzeby.");
				} else {
					userToDelete.setDeleted(true);
					if(userToDelete.isEnabled()) {
						userToDelete.setEnabled(false);
					}
					userRepository.save(userToDelete);
					model.addAttribute("operationInfo", "Status konta użytkownika " + userToDelete.getUsername() + " pomyślnie ustawiono na skasowany oraz na zbanowany aby nie pokazywać jego tweetów/komentarzy.");
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
				
				model.addAttribute("operationInfo", "Oto wszystkie tweety użytkownika " + userToViewTweets.getUsername() + " (razem: " + userTweets.size() + "):");
				
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
				
				model.addAttribute("operationInfo", "Oto wszystkie komentarze użytkownika " + userToViewComments.getUsername() + " (razem: " + userComments.size() + "):");
				
			} else {
				model.addAttribute("operationInfo", "Użytkownik o takim id nie istnieje.");
			}
			
			
			
			
			return "panelAdmin";
		} else {
			model.addAttribute("infoError", "Musisz mieć uprawnienia administratora aby wejść do panelu admina!");
			return "userLoginForm";
		}
		
		
	}
	
	
	
	@GetMapping("/adminShowUserMessages/{id}")
	public String adminShowUserMessages(Model model, HttpSession session, HttpServletRequest request,
			@PathVariable long id) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(user != null && user.isAdmin()) {
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
			
			User userToViewMessages = userRepository.findFirstById(id);
			
			if(userToViewMessages != null) {
				model.addAttribute("messages",messageRepository.findAllByReceiverIdOrderByCreatedDesc(userToViewMessages.getId()));
				model.addAttribute("messagesSent",messageRepository.findAllBySenderIdOrderByCreatedDesc(userToViewMessages.getId()));
				model.addAttribute("messagesUser",userToViewMessages);
				
				model.addAttribute("operationInfo", "Oto wszystkie wiadomości użytkownika " + userToViewMessages.getUsername() + ":");
				
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
	
	
	@GetMapping("/adminHardDeleteUserMessage/{id}")
	public String adminHardDeleteUserMessage(Model model, HttpSession session, HttpServletRequest request,
			@PathVariable long id) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(user != null && user.isAdmin()) {
			Message messageToDelete = messageRepository.findFirstById(id);
			if(messageToDelete != null) {
				messageRepository.delete(messageToDelete);
				model.addAttribute("operationInfo", "Wiadomość do użytkownika " + messageToDelete.getReceiver().getUsername() + " skasowano pomyślnie.");
			} else {
				model.addAttribute("operationInfo", "Wiadomość o takim id nie istnieje.");
			}
			
			
			return "redirect:/panelAdmin";
			
		} else {
			model.addAttribute("infoError", "Musisz mieć uprawnienia administratora aby wejść do panelu admina!");
			return "userLoginForm";
		}
		
	}
	
	
	@GetMapping("/adminDeleteUserImage/{id}")
	public String adminDeleteUserImage(Model model, HttpSession session, HttpServletRequest request,
			@PathVariable long id) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(user != null && user.isAdmin()) {
			User userToDeleteImage = userRepository.findFirstById(id);
			if(userToDeleteImage != null) {
				userToDeleteImage.setUsrImg(null);
				userRepository.save(userToDeleteImage);
				model.addAttribute("operationInfo", "Obrazek należący do użytkownika " + userToDeleteImage.getUsername() + " usunięto pomyślnie.");
			} else {
				model.addAttribute("operationInfo", "Użytkownik o takim id nie istnieje.");
			}
			
			
			return "redirect:/panelAdmin";
			
		} else {
			model.addAttribute("infoError", "Musisz mieć uprawnienia administratora aby wejść do panelu admina!");
			return "userLoginForm";
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	@GetMapping("/adminEdit")
	public String adminEdit(Model model, HttpSession session, HttpServletRequest request,
			@RequestParam(defaultValue="-1") long tweetId,
			@RequestParam(defaultValue="-1") long commentId,
			@RequestParam(defaultValue="-1") long userId) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(user != null && user.isAdmin()) {
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
			model.addAttribute("operationInfo", "Tu edytujemy różne rzeczy jeśli są odpowiednie parametry w linku.");
			
			if(tweetId > 0) {
				Tweet tweet = tweetRepository.findFirstById(tweetId);
				if(tweet != null){
					model.addAttribute("tweet", tweet);
					model.addAttribute("operationInfo", "Edycja tweeta należącego do użytkownika " + tweet.getUser().getUsername() + ":");
				}  else {
					model.addAttribute("operationInfo", "Tweet o takim id nie istnieje.");
					}
			} 
			
			if(commentId > 0) {
				Comment comment = commentRepository.findFirstById(commentId);
				if(comment != null){
					model.addAttribute("comment", comment);
					model.addAttribute("operationInfo", "Edycja komentarza należącego do użytkownika " + comment.getUser().getUsername() + ":");
				}  else {
					model.addAttribute("operationInfo", "Komentarz o takim id nie istnieje.");
					}
			} 
			
			if(userId > 0) {
				User userToEdit = userRepository.findFirstById(userId);
				if(userToEdit != null){
					model.addAttribute("user", userToEdit);
					model.addAttribute("operationInfo", "Edycja użytkownika " + userToEdit.getUsername() + ":");
				}  else {
					model.addAttribute("operationInfo", "Użytkownik o takim id nie istnieje.");
					}
			}
			

			
			
			return "panelAdminEdit";
	
		} else {
			model.addAttribute("infoError", "Musisz mieć uprawnienia administratora aby wejść do panelu admina!");
			return "userLoginForm";
			}
		
		
	}

	
	
	
	
	@PostMapping("/adminEdit")
	public String adminEdit(@Valid Tweet tweet, BindingResult result, Model model, HttpSession session, HttpServletRequest request,
			@RequestParam(defaultValue="-1") long tweetId) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(user != null && user.isAdmin()) {
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
			model.addAttribute("operationInfo", "Tu edytujemy różne rzeczy jeśli są odpowiednie parametry w linku.");

			
			if (result.hasErrors())
			{
				return "panelAdminEdit";
			}

			tweetRepository.save(tweet);
			model.addAttribute("tweet", tweetRepository.findFirstById(tweet.getId()));
			model.addAttribute("operationInfo", "Edycja tweeta przebiegła pomyślnie.");
			return "panelAdminEdit";
	
		} else {
			model.addAttribute("infoError", "Musisz mieć uprawnienia administratora aby wejść do panelu admina!");
			return "userLoginForm";
			}
		
		
	}
	
	
	
	
	
	
	
	@PostMapping("/adminEditComment")
	public String adminEditComment(@Valid Comment comment, BindingResult result, Model model, HttpSession session, HttpServletRequest request,
			@RequestParam(defaultValue="-1") long tweetId) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(user != null && user.isAdmin()) {
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
			model.addAttribute("operationInfo", "Tu edytujemy różne rzeczy jeśli są odpowiednie parametry w linku.");

			
			if (result.hasErrors())
			{
				return "panelAdminEdit";
			}

			commentRepository.save(comment);
			model.addAttribute("comment", commentRepository.findFirstById(comment.getId()));
			model.addAttribute("operationInfo", "Edycja komentarza przebiegła pomyślnie.");
			return "panelAdminEdit";
	
		} else {
			model.addAttribute("infoError", "Musisz mieć uprawnienia administratora aby wejść do panelu admina!");
			return "userLoginForm";
			}
		
		
	}
	
	
	@PostMapping("/adminEditUser")
	public String adminEditUser(@Valid User user, BindingResult result, Model model, HttpSession session, HttpServletRequest request,
			@RequestParam(defaultValue="-1") long tweetId) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User loggedUser = (User) session.getAttribute("loggedUser");
		
		if(loggedUser != null && loggedUser.isAdmin()) {
			model.addAttribute("info", "Jesteś zalogowany jako " + loggedUser.getUsername());
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, loggedUser, messageRepository);
			model.addAttribute("operationInfo", "Tu edytujemy różne rzeczy jeśli są odpowiednie parametry w linku.");

			
			if (result.hasErrors())
			{
				return "panelAdminEdit";
			}

			userRepository.save(user);
			model.addAttribute("user", userRepository.findFirstById(user.getId()));
			model.addAttribute("operationInfo", "Edycja użytkownika przebiegła pomyślnie.");
			return "panelAdminEdit";
	
		} else {
			model.addAttribute("infoError", "Musisz mieć uprawnienia administratora aby wejść do panelu admina!");
			return "userLoginForm";
			}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

		
	
