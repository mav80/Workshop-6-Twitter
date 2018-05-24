package pl.coderslab.app;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class zzzzzTestFreeTimeController {

	@RequestMapping("/time")
	@ResponseBody
	public String body() {
		
		String data = new Date().toString();
		int hour = Integer.parseInt(data.substring(11, 13)); //lepiej skorzystać z getDay() i getHours na obiekcie z Date() które od razu zwracają wartości liczbowe: int hour = date.getHours();

		if (data.toLowerCase().contains("sat") || data.toLowerCase().contains("sun")) {
			return "Wolne.";
		} else if (hour >= 9 && hour <= 17) {
			return "Pracuje, nie dzwon";
		} else {
			return "Po pracy";
		}
	}

}
