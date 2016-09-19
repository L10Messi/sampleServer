package zgbzhyjy.sampsystem.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SessionInvalidAction {
	
	@RequestMapping(value = "/showSessionInvalid")
	public String showSessionInvalid() {
		return "/showSessionInvalid";
	}
	
}
