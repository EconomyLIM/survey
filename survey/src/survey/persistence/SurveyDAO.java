package survey.persistence;

import java.sql.SQLException;
import java.util.ArrayList;

import survey.domain.SurveyDTO;
import survey.domain.SurveyOptionDTO;
import survey.domain.UserDTO;

public interface SurveyDAO {
	
	// 1. 설문조사 등록 --완료
	int insertSurvey (SurveyDTO dto) throws SQLException;
	
	// 1-2. 설문조사 항목 등록 --완료
	int insertSurveyOption(SurveyOptionDTO dto) throws SQLException;
	
	// 2. 설문조사 조회 --완료
	ArrayList<SurveyDTO> select() throws SQLException;
	
	// 2-2. 설문조사 조회 + 페이징 처리 --완료
	ArrayList<SurveyDTO> surveySelect(int currentPage, int numberPerPage) throws SQLException;
	
	// 3-0 설문조사 조회 페이지 들어갈때 설문조사 정보 조회
	SurveyDTO viewSurvey(int id) throws SQLException;
	
	// 3. 설문조사 항목조회 --완료
	ArrayList<SurveyOptionDTO> select(int seq) throws SQLException;
	
	// 4. 설문조사 투표 수 증가 --완료
	int increaseOptionCnt (int seq) throws SQLException;
	
	// 5. 설문조사 수정 --완료
	int updateSurvey(SurveyDTO dto) throws SQLException;
	
	// 5.2 설문조사 항목 수정 및 삭제
	
	// 6. 설문조사 삭제 --완료
	int delete (int seq) throws SQLException;
	
	// 7. 설문조사 검색 --완료
	ArrayList<SurveyDTO> surveySearch(int searchCondition, String searchWord) throws SQLException;
	
	// 8. 총 설문조사 수 --완료
	int getTotalSurvey() throws SQLException;
	
	// 9. 총 페이지 수 --완료
	int getTotalPages(int numberPerPage) throws SQLException;
	
	// 10. 한 설문조사의 전체 투표수
	int getTotalOption(int seq) throws SQLException;

}
