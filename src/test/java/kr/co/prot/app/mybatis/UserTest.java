package kr.co.prot.app.mybatis;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import kr.co.prot.app.dao.UserDAO;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/hikari-context.xml"})
public class UserTest {

    @Autowired
    private SqlSessionTemplate session;

    @Test
    public void selectAll(){

        UserDAO userDAO = new UserDAO();
        userDAO.setSqlSessionTemplate(session);

        assertTrue(userDAO.selectAll().size() == 2);
    }

    @Test
    public void selectOne(){

        UserDAO userDAO = new UserDAO();
        userDAO.setSqlSessionTemplate(session);

        Map<String, Object> params = new HashMap<>();
        params.put("email", "joe24@gmail.com");

        assertEquals(userDAO.selectUser(params).size(), 1);
    }

}
