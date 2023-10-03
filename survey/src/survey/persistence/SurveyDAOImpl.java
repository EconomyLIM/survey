package survey.persistence;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import survey.domain.SurveyDTO;
import survey.domain.SurveyOptionDTO;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class SurveyDAOImpl implements SurveyDAO{

	private Connection conn = null;

	//========================================================================
	@Override
	public int insertSurvey(SurveyDTO dto) throws SQLException {
		int rowCnt = 0;
		String sql = " INSERT INTO survey (survey_id, user_id, end_date, title) "
				+ " VALUES (survey_seq.nextval, 'teamleader' ,TO_DATE( ? ,'YYYY-MM-DD hh24:mi:ss') ,?) ";

		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setDate(1, dto.getEndDate());
		pstmt.setString(2, dto.getTitle());

		rowCnt = pstmt.executeUpdate();

		pstmt.close();


		return rowCnt;
	}

	//========================================================================
	@Override
	public ArrayList<SurveyDTO> select() throws SQLException {
		String sql = " SELECT survey_id, user_id, start_date, end_date, title, state, survey_allcnt, regdate "
				+ " FROM survey "
				+ " ORDER BY survey_id";

		PreparedStatement pstmt = null;
		ArrayList<SurveyDTO> list = null;
		ResultSet rs = null;
		SurveyDTO vo = null;

		int surveyId;
		String userId;
		Date startDate;
		Date endDate;
		String title;
		String state;
		int surveyAllcnt;
		Date regdate;

		pstmt = conn.prepareStatement(sql);
		rs = pstmt.executeQuery();

		if (rs.next()) {
			list = new ArrayList<>();
			do {

				surveyId = rs.getInt("survey_id");
				userId = rs.getString("user_id");
				startDate = rs.getDate("start_date");
				endDate = rs.getDate("end_date");
				title = rs.getString("title");
				state = rs.getString("state");
				surveyAllcnt = rs.getInt("survey_allcnt");
				regdate = rs.getDate("regdate");

				vo = SurveyDTO.builder()
						.surveyId(surveyId)
						.userId(userId)
						.startDate(startDate)
						.endDate(endDate)
						.title(title)
						.state(state)
						.surveyAllcnt(surveyAllcnt)
						.regdate(regdate)
						.build();

				list.add(vo);
			} while (rs.next());
		} // if

		pstmt.close();
		return list;

	} // select


	//========================================================================
	@Override
	public int increaseOptionCnt(int seq) throws SQLException {
		String sql = " UPDATE survey_option "
				+ " SET option_cnt = option_cnt + 1 "
				+ " WHERE content_id = ?";

		PreparedStatement pstmt = this.conn.prepareStatement(sql);
		pstmt.setInt(1,seq);
		int rowCnt= pstmt.executeUpdate();


		pstmt.close();
		return rowCnt;

	} //increaseOptionCnt

	//========================================================================
	@Override
	public int delete(int seq) throws SQLException {
		int rowCnt = 0;
		String sql = " DELETE FROM survey"
				+ " WHERE survey_id = ? ";

		PreparedStatement pstmt = this.conn.prepareStatement(sql);
		pstmt.setInt(1, seq);
		rowCnt = pstmt.executeUpdate();

		pstmt.close();

		return rowCnt;
	} // delete

	//========================================================================
	@Override
	public int getTotalSurvey() throws SQLException {
		int totalRecords = 0;
		String sql = " SELECT COUNT(*) FROM survey ";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		pstmt = this.conn.prepareStatement(sql);
		rs = pstmt.executeQuery();
		if (rs.next()) {
			totalRecords = rs.getInt(1);
		} //if
		
		rs.close();
		pstmt.close();
		
		return totalRecords;
	}

	//========================================================================
	@Override
	public int getTotalPages(int numberPerPage) throws SQLException {
		int totalpages = 0;
		String sql = " SELECT CEIL(COUNT(*)/?) FROM survey ";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		pstmt = this.conn.prepareStatement(sql);
		pstmt.setInt(1, numberPerPage);
		rs = pstmt.executeQuery();
		if (rs.next()) {
			totalpages = rs.getInt(1);
		}
		
		rs.close();
		pstmt.close();
		
		return totalpages;
	}

	//========================================================================
	@Override
	public ArrayList<SurveyOptionDTO> select(int seq) throws SQLException {

		String sql = " SELECT survey_id, content_id, option_content, option_cnt "
				+ " FROM survey_option "
				+ " WHERE survey_id = ? ";

		ArrayList<SurveyOptionDTO> list = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null; 
		SurveyOptionDTO vo = null;

		pstmt = this.conn.prepareStatement(sql);
		pstmt.setInt(1, seq);
		rs = pstmt.executeQuery();

		int surveyId;
		int contentId;
		String optionContent;
		int optionselCnt;

		if (rs.next()) {
			list = new ArrayList<>();
			do {
				surveyId = rs.getInt("survey_id");
				contentId = rs.getInt("content_id");
				optionContent = rs.getString("option_content");
				optionselCnt =rs.getInt("option_cnt");

				vo = new SurveyOptionDTO().builder()
						.surveyId(surveyId)
						.contentId(contentId)
						.optionContent(optionContent)
						.optionCnt(optionselCnt)
						.build();
				list.add(vo);

			} while (rs.next());
		} // if

		pstmt.close();

		return list;
	} // select

	//========================================================================
	@Override
	public int insertSurveyOption(SurveyOptionDTO dto) throws SQLException {
		int rowCnt = 0;
		String sql = " INSERT INTO survey_option (survey_id, content_id, option_content)"
				+ " VALUES( (SELECT max(survey_id) FROM survey) ,survey_vote_seq.nextval, ?)";

		PreparedStatement pstmt = this.conn.prepareStatement(sql);
		pstmt.setString(1, dto.getOptionContent());

		rowCnt =  pstmt.executeUpdate();

		pstmt.close();


		return rowCnt;
	} //insertSurveyOption

	//========================================================================
	@Override
	public int updateSurvey(SurveyDTO dto) throws SQLException {
		int rowCnt = 0;
		String sql = " UPDATE survey "
				+ " SET end_date = ? "
				+ " , title = ? "
				+ " WHERE survey_id = ? ";
		PreparedStatement pstmt = this.conn.prepareStatement(sql);
		pstmt.setDate(1, dto.getEndDate());
		pstmt.setString(2, dto.getTitle());
		pstmt.setInt(3, dto.getSurveyId());

		rowCnt = pstmt.executeUpdate();

		pstmt.close();

		return rowCnt;
	}
	//========================================================================
	@Override
	public SurveyDTO viewSurvey(int id) throws SQLException {
		String sql = " SELECT survey_id, user_id, start_date, end_date, title, state, survey_allcnt, regdate "
				+ " FROM survey "
				+ " WHERE survey_id = ? ";

		PreparedStatement pstmt = this.conn.prepareStatement(sql);
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		SurveyDTO dto = null;
		if (rs.next()) {
			dto = SurveyDTO.builder()
					.surveyId(id)
					.userId(rs.getString("user_id"))
					.startDate(rs.getDate("start_date"))
					.endDate(rs.getDate("end_date"))
					.title(rs.getString("title"))
					.state(rs.getString("state"))
					.surveyAllcnt(rs.getInt("survey_allcnt"))
					.regdate(rs.getDate("regdate")).build();
		}
		rs.close();
		pstmt.close();

		return dto;
	} // viewSurvey

	//========================================================================
	@Override
	public ArrayList<SurveyDTO> surveySearch(int searchCondition, String searchWord) throws SQLException {

		String sql = " SELECT survey_id, user_id, start_date, end_date, title, state, survey_allcnt, regdate "
				+ "FROM survey ";

		switch (searchCondition) {
		case 1: // 설문조사 id로 검색
			sql += " WHERE survey_id = ? ";
			break;
		case 2: // 제목으로 검색
			sql += " WHERE REGEXP_LIKE(title, ? , 'i') ";
			break;
		
		} //swtich

		PreparedStatement pstmt = this.conn.prepareStatement(sql);
		ResultSet rs = null;
		SurveyDTO dto = null;
		ArrayList<SurveyDTO> list = null;
		
		int surveyId;
		String userId;
		Date startDate;
		Date endDate;
		String title;
		String state;
		int surveyAllcnt;
		Date regdate;
		
		if (searchCondition == 1) {
			pstmt.setInt(1, Integer.parseInt(searchWord));
		} else if (searchCondition == 2) {
			pstmt.setString(1, searchWord);
		} // if
		
		rs = pstmt.executeQuery();
		if (rs.next()) {
			list = new ArrayList<>();
			do {
				dto = SurveyDTO.builder()
						.surveyId(rs.getInt("survey_id"))
						.userId(rs.getString("user_id"))
						.startDate(rs.getDate("start_date"))
						.endDate(rs.getDate("end_date"))
						.title(rs.getString("title"))
						.state(rs.getString("state"))
						.surveyAllcnt(rs.getInt("survey_allcnt"))
						.regdate(rs.getDate("regdate"))
						.build();
				
				list.add(dto);
			} while (rs.next());
		}
		
		pstmt.close();
		rs.close();
		
		return list;
	}

	
	//========================================================================
	@Override
	public ArrayList<SurveyDTO> surveySelect(int currentPage, int numberPerPage) throws SQLException {
		int begin = (currentPage -1)*numberPerPage +1 ;
		int end = begin + numberPerPage -1;
		
		String sql = 
				" SELECT * "
				+ "FROM ( "
				+ "        SELECT ROWNUM no, t.* "
				+ "        FROM (  "
				+ "                SELECT survey_id, user_id, start_date, end_date, title, state, survey_allcnt, regdate   "
				+ "                FROM survey "
				+ "                ORDER BY survey_id "
				+ "        ) t "
				+ ")  b "
				+ "WHERE b.no BETWEEN ? AND ? ";

		ResultSet rs = null;
		PreparedStatement pstmt = null;
		ArrayList<SurveyDTO> list = null;
		SurveyDTO dto = null;
		
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, begin);
		pstmt.setInt(2, end);
		rs = pstmt.executeQuery();
		if (rs.next()) {
			list = new ArrayList<>();
			do {
				dto = SurveyDTO.builder()
						.surveyId(rs.getInt("survey_id"))
						.userId(rs.getString("user_id"))
						.startDate(rs.getDate("start_date"))
						.endDate(rs.getDate("end_date"))
						.title(rs.getString("title"))
						.state(rs.getString("state"))
						.surveyAllcnt(rs.getInt("survey_allcnt"))
						.regdate(rs.getDate("regdate"))
						.build();
				
				list.add(dto);
				
			} while (rs.next());
		} //if
		
		rs.close();
		pstmt.close();
		
		return list;
	}
	//========================================================================

	@Override
	public int getTotalOption(int seq) throws SQLException {
		int getTotalcnt = 0;
		String sql = " SELECT SUM(option_cnt) FROM survey_option "
				+ " WHERE survey_id = ? ";
		
		PreparedStatement pstmt = this.conn.prepareStatement(sql);
		ResultSet rs = null;
		
		pstmt.setInt(1, seq);
		rs = pstmt.executeQuery();
		
		if (rs.next()) {
			getTotalcnt = rs.getInt(1);
		}
		return getTotalcnt;
	}
	//========================================================================


}
