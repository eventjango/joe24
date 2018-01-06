package kr.co.prot.app.service;

import kr.co.prot.app.dao.UserDAO;
import kr.co.prot.app.dto.UserDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class UserService {

	@Autowired
	private UserDAO dao;
	
	public List<UserDTO> getAllUsers() {
		return dao.selectAll();
	}

	public List<UserDTO> getUser(Map<String, Object> params){

		List<UserDTO> resultList = dao.selectUser(params);
		resultList.removeIf(userDTO -> Objects.isNull(userDTO));

		return Collections.unmodifiableList(resultList);
	}
	
}
