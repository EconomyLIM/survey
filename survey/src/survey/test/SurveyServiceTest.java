package survey.test;

import java.sql.Connection;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.Test;

import com.util.DBConn;

import survey.domain.SurveyDTO;
import survey.persistence.SurveyDAO;
import survey.persistence.SurveyDAOImpl;
import survey.service.SurveyService;

public class SurveyServiceTest {
	
	@Test
	void insertSurveyTest()  {
		Connection conn = DBConn.getConnection();
		SurveyDAO dao = new SurveyDAOImpl(conn);
		SurveyService service = new SurveyService(dao);
		
		String startDate= "2023-09-30";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date;
		try {
			date = sdf.parse(startDate);
			System.out.println("util date"+ date);
			Date sqldate = new Date(date.getTime());
			System.out.println("sql date"+ sqldate);
			
			
			SurveyDTO dto = SurveyDTO.builder()
					.userId("teamleader")
					.startDate(null)
					.endDate(sqldate)
					.title("test3")
					.state(null)
					.surveyAllcnt(0)
					.regdate(null)
					.build();
			
		
			
			int rowCnt = service.insertSurvey(dto);
			
			if (rowCnt == 1 ) {
				System.out.println("> 설문조사 등록 완료");
				return;
			} // if
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
