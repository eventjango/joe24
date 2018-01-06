package kr.co.prot.app.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.prot.app.dto.UserDTO;

@Repository
public class UserDAO {

	private static final String namespace = "kr.co.prot.app.dao.UserDAO";
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	public List<UserDTO> selectAll() {
		return sqlSessionTemplate.selectList(namespace + ".selectAll");
	}

	public List<UserDTO> selectUser(Map<String, Object> params){

		return sqlSessionTemplate.selectList(namespace + ".selectUser", params);
	}

	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate){
		this.sqlSessionTemplate = sqlSessionTemplate;
	}
}
