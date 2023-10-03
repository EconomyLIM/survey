package survey.service;

import java.sql.SQLException;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import oracle.net.aso.p;
import survey.domain.SurveyDTO;
import survey.domain.SurveyOptionDTO;
import survey.domain.UserDTO;
import survey.persistence.SurveyDAO;
import survey.persistence.SurveyDAOImpl;

@Data
@AllArgsConstructor
@Builder
public class SurveyService {
	private SurveyDAO dao = null;
	
	public int insertSurvey(SurveyDTO dto) {
		int rowCnt = 0;
		
		System.out.println(" 설문조사 등록 - 로그기록 ");
		try {
			rowCnt = dao.insertSurvey(dto);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rowCnt;
	} // insertSurvey
	
	//================================================================
	public int insertSurveyOption(SurveyOptionDTO dto) {
		int rowCnt = 0;
		
		
		try {
			rowCnt = dao.insertSurveyOption(dto);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rowCnt;
	}
	
	//================================================================
	public ArrayList<SurveyDTO> selectService(){
		ArrayList<SurveyDTO> list = null;
		
		
		try {
			list = this.dao.select();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
		
	}
	//================================================================
	
	public ArrayList<SurveyOptionDTO> selectOpService(int seq){
		ArrayList<SurveyOptionDTO> list = null;
		
	
		
		try {
			list = this.dao.select(seq);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
	
	//================================================================
	public int increaseOptionCount(int seq) {
		int rowCnt = 0;
		
		try {
			rowCnt = dao.increaseOptionCnt(seq);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rowCnt;
	}
	
	//================================================================
	public int deleteService(int seq) {
		int rowCnt = 0;
		
		try {
			rowCnt = this.dao.delete(seq);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rowCnt;
	}
	
	//================================================================
	public int SurveyUpdateService(SurveyDTO dto) {
		int rowCnt = 0;
		
		try {
			rowCnt = this.dao.updateSurvey(dto);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rowCnt;
	}
	//================================================================
	
	public SurveyDTO surveyViewService(int id) {
		SurveyDTO dto = null;
		
		try {
			dto = this.dao.viewSurvey(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return dto;
	}
	//================================================================
	
	public ArrayList<SurveyDTO> surveySearchService (int searchCondition, String searchWord){
		
		ArrayList<SurveyDTO> list =  null;
		
		System.out.println("검색 로그 기록");
		
		try {
			list = this.dao.surveySearch(searchCondition, searchWord);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	//================================================================
	public String pageService(int currentPage, int numberPerPage, int numberOfPageBlock) {
		// "\t\t\t [1] 2 3 4 5 6 7 8 9 10 > "
		String pagingBlock = "\t\t\t";

		int totalPages;
		try {
			totalPages = this.dao.getTotalPages(numberPerPage);
			
			int start = (currentPage -1) /numberOfPageBlock * numberOfPageBlock +1 ;
			int end= start + numberOfPageBlock -1;         
			end =   end > totalPages ? totalPages : end;

			if( start != 1 ) pagingBlock +=" < ";          
			for (int j = start; j <= end; j++) {
				pagingBlock += String.format(currentPage==j?"[%d] " : "%d " , j);
			}         
			if( end != totalPages ) pagingBlock +=" > ";

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return pagingBlock;
	} // pageService
	
	//================================================================
	
	public ArrayList<SurveyDTO> surveySelectService(int currentPage, int numberPerPage){
		ArrayList<SurveyDTO> list = null;
		
		try {
			list = this.dao.surveySelect(currentPage, numberPerPage);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	} 
	//================================================================
	
	public int getOPtionCnt(int seq) {
		int getTotalCnt = 0;
		
		System.out.println("한 설문조사 전체 카운트 수 - 로그 기록");
		 try {
			getTotalCnt =  this.dao.getTotalOption(seq);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return getTotalCnt;
	}
	
} // class
