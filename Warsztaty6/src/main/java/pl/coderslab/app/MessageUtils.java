package pl.coderslab.app;

import org.springframework.ui.Model;

import pl.coderslab.entities.User;
import pl.coderslab.repositories.MessageRepository;

public class MessageUtils {
	
	public static void countUnreadMessagesAndSetInfoIfAny(Model model, User user, MessageRepository messageRepository) {
		if(user != null && messageRepository.howManyUnreadMessagesByUserId(user.getId()) > 0) {
			model.addAttribute("unreadMessagesNumber", messageRepository.howManyUnreadMessagesByUserId(user.getId()));
		}
	}

}
