package survey.test;

import java.sql.Connection;

import org.junit.jupiter.api.Test;

import com.util.DBConn;

import survey.domain.UserDTO;
import survey.persistence.UserDAO;
import survey.persistence.UserDAOImpl;
import survey.service.UserService;

public class UserServiceTest {

	@Test
	void insertUserTest() {
		Connection conn = DBConn.getConnection();
		UserDAO dao = new UserDAOImpl(conn);
		UserService service = new UserService(dao);

		UserDTO dto = UserDTO.builder()
				.userId("test1")
				.password("testpwd123")
				.name("name")
				.email("test@test.com")
				.role("일반")
				.build();

		int rowCnt = service.insertUser(dto);

		if (rowCnt == 1 ) {
			System.out.println("> 회원가입 완료");
			return;
		}

	}
}
