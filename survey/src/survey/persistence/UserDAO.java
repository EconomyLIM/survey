package survey.persistence;

import java.sql.SQLException;

import survey.domain.UserDTO;

public interface UserDAO {

	// 1. 회원가입
	int insertUser (UserDTO dto) throws SQLException;

}
