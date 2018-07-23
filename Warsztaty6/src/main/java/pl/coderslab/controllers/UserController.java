package pl.coderslab.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.util.WebUtils;

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
public class UserController {
	
	@Autowired
	TweetRepository tweetRepository;
	@Autowired
	CommentRepository commentRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	MessageRepository messageRepository;
	
	@GetMapping("/panelUser")
	public String userPanel(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response, @RequestParam(defaultValue="") String showAll) {

		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(user != null) {
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
			
			model.addAttribute("tweets", tweetRepository.findByUserIdOrderByCreatedDesc(user.getId()));
			
			//comment count section
			List<Tweet> tweets = tweetRepository.findAllByUserIdOrderByCreatedDesc(user.getId());
			
			Map<Integer, Integer> commentCountMap = new HashMap<>();
			for(Tweet tweet: tweets) {
				commentCountMap.put((int) tweet.getId(), tweetRepository.findCommentCountFromNotDeletedUsersById(tweet.getId()));
			}
			
			model.addAttribute("commentCountMap", commentCountMap);
			//end of comment count section
			
			
//			if(showAll.equals("true") ) {
//				model.addAttribute("tweets", tweetRepository.findByUserIdOrderByCreatedDesc(user.getId()));
//				model.addAttribute("modelInfo", "Oto wszystkie Twoje tweety:");
//			}
			
			return "panelUser";
		}

		model.addAttribute("infoError", "Aby wejść do swojego panelu musisz się najpierw zalogować!");
		return "userLoginForm";
	}
	
	
	
	
	
	
	@GetMapping("/userView/{id}")
	public String singleUserView(Model model, @PathVariable Long id, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		User userToview = userRepository.findFirstById(id);
		if(userToview != null && !userToview.isDeleted()) {
			model.addAttribute("viewedUser", userToview);
			model.addAttribute("tweets", tweetRepository.findByUserIdOrderByCreatedDesc(id));
		}
		
		//comment count section
		List<Tweet> tweets = tweetRepository.findByUserIdOrderByCreatedDesc(id);
		
		Map<Integer, Integer> commentCountMap = new HashMap<>();
		for(Tweet tweet: tweets) {
			commentCountMap.put((int) tweet.getId(), tweetRepository.findCommentCountFromNotDeletedUsersById(tweet.getId()));
		}
		
		model.addAttribute("commentCountMap", commentCountMap);
		//end of comment count section
		
		if(session.getAttribute("loggedUser") != null) {
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
			
			

			model.addAttribute("message", new Message()); //new message to bind with message adding form
			
		
		}

		return "userViewSingle";
		
	}
	
	
	@PostMapping("/userView/{id}")
	public String singleUserViewPost(@Valid Message message, BindingResult result, Model model, @PathVariable Long id, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		
		message.setId(0); //if not set to 0 it will overwrite existing message
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		
		if(session.getAttribute("loggedUser") != null ) {
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
			
			model.addAttribute("tweets", tweetRepository.findByUserIdOrderByCreatedDesc(id));
			model.addAttribute("viewedUser", userRepository.findFirstById(id));
			
			//comment count section
			List<Tweet> tweets = tweetRepository.findByUserIdOrderByCreatedDesc(id);
			
			Map<Integer, Integer> commentCountMap = new HashMap<>();
			for(Tweet tweet: tweets) {
				commentCountMap.put((int) tweet.getId(), tweetRepository.findCommentCountFromNotDeletedUsersById(tweet.getId()));
			}
			
			model.addAttribute("commentCountMap", commentCountMap);
			//end of comment count section
			
			
			
			
			
			
			if(result.hasErrors()) {
				System.out.println(result.getAllErrors());
				return "userViewSingle";
			}
			
		
			
			
			message.setSender(user);
			User receiver = userRepository.findFirstById(id);
			if(!user.isDeleted() && receiver != null && !receiver.isDeleted()) {
				message.setReceiver(receiver);
			} else {
				model.addAttribute("messageSentStatus", "Błąd wysyłania wiadomości, użytkownik docelowy nie istnieje.");
				return "userViewSingle";
			}
			
			if(receiver.getId() == user.getId()) {
				model.addAttribute("messageSentStatus", "Nie możesz wysłać wiadomości do samego siebie!");
				return "userViewSingle";
			}
			
			messageRepository.save(message);
			
			//clear the message so its content won't reappear after sending
			message.setText(null);
			message.setTopic(null);
			
			model.addAttribute("messageSentStatus", "Wiadomość została poprawnie wysłana.");
			
			return "userViewSingle";
			
		}
		
		model.addAttribute("messageSentStatus", "Błąd wysyłania wiadomości, nadawca nie istnieje.");

		return "userViewSingle";
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	@GetMapping("/deleteUserAccount/{id}")
	@ResponseBody
	public String deleteUserAccount(Model model, @PathVariable Long id, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		
		if(user != null && user.getId() == id) {
			user.setDeleted(true);
			userRepository.save(user);
			
			//cookie section
			Cookie userCookie = WebUtils.getCookie(request, "userCookie");
			if(userCookie!=null) {
				userCookie.setPath("/");
				userCookie.setMaxAge(0);
				response.addCookie(userCookie);
			}
			//end of cookie section
			
			session.setAttribute("loggedUser",  null);
			return "Konto skasowano poprawnie.";
			
		}
		
		return "Wystapil blad, konto nie zostalo usuniete.";

		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@GetMapping("/panelUser/userSettings")
	public String userPanelSettings(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(defaultValue="") String userDeleteImage) {

		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(user != null) {
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
			
			model.addAttribute("user", user);
			
			if(userDeleteImage.equals("true")) {
				user.setUsrImg(null);
				userRepository.save(user);
				model.addAttribute("operationInfo", "Obrazek usunięto.");
			}
			
			return "panelUserSettings";
	
		} else {
			model.addAttribute("infoError", "Aby wejść do swojego panelu musisz się najpierw zalogować!");
			return "userLoginForm";
		}

	}
	
	
	
	//@ResponseBody
	@PostMapping("/panelUser/userSettings")
	public String userPanelSettings(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response, @RequestParam MultipartFile fileUploaded) throws Exception {

		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(user != null && fileUploaded != null && !fileUploaded.isEmpty()) {
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
				
			if(fileUploaded.getSize() > 0 && fileUploaded.getSize() <= 1048576) { //we upload and change the file only if it's longer than 0 and smaller than max allowed size
				
				System.out.println("Nazwa pliku: " + fileUploaded.getOriginalFilename());
				
				String contentType = fileUploaded.getContentType();
				System.out.println("Typ pliku: " + contentType);
				System.out.println(fileUploaded.getSize());
//					System.out.println(aFile.getBytes().toString()); 
//					
				//here we try to display file content
//					ByteArrayInputStream stream = new  ByteArrayInputStream(fileUploaded.getBytes());
//					try {
//						String myString = IOUtils.toString(stream, "UTF-8");
//						System.out.println(myString);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				
				if(contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/gif")) {
					user.setUsrImg(fileUploaded.getBytes());
					userRepository.save(user);
				} else {
					model.addAttribute("message", "Wybrano zły typ pliku - dopuszczalne formaty to JPG, PNG oraz GIF. Obrazka nie zmieniono.");
				}
				
			}
			
			model.addAttribute("user", user); //we will need user's id to display his image
			
			return "fileUploadSuccess";
		}

		return "panelUserSettings";
	}
	
	
	
	
	
	
	
	
	
	
	
	//image display controller
	
	@GetMapping(value = "/imageDisplay")
	  public void showImage(@RequestParam("id") Integer id, HttpServletResponse response, HttpServletRequest request, HttpSession session, Model model) 
	          throws ServletException, IOException{
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(user != null ) {
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			//unread messages counter
			//MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
			
		}
		
		
		User userToDisplayImage = userRepository.findFirstById(id);
		
		if(userToDisplayImage != null) {
			
			byte[] imageData = userToDisplayImage.getUsrImg();
			
			if(imageData != null) {
				response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
			    response.getOutputStream().write(imageData);
			    response.getOutputStream().close();
			}

		}
		

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	//error handler - if the file is too big
	@ControllerAdvice
	public class FileUploadExceptionAdvice {
	      
	    @ExceptionHandler(MaxUploadSizeExceededException.class)
	    public String handleMaxSizeException(MaxUploadSizeExceededException exc, HttpServletRequest request, HttpServletResponse response, Model model, HttpSession session) {
	    	
	    	Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
			User user = (User) session.getAttribute("loggedUser");
			
			if(user != null ) {
				model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
				//unread messages counter
				MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
				
				model.addAttribute("user", user);
			}
	  
	        model.addAttribute("message", "Wybrany plik jest zbyt duży, maksymalna dopuszczona wielkość to 1MB! Obrazka nie zmieniono:");
	        return "fileUploadSuccess";
	    }
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@PostMapping("/panelUser/userChangeProfileData")
	public String userChangeProfileData(@Valid User user, BindingResult result, Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) {

		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User loggedUser = (User) session.getAttribute("loggedUser");
		
		if(loggedUser != null && loggedUser.getId() == user.getId() && loggedUser.isAdmin() == user.isAdmin() && loggedUser.isDeleted() == user.isDeleted() && loggedUser.isEnabled() == user.isEnabled()) {
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
			
			//comment count section
			List<Tweet> tweets = tweetRepository.findAllByUserIdOrderByCreatedDesc(user.getId());
			Map<Integer, Integer> commentCountMap = new HashMap<>();
			for(Tweet tweet: tweets) {
				commentCountMap.put((int) tweet.getId(), tweetRepository.findCommentCountFromNotDeletedUsersById(tweet.getId()));
			}
			model.addAttribute("commentCountMap", commentCountMap);
			//end of comment count section
			

			
			if(result.hasErrors() || userRepository.findFirstByEmail(user.getEmail()) != null || userRepository.findFirstByUsername(user.getUsername()) != null) {
				
				List<User> usersWithSameEmail = userRepository.findAllByEmail(user.getEmail());
				List<User> usersWithSameUsername = userRepository.findAllByUsername(user.getUsername());
								
				boolean emailOk = false;
				boolean usernameOk = false;
				
				if(userRepository.findFirstByEmail(user.getEmail()) != null) {
					
					if(usersWithSameEmail.size() > 1) {
						model.addAttribute("error", "Ten email jest zajęty przez innego użytkownika.");
						//System.out.println("Ten email jest zajęty przez innego użytkownika.");
					} else if (usersWithSameEmail.size() < 1) {
						//System.out.println("Ten email jest wolny.");
						emailOk = true;
					} else if (usersWithSameEmail.get(0).getId() == user.getId()) {
						//System.out.println("Ten email jest zajęty tylko przez użytkownika który dokonuje zmian w profilu.");
						emailOk = true;
					}
					
				} else {
					emailOk = true;
				}
				
				if(userRepository.findFirstByUsername(user.getUsername()) != null) {
					
					if(usersWithSameUsername.size() > 1) {
						model.addAttribute("error", "Ten login jest zajęty przez innego użytkownika.");
						//System.out.println("Ten login jest zajęty przez innego użytkownika.");
					} else if (usersWithSameUsername.size() < 1) {
						//System.out.println("Ten login jest wolny.");
						usernameOk = true;
					} else if (usersWithSameUsername.get(0).getId() == user.getId()) {
						//System.out.println("Ten login jest zajęty tylko przez użytkownika który dokonuje zmian w profilu.");
						usernameOk = true;
					}
					
				} else {
					usernameOk = true;
				}
				
				
				
				if(emailOk && usernameOk && !result.toString().contains("Field error in object 'user' on field 'email'")) {
					userRepository.save(user);
					session.setAttribute("loggedUser", user);
					//System.out.println("zapisano usera");
					model.addAttribute("operationInfo", "Dane zmieniono pomyślnie.");
				} else if (!emailOk && !usernameOk) {
					model.addAttribute("error", "Wybrany  email i login są zajęte przez innego użytkownika.");
				}	else if (!emailOk) {
					model.addAttribute("error", "Ten email jest zajęty przez innego użytkownika.");
				} else if (!usernameOk) {
					model.addAttribute("error", "Ten login jest zajęty przez innego użytkownika.");
				} else if (result.toString().contains("Field error in object 'user' on field 'email'")){
					model.addAttribute("error", "Nieprawidłowy adres email.");
				}
				
				return "panelUserSettings";
			}
			
			userRepository.save(user);
			session.setAttribute("loggedUser", user);
			model.addAttribute("operationInfo", "Dane zmieniono pomyślnie.");
			//System.out.println("zapisano usera bez błędów w formularzu");
	
			return "panelUserSettings";
		}

		model.addAttribute("infoError", "Wystąpił błąd!");
		return "userLoginForm";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@PostMapping("/panelUser/userChangeProfilePassword")
	public String userChangeProfileData(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(defaultValue="") String passwordToChange) {

		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(user != null) {
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
			
			//comment count section
			List<Tweet> tweets = tweetRepository.findAllByUserIdOrderByCreatedDesc(user.getId());
			Map<Integer, Integer> commentCountMap = new HashMap<>();
			for(Tweet tweet: tweets) {
				commentCountMap.put((int) tweet.getId(), tweetRepository.findCommentCountFromNotDeletedUsersById(tweet.getId()));
			}
			model.addAttribute("commentCountMap", commentCountMap);
			//end of comment count section
			
			if(!passwordToChange.isEmpty()) {
				user.setPassword(BCrypt.hashpw(passwordToChange,  BCrypt.gensalt()));
				userRepository.save(user);
				model.addAttribute("operationInfo", "Hasło zmieniono pomyślnie.");
			} else {
				model.addAttribute("error", "Hasło nie może być puste!");
			}
			
			model.addAttribute("user", user);
			return "panelUserSettings";
		}

		model.addAttribute("infoError", "Wystąpił błąd!");
		return "userLoginForm";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@GetMapping("/userHardDeleteUserTweet/{id}")
	public String userHardDeleteUserTweet(Model model, @PathVariable Long id, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		Tweet tweetToDelete = tweetRepository.findFirstById(id);
		
		
		if(user != null && tweetToDelete != null && user.getId() == tweetToDelete.getUser().getId() && HomeController.checkIfTweetIsEditable(tweetToDelete)) {
			tweetRepository.deleteById(id);
			model.addAttribute("operationInfo", "Tweeta należącego do użytkownika " + tweetToDelete.getUser().getUsername() + " oraz wszystkie komentarze do niego skasowano pomyślnie.");
			return "userOperationStatus";
			
		}
		
		model.addAttribute("operationInfo", "Wystąpił błąd, tweet nie został usunięty.");
		if(HomeController.checkIfTweetIsEditable(tweetToDelete) == false ) {
			model.addAttribute("operationInfo", "Czas na edycję (" + HomeController.timeForEditingTweetsAndComments + " minut) upłynął.");
		}
		return "userOperationStatus";

		
	}
	
	@GetMapping("/userHardDeleteUserComment/{id}")
	public String userHardDeleteUserComment(Model model, @PathVariable Long id, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		Comment commentToDelete = commentRepository.findFirstById(id);
		
		
		if(user != null && commentToDelete != null && user.getId() == commentToDelete.getUser().getId() && HomeController.checkIfCommentIsEditable(commentToDelete)) {
			commentRepository.deleteCommentById(id);
			model.addAttribute("operationInfo", "Komentarz należący do użytkownika " + commentToDelete.getUser().getUsername() + " skasowano pomyślnie.");
			return "userOperationStatus";	
		}
		
		model.addAttribute("operationInfo", "Wystąpił błąd, komentarz nie został usunięty.");
		if(HomeController.checkIfCommentIsEditable(commentToDelete) == false ) {
			model.addAttribute("operationInfo", "Czas na edycję (" + HomeController.timeForEditingTweetsAndComments + " minut) upłynął.");
		}
		return "userOperationStatus";

		
	}
	
	
	
	
	
	
	
	
	@GetMapping("/userEdit")
	public String adminEdit(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(defaultValue="-1") long tweetId,
			@RequestParam(defaultValue="-1") long commentId) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(user != null) {
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
			model.addAttribute("operationInfo", "Tu edytujemy różne rzeczy jeśli są odpowiednie parametry w linku.");
			
			if(tweetId > 0) {
				Tweet tweetToEdit = tweetRepository.findFirstById(tweetId);
				if(tweetToEdit != null && user.getId() == tweetToEdit.getUser().getId() && HomeController.checkIfTweetIsEditable(tweetToEdit)){
					model.addAttribute("tweet", tweetToEdit);
					model.addAttribute("operationInfo", "Edycja tweeta należącego do użytkownika " + tweetToEdit.getUser().getUsername() + ":");
				}  else {
					model.addAttribute("operationInfo", "Wystąpił błąd, edycja nieudana.");
					if(HomeController.checkIfTweetIsEditable(tweetToEdit) == false ) {
						model.addAttribute("operationInfo", "Czas na edycję (" + HomeController.timeForEditingTweetsAndComments + " minut) upłynął.");
					}
					return "userOperationStatus";
					}
			} 
			
			if(commentId > 0) {
				Comment commentToEdit = commentRepository.findFirstById(commentId);
				if(commentToEdit != null && user.getId() == commentToEdit.getUser().getId() && HomeController.checkIfCommentIsEditable(commentToEdit)){
					model.addAttribute("comment", commentToEdit);
					model.addAttribute("operationInfo", "Edycja komentarza należącego do użytkownika " + commentToEdit.getUser().getUsername() + ":");
				}  else {
					model.addAttribute("operationInfo", "Wystąpił błąd, edycja nieudana.");
					if(HomeController.checkIfCommentIsEditable(commentToEdit) == false ) {
						model.addAttribute("operationInfo", "Czas na edycję (" + HomeController.timeForEditingTweetsAndComments + " minut) upłynął.");
					}
					return "userOperationStatus";
					}
			} 
					
			return "userEdit";
	
		} else {
			model.addAttribute("operationInfo", "Wystąpił błąd, edycja nieudana.");
			return "userOperationStatus";
			}
	}
	
	
	
	
	
	
	@PostMapping("/userEdit")
	public String adminEdit(@Valid Tweet tweet, BindingResult result, Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(defaultValue="-1") long tweetId) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(user != null && tweet.getUser().getId() == user.getId() && HomeController.checkIfTweetIsEditable(tweet)) {
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
			model.addAttribute("operationInfo", "Tu edytujemy różne rzeczy jeśli są odpowiednie parametry w linku.");

			
			if (result.hasErrors())
			{
				return "userEdit";
			}

			tweetRepository.save(tweet);
			//model.addAttribute("tweet", tweetRepository.findFirstById(tweet.getId()));
			model.addAttribute("operationInfo", "Edycja tweeta przebiegła pomyślnie.");
			return "userOperationStatus";
	
		} else {
			model.addAttribute("operationInfo", "Wystąpił błąd, edycja nieudana.");
			if(HomeController.checkIfTweetIsEditable(tweet) == false ) {
				model.addAttribute("operationInfo", "Czas na edycję (" + HomeController.timeForEditingTweetsAndComments + " minut) upłynął.");
			}
			return "userOperationStatus";
			}
	}
	
	
	
	
	@PostMapping("/userEditComment")
	public String adminEditComment(@Valid Comment comment, BindingResult result, Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(defaultValue="-1") long tweetId) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(user != null && comment.getUser().getId() == user.getId() && HomeController.checkIfCommentIsEditable(comment)) {
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
			model.addAttribute("operationInfo", "Tu edytujemy różne rzeczy jeśli są odpowiednie parametry w linku.");

			
			if (result.hasErrors())
			{
				return "userEditComment";
			}

			commentRepository.save(comment);
			//model.addAttribute("comment", commentRepository.findFirstById(comment.getId()));
			model.addAttribute("operationInfo", "Edycja komentarza przebiegła pomyślnie.");
			return "userOperationStatus";
	
		} else {
			model.addAttribute("operationInfo", "Wystąpił błąd, edycja nieudana.");
			if(HomeController.checkIfCommentIsEditable(comment) == false ) {
				model.addAttribute("operationInfo", "Czas na edycję (" + HomeController.timeForEditingTweetsAndComments + " minut) upłynął.");
			}
			return "userOperationStatus";
			}
	}
	
	
	
	
	
	

}
