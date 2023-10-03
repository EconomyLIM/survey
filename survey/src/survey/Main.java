package survey;

import java.sql.Connection;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.util.DBConn;

import survey.controller.SurveyController;
import survey.controller.UserController;
import survey.persistence.SurveyDAO;
import survey.persistence.SurveyDAOImpl;
import survey.persistence.UserDAO;
import survey.persistence.UserDAOImpl;
import survey.service.SurveyService;
import survey.service.UserService;

public class Main {
	public static void main(String[] args) throws ParseException {
		
		Connection conn = DBConn.getConnection();
		
		// 유저 회원가입
//		UserDAO dao = new UserDAOImpl(conn);
//		UserService uservice = new UserService(dao);
//		UserController ucontroller = new UserController(uservice);
//		ucontroller.insertUSer();
		
		// 설문조사
		SurveyDAO sDao = new SurveyDAOImpl(conn);
		SurveyService sService = new SurveyService(sDao);
		SurveyController scontroller = new SurveyController(sService);
		scontroller.surveyStart();
		
		
		
		
	} //main
}
