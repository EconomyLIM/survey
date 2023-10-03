package survey.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import survey.domain.UserDTO;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserDAOImpl implements UserDAO{

	private Connection conn = null;

	//======================================================================== 
	@Override
	public int insertUser(UserDTO dto) throws SQLException {

		int rowCnt = 0;

		String sql = " INSERT INTO user_info "
				+ " VALUES (?, ?, ?, ?, ?) ";

		PreparedStatement pstmt = null;
		pstmt = conn.prepareStatement(sql);

		pstmt.setString(1, dto.getUserId());
		pstmt.setString(2, dto.getPassword());
		pstmt.setString(3, dto.getName());
		pstmt.setString(4, dto.getEmail());
		pstmt.setString(5, dto.getRole());

		rowCnt = pstmt.executeUpdate();

		pstmt.close();

		System.out.println(" <<<유저 회원가입 성공>>> ");
		return rowCnt;
	}
	//========================================================================
}
