package pl.coderslab.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
	public String adminPanel(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(defaultValue="") String userSearchNameLike,
			@RequestParam(defaultValue="") String userSearchEmailLike,
			@RequestParam(defaultValue="-1") long userSearchId,
			@RequestParam(defaultValue="false") boolean userSearchShowAll) {
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
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
	public String adminPanelBanUser(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@PathVariable long id) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
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
	public String adminToggleDeleteUser(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@PathVariable long id) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
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
	public String adminHardDeleteUserAndData(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@PathVariable long id) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
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
	public String adminShowUserTweets(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@PathVariable long id) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request,  response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
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
	public String adminShowUserComments(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@PathVariable long id) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
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
	public String adminShowUserMessages(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@PathVariable long id) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
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
	public String adminHardDeleteUserTweet(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@PathVariable long id) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
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
	public String adminHardDeleteUserComment(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@PathVariable long id) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
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
	public String adminHardDeleteUserMessage(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@PathVariable long id) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
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
	public String adminDeleteUserImage(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@PathVariable long id) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
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
	public String adminEdit(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(defaultValue="-1") long tweetId,
			@RequestParam(defaultValue="-1") long commentId,
			@RequestParam(defaultValue="-1") long userId) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
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
	public String adminEdit(@Valid Tweet tweet, BindingResult result, Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(defaultValue="-1") long tweetId) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
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
	public String adminEditComment(@Valid Comment comment, BindingResult result, Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(defaultValue="-1") long tweetId) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
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
	public String adminEditUser(@Valid User user, BindingResult result, Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(defaultValue="-1") long tweetId) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@GetMapping("/adminAddTweets")
	public String adminAddTweets(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(defaultValue="-1") long tweetCount) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(user != null && user.isAdmin()) {
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
			model.addAttribute("operationInfo", "Tu edytujemy różne rzeczy jeśli są odpowiednie parametry w linku.");
			
			
			
			HashSet<String> popularWordListHash = new HashSet<>(); //adding to hash set removes duplicates
			
			Connection connect = Jsoup.connect("http://wyborcza.pl/");
			Connection connect2 = Jsoup.connect("http://purepc.pl/");
			Connection connect3 = Jsoup.connect("http://www.newsweek.pl/");
			
			try {
				Document document = connect.get();
				Document document2 = connect2.get();
				Document document3 = connect3.get();
				Elements links = document.select("body");
				Elements links2 = document2.select("body");
				Elements links3 = document3.select("body");
				
				links.addAll(links2);
				links.addAll(links3);
				
				for (Element elem : links) {
					// System.out.println(elem.text());

					String[] split = elem.text().split("\\s|\\?|:|\\.|\"|!|-|,|\\[|\\]|\\(|\\)");

					for (int i = 0; i < split.length; i++) {
						// System.out.println(split[i]);
						if (split[i].length() > 3) {
							popularWordListHash.add(split[i].toLowerCase());
						}

					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			List<String> popularWordList = new ArrayList<>(popularWordListHash); //moving to regular array makes pulling words from it easier
			
			List<Tweet> newTweets = new ArrayList<>();
			List<User> usersWithoutAdmins = new ArrayList<>();
			usersWithoutAdmins = userRepository.findAllWithoutAdmins();
			Random rand = new Random();	
			
			for(int i = 0; i < tweetCount ; i++) {
				
				Tweet randomTweet = new Tweet();
				
				int howManyWordsForThisTweet = rand.nextInt(20 - 2) + 2; //each tweet will have 2 to 20 random words
				
				for(int j = 0 ; j < howManyWordsForThisTweet; j++) {
					int wordNumber = rand.nextInt(popularWordList.size()-1);
					randomTweet.setText(randomTweet.getText() + " " + popularWordList.get(wordNumber));
					
					if(j > 0 && j % 10 == 0) {
						randomTweet.setText(randomTweet.getText() + "\n"); //adds enter if tweet is long
					}
				}
				
				randomTweet.setText(randomTweet.getText().substring(5, randomTweet.getText().length())); //throws away 'null" text from the beginning
				
				randomTweet.setUser(usersWithoutAdmins.get(rand.nextInt(usersWithoutAdmins.size()-1)));
				//System.out.println("Tweet ma usera o numerze " + randomTweet.getUser().getId());
				tweetRepository.save(randomTweet);
				newTweets.add(randomTweet);
				
			
				
			}
			
			System.out.println("Ilość słów w tablicy: " + popularWordList.size());
			System.out.println(popularWordList);
			System.out.println("Ilość tweetów w tablicy: " + newTweets.size());
			System.out.println(newTweets);
	
			model.addAttribute("newGeneratedTweets", newTweets);
			model.addAttribute("operationInfo", "Dodawanie " + tweetCount + " tweetów przebiegło pomyślnie.");
			
			return "panelAdmin";
	
		} else {
			model.addAttribute("infoError", "Musisz mieć uprawnienia administratora aby wejść do panelu admina!");
			return "userLoginForm";
			}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	//adds between 2-17 comments to every tweet that has no comments
	@GetMapping("/adminAddComments")
	public String adminAddComments(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(user != null && user.isAdmin()) {
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
			model.addAttribute("operationInfo", "Tu edytujemy różne rzeczy jeśli są odpowiednie parametry w linku.");
			
			HashSet<String> popularWordListHash = new HashSet<>(); //adding to hash set removes duplicates
			
			Connection connect = Jsoup.connect("http://wyborcza.pl/");
			Connection connect2 = Jsoup.connect("http://purepc.pl/");
			Connection connect3 = Jsoup.connect("http://www.newsweek.pl/");
			
			try {
				Document document = connect.get();
				Document document2 = connect2.get();
				Document document3 = connect3.get();
				Elements links = document.select("body");
				Elements links2 = document2.select("body");
				Elements links3 = document3.select("body");
				
				links.addAll(links2);
				links.addAll(links3);
				
				for (Element elem : links) {
					// System.out.println(elem.text());

					String[] split = elem.text().split("\\s|\\?|:|\\.|\"|!|-|,|\\[|\\]|\\(|\\)");

					for (int i = 0; i < split.length; i++) {
						// System.out.println(split[i]);
						if (split[i].length() > 3) {
							popularWordListHash.add(split[i].toLowerCase());
						}

					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			List<String> popularWordList = new ArrayList<>(popularWordListHash); //moving to regular array makes pulling words from it easier
			List<Tweet> tweetsWithoutComment = new ArrayList<>();
			tweetsWithoutComment = tweetRepository.findAllTweetsWithoutComments();
			
			List<User> usersWithoutAdmins = new ArrayList<>();
			usersWithoutAdmins = userRepository.findAllWithoutAdmins();
			List<Comment> newComments = new ArrayList<>();
			
			Random rand = new Random();
			
			//number of tweets needing comments
			for(int i = 0; i < tweetsWithoutComment.size(); i++) {
				
				//number of comments for a given tweet
				int howManyCommentsForTweet = rand.nextInt(17-2) + 2;
				
				for(int k = 0; k < howManyCommentsForTweet ; k++) {
	
					//number of words for a given comment
					int howManyWordsInComment = rand.nextInt(8-2) + 2; //each comment will have 2 to 8 random words
					Comment newGeneratedComment = new Comment();
					
					for(int j = 0 ; j < howManyWordsInComment ; j++) {
						int wordNumber = rand.nextInt(popularWordList.size()-1);
							newGeneratedComment.setText(newGeneratedComment.getText() + " " + popularWordList.get(wordNumber));
					}
					
					System.out.println("equals wynosi: " + newGeneratedComment.getText().substring(0, 4) );
					if(newGeneratedComment.getText().substring(0, 4).equals("null")) {
						newGeneratedComment.setText(newGeneratedComment.getText().substring(5, newGeneratedComment.getText().length())); //throws away 'null" text from the beginning
					}
					
					if(newGeneratedComment.getText().length() > 60) {
						newGeneratedComment.setText(newGeneratedComment.getText().substring(5, 59));
					}
					
					newGeneratedComment.setUser(usersWithoutAdmins.get(rand.nextInt(usersWithoutAdmins.size()-1)));
					newGeneratedComment.setTweet(tweetsWithoutComment.get(i));
					//System.out.println("Komentarz ma tweeta o numerze " + newGeneratedComment.getTweet().getId() + " i usera o numerze " + newGeneratedComment.getUser().getId());
					commentRepository.save(newGeneratedComment);
					newComments.add(newGeneratedComment);
				
				}
				
			}
			
			System.out.println("Liczba znalezionych tweetów bez komentarzy: " + tweetsWithoutComment.size());
			System.out.println("Ilość słów w tablicy: " + popularWordList.size());
			System.out.println(popularWordList);
			System.out.println("Ilość komentarzy w tablicy: " + newComments.size());
			System.out.println(newComments);
	
			model.addAttribute("newGeneratedComments", newComments);
			model.addAttribute("operationInfo", "Dodano " + newComments.size() +" komentarzy do " + tweetsWithoutComment.size() + " tweetów które ich nie miały.");
			
			return "panelAdmin";
	
		} else {
			model.addAttribute("infoError", "Musisz mieć uprawnienia administratora aby wejść do panelu admina!");
			return "userLoginForm";
			}
		
		
	}
	
	

	
	
	
	
}

		
	
