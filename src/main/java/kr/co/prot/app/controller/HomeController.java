package kr.co.prot.app.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.co.prot.app.dto.UserDTO;
import kr.co.prot.app.service.UserService;

@Controller
public class HomeController {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value= {"/", "/index"}, method = RequestMethod.GET)
	public String index(Model model) {
		try {
			
			List<UserDTO> users = userService.getAllUsers();
			model.addAttribute("users", users);
			
		}catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		return "main";
	}
	
	
}
