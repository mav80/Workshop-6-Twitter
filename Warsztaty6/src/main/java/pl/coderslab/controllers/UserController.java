package pl.coderslab.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
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
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import pl.coderslab.app.Cookies;
import pl.coderslab.app.MessageUtils;
import pl.coderslab.entities.Comment;
import pl.coderslab.entities.Message;
import pl.coderslab.entities.Tweet;
import pl.coderslab.entities.User;
import pl.coderslab.repositories.MessageRepository;
import pl.coderslab.repositories.TweetRepository;
import pl.coderslab.repositories.UserRepository;

@Controller
public class UserController {
	
	@Autowired
	TweetRepository tweetRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	MessageRepository messageRepository;
	
	@GetMapping("/panelUser")
	public String userPanel(Model model, HttpSession session, HttpServletRequest request, @RequestParam(defaultValue="") String showAll) {

		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(user != null) {
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
			
			model.addAttribute("tweets", tweetRepository.findByUserIdOrderByCreatedDesc(user.getId()));
			
			//comment count section - we get 
			List<Tweet> tweets = tweetRepository.findAllByUserIdOrderByCreatedDesc(user.getId());
			
			Map<Integer, Integer> commentCountMap = new HashMap<>();
			for(Tweet tweet: tweets) {
				commentCountMap.put((int) tweet.getId(), tweetRepository.findCommentCountById(tweet.getId()));
			}
			
			model.addAttribute("commentCountMap", commentCountMap);
			//end of comment count section
			
			
//			if(showAll.equals("true") ) {
//				model.addAttribute("tweets", tweetRepository.findByUserIdOrderByCreatedDesc(user.getId()));
//				model.addAttribute("modelInfo", "Oto wszystkie Twoje tweety:");
//			}
			
			
		}

		return "panelUser";
	}
	
	
	
	
	
	
	@GetMapping("/userView/{id}")
	public String singleUserView(Model model, @PathVariable Long id, HttpSession session, HttpServletRequest request) {
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(session.getAttribute("loggedUser") != null ) {
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
			
			model.addAttribute("tweets", tweetRepository.findByUserIdOrderByCreatedDesc(id));
			model.addAttribute("viewedUser", userRepository.findFirstById(id));
			model.addAttribute("message", new Message()); //new message to bind with message adding form
			
			//comment count section
			List<Tweet> tweets = tweetRepository.findByUserIdOrderByCreatedDesc(id);
			
			Map<Integer, Integer> commentCountMap = new HashMap<>();
			for(Tweet tweet: tweets) {
				commentCountMap.put((int) tweet.getId(), tweetRepository.findCommentCountById(tweet.getId()));
			}
			
			model.addAttribute("commentCountMap", commentCountMap);
			//end of comment count section
		}

		return "userViewSingle";
		
	}
	
	
	@PostMapping("/userView/{id}")
	public String singleUserViewPost(@Valid Message message, BindingResult result, Model model, @PathVariable Long id, HttpSession session, HttpServletRequest request) {
		
		message.setId(0); //if not set to 0 it will overwrite existing message
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		
		if(session.getAttribute("loggedUser") != null ) {
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			
			model.addAttribute("tweets", tweetRepository.findByUserIdOrderByCreatedDesc(id));
			model.addAttribute("viewedUser", userRepository.findFirstById(id));
			
			//comment count section
			List<Tweet> tweets = tweetRepository.findByUserIdOrderByCreatedDesc(id);
			
			Map<Integer, Integer> commentCountMap = new HashMap<>();
			for(Tweet tweet: tweets) {
				commentCountMap.put((int) tweet.getId(), tweetRepository.findCommentCountById(tweet.getId()));
			}
			
			model.addAttribute("commentCountMap", commentCountMap);
			//end of comment count section
			
			
			
			
			
			
			if(result.hasErrors()) {
				System.out.println(result.getAllErrors());
				return "userViewSingle";
			}
			
		
			
			
			message.setSender(user);
			User receiver = userRepository.findFirstById(id);
			if(receiver != null) {
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
	
	
	
	
	
	
	
	
	
	
	
	@GetMapping("/panelUser/userSettings")
	public String userPanelSettings(Model model, HttpSession session, HttpServletRequest request) {

		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(user != null) {
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
			
			model.addAttribute("user", user);
	
		}

		return "panelUserSettings";
	}
	
	
	
	//@ResponseBody
	@PostMapping("/panelUser/userSettings")
	public String userPanelSettings(Model model, HttpSession session, HttpServletRequest request, @RequestParam CommonsMultipartFile[] fileUpload) throws Exception {

		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(user != null && fileUpload != null && fileUpload.length > 0) {
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
			
			
			
			//model.addAttribute("user", user);
			
			System.out.println(fileUpload.length);
			
			for (CommonsMultipartFile aFile : fileUpload){
				
				if(aFile.getSize() > 0 && aFile.getSize() <= 1048576) { //we upload and change the file only if it's longer than 0 and smaller than max allowed size
					
					System.out.println("Nazwa pliku: " + aFile.getOriginalFilename());
					
					String contentType = aFile.getContentType();
					System.out.println("Typ pliku: " + contentType);
					System.out.println(aFile.getSize());
//					System.out.println(aFile.getBytes().toString()); 
//					
					//here we try to display file content
//					ByteArrayInputStream stream = new  ByteArrayInputStream(aFile.getBytes());
//					try {
//						String myString = IOUtils.toString(stream, "UTF-8");
//						System.out.println(myString);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					
					
					if(contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/gif")) {
						user.setUsrImg(aFile.getBytes());
						userRepository.save(user);
					} else {
						model.addAttribute("message", "Wybrano zły typ pliku - dopuszczalne formaty to JPG, PNG orasz GIF. Obrazka nie zmieniono.");
					}
					

				}
    			
    			break; //we need only first file from a collection (in case many are uploaded)
    			
    			
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
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
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
	    	
	    	Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
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
	
	
	

}
