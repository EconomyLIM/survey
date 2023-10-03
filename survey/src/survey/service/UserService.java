package survey.service;

import java.sql.SQLException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import survey.domain.UserDTO;
import survey.persistence.UserDAO;

@Data
@AllArgsConstructor
@Builder
public class UserService {

	private UserDAO dao = null;
	
	public int insertUser(UserDTO dto) {
		int rowCnt = 0;
		System.out.println(" 회원가입 - 로그기록 ");
		
		try {
			rowCnt = this.dao.insertUser(dto);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rowCnt;
	}
}
