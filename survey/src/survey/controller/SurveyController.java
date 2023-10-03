package survey.controller;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import com.util.DBConn;

import survey.domain.SurveyDTO;
import survey.domain.SurveyOptionDTO;
import survey.service.SurveyService;

public class SurveyController {
	private Scanner scanner = null;
	private int selectedNumber;
	private SurveyService service;

	// 페이징 처리 필드
	private int currentPage = 1;
	private int numberPerPage = 10;
	private int numberOfPageBlock = 10;

	public SurveyController() {
		this.scanner = new Scanner(System.in);
	}

	public SurveyController(SurveyService service) {
		this();
		this.service = service;
	}
	//===========================================================================
	private void 일시정지() {
		System.out.println(" \t\t 계속하려면 엔터치세요.");
		try {
			System.in.read();
			System.in.skip(System.in.available()); // 13, 10
		} catch (IOException e) { 
			e.printStackTrace();
		}
	} // 일시정지

	//=================================================================================
	// SurveyStart
	public void surveyStart() {
		while (true) {
			메뉴출력();
			메뉴선택();
			메뉴처리();
		} //while
	} // surveyStart

	//===========================================================================
	private void 메뉴출력() {
		String [] menus = {"등록", "조회", "선택", "수정", "삭제", "검색", "종료"};
		System.out.println("메뉴 출력");
		for (int i = 0; i < menus.length; i++) {
			System.out.printf("%d. %s\t", i+1, menus[i]);
		} // for
		System.out.println();

	} // 메뉴선택

	//===========================================================================
	private void 메뉴선택() {
		System.out.printf("> 메뉴 선택하세요 >");
		selectedNumber = this.scanner.nextInt();

	} // 메뉴출력

	//===========================================================================
	private void 메뉴처리() {
		switch (this.selectedNumber) {
		case 1: 
			설문조사등록();
			break;
		case 2: 
			설문조사조회();
			break;
		case 3: 
			설문조사선택();
			break;
		case 4: 
			설문조사수정();
			break;
		case 5: 
			설문조사삭제();
			break;
		case 6: 
			설문조사검색();
			break;
		case 7: 
			exit();
			break;
		}
	} // 메뉴처리
	//===========================================================================

	private void 설문조사등록() {
		System.out.printf(" > 설문조사 제목, 종료 시간 입력 :>");
		String [] datas = this.scanner.next().split(",");
		String title = datas[0];
		String endDate = datas[1];

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date;
		try {
			date = sdf.parse(endDate);
			Date sqldate = Date.valueOf(endDate);

			SurveyDTO dto = SurveyDTO.builder()
					.userId("teamleader")
					.startDate(null)
					.endDate(sqldate)
					.title(title)
					.state(null)
					.surveyAllcnt(0)
					.regdate(null)
					.build();

			int rowCnt = this.service.insertSurvey(dto);

			if (rowCnt == 1) {
				System.out.println(" <<<설문조사 등록 완료>>>");
			}

			설문항목등록();

			일시정지();

		} catch (ParseException e) {
			System.out.println("시간 입력 오류");
			e.printStackTrace();
		}

	} // 설문조사등록

	//===========================================================================
	private void 설문항목등록() {

		char order = 'y';

		do {

			System.out.printf("설문항목 입력 >");
			String optionContent = this.scanner.next();

			SurveyOptionDTO vo = SurveyOptionDTO.builder()
					.optionContent(optionContent)
					.build();
			int rowCnt = this.service.insertSurveyOption(vo);

			if (rowCnt == 1) {
				System.out.println(" 옵션 insert 성공! ");
			}

			try {
				System.out.printf("입력을 계속 하시겠습니까? >");
				order = (char)System.in.read();
				System.in.skip(System.in.available());
			} catch (IOException e) {
				e.printStackTrace();
			} // try_catch


		} while (Character.toUpperCase(order) == 'Y');
	}

	//===========================================================================
	private void 설문조사조회() {
		System.out.printf("현재 페이지( current Page) 번호를 입력 >");
		this.currentPage = this.scanner.nextInt();


		ArrayList<SurveyDTO> list = this.service.surveySelectService(currentPage, numberPerPage);
		ArrayList<SurveyOptionDTO> optionlist =null;
		java.util.Date date = new java.util.Date();
		java.util.Date dat2 = new java.util.Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("\t\t\t설문조사");
		System.out.println("-".repeat(150));
		System.out.printf("%-10s\t%-10s\t\t%-10s\t\t%-7s\t%-7s\t%s\t%s%-10s\t\t%s\n"
				,"설문조사번호" ,"작성자" ,"제목", "시작일" ,"종료일", "상태","항목수", "(총 투표 수)", "등록일");
		System.out.println("-".repeat(150));
		
		if (list == null) {
			System.out.println("\t\t >  설문조사가 존재 하지 않습니다.");
		} else {
			Iterator<SurveyDTO> it = list.iterator();
			while (it.hasNext()) {
				SurveyDTO dto = it.next();
				optionlist =  this.service.selectOpService(dto.getSurveyId());
				Date sqlStartDate = dto.getStartDate();
				Date sqlEndDate = dto.getEndDate();
				String state = "진행중";
				if (date.before(sqlStartDate)) {
					state = "진행전";
				} else if (date.after(sqlEndDate)){
					state = "종료";
				}else {
					state = "진행중";
				}
				System.out.printf("%-10s\t%-10s\t\t%-10s\t\t%-7s\t%-7s\t%s\t%d\t%-10s\t\t%s\n"
						,dto.getSurveyId() 
						,dto.getUserId()
						,dto.getTitle()
						,dto.getStartDate()
						,dto.getEndDate()
						,state
						,optionlist.size()
//						,sqlEndDate.before(date) ? "진행중" : sqlStartDate.after(date)? "진행전" : "종료"
						,dto.getSurveyAllcnt()
						,dto.getRegdate());
			}
		} //if

		System.out.println("-".repeat(150));

		
		String pagingBlock = this.service.pageService(
				this.currentPage
				, this.numberPerPage
				, this.numberOfPageBlock
				);
		System.out.println(pagingBlock);
		
		System.out.println("-".repeat(150));
		
		System.out.printf("보고자 하는 설문조사 입력 >");
		int seq = this.scanner.nextInt();
		optionlist =  this.service.selectOpService(seq);
		int totalCnt = this.service.getOPtionCnt(seq);
		if (totalCnt == 0) {
			totalCnt = 1;
		}
		System.out.printf("%-3s\t\t%s\t\t%s\n", "옵션ID", "옵션내용", "투표수");
		if (list == null) {
			System.out.println(" 아직 등록된 항목이 없습니다. ");
		}else {
			Iterator<SurveyOptionDTO> it = optionlist.iterator();
			while (it.hasNext()) {
				SurveyOptionDTO dto = it.next();
				System.out.printf("%-3d\t\t%s\t\t%s%d(%d%s)\n"
						,dto.getContentId()
						,dto.getOptionContent()
						,"#".repeat(dto.getOptionCnt()*100 / totalCnt)
						,dto.getOptionCnt()
						,dto.getOptionCnt()*100 / totalCnt,"%" )
						;
				
			} // while
			System.out.printf("		투표 하시겠습니까? (Y/N) >");
			String answer = this.scanner.next();
			if (answer.toUpperCase().equals("Y") ) {
				System.out.printf("선택할 번호 입력: ");
				int option = this.scanner.nextInt();
				int rowCnt = this.service.increaseOptionCount(option);

				if (rowCnt == 1) {
					System.out.printf(">%d번 설문 선택 완료 \n", option);
				} //if
			} // if

		} // if else 
		
		일시정지();
	} // 설문조사조회

	//===========================================================================
	private void 설문조사선택() {
		System.out.printf("설문조사번호 입력 >");
		int seq = this.scanner.nextInt();
		
		// 설문조사 번호, 작성자, 제목 입력
		SurveyDTO vo =  this.service.surveyViewService(seq);
		System.out.printf("설문조사번호:%d\t작성자:%s\t\t제목:%s\t\n",vo.getSurveyId(), vo.getUserId(), vo.getTitle());
		
		// 항목 출력
		ArrayList<SurveyOptionDTO> list =  this.service.selectOpService(seq);

		System.out.printf("%-3s\t\t%s\t\t%s\n", "옵션ID", "옵션내용", "투표수");
		if (list == null) {
			System.out.println(" 아직 등록된 항목이 없습니다. ");
		}else {
			Iterator<SurveyOptionDTO> it = list.iterator();
			while (it.hasNext()) {
				SurveyOptionDTO dto = it.next();
				System.out.printf("%-3d\t\t%s\t\t%d\n"
						,dto.getContentId()
						,dto.getOptionContent()
						,dto.getOptionCnt());
			} // while
			System.out.printf("		투표 하시겠습니까? (Y/N) >");
			String answer = this.scanner.next();
			if (answer.toUpperCase().equals("Y") ) {
				System.out.printf("선택할 번호 입력: ");
				int option = this.scanner.nextInt();
				int rowCnt = this.service.increaseOptionCount(option);

				if (rowCnt == 1) {
					System.out.printf(">%d번 설문 선택 완료 \n", option);
				} //if
			} // if

		} // if else 

		일시정지();
	} //설문조사선택

	//===========================================================================
	private void 설문조사수정() {
		System.out.printf("수정할 설문조사번호를 입력하세요. >");
		int id = this.scanner.nextInt();
		
		
		try {
			SurveyDTO dto = this.service.surveyViewService(id);
			System.out.println("설문조사 아이디: " + id);
			System.out.println("설문조사 작성자: " + dto.getUserId());
			System.out.println("설문조사 시작일: " + dto.getStartDate());
			System.out.println("설문조사 종료일: " + dto.getEndDate());
			System.out.println("설문조사 제목: " + dto.getTitle());
			System.out.println("설문조사 상태: " + dto.getState());
			System.out.println("설문조사 투표수: " + dto.getSurveyAllcnt());
			System.out.println("설문조사 등록일: " + dto.getRegdate());

			// 제목, 종료일만 수정
			System.out.print("수정할 제목을 입력하세요 >");
			String title = this.scanner.next();
			System.out.print("수정할 종료일을 입력하세요 >");
			String inputdate = this.scanner.next();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date;
			
			date = sdf.parse(inputdate);
			Date sqldate = new Date(date.getTime());
			dto = dto.builder()
					.surveyId(id)
					.userId(dto.getUserId())
					.startDate(dto.getStartDate())
					.endDate(sqldate)
					.title(title)
					.state(dto.getState())
					.surveyAllcnt(dto.getSurveyAllcnt())
					.regdate(dto.getRegdate())
					.build();
			int rowCnt = this.service.SurveyUpdateService(dto);
			
			if (rowCnt == 1) {
				System.out.printf(">%d 설문조사 수정 완료 \n", id);
			}
			
		} catch (NullPointerException e) {
			System.out.println("정보가 없습니다.");
			e.printStackTrace();
			return;
		} catch (ParseException e) {
			System.out.println("날짜타입 확인하십시오.");
			e.printStackTrace();
			return;
		}

		

		일시정지();
	} // 설문조사수정

	//===========================================================================
	private void 설문조사삭제() {
		System.out.println("삭제할 설문조사 번호를 입력하세요 >");
		int survey_id = this.scanner.nextInt();
		int rowCnt = 0;
		rowCnt =  this.service.deleteService(survey_id);

		if (rowCnt == 1) {
			System.out.println("삭제 완료!");
		} // if

		일시정지();
	} // 설문조사삭제

	//===========================================================================
	private void 설문조사검색() {
		System.out.printf("		검색 조건: 설문조사ID(1), 제목(2) 선택 >");
		int searchCondtion = this.scanner.nextInt();
		System.out.printf("		검색어 입력 >");
		String serachWord = this.scanner.next();
		System.out.printf("현재 페이지( current Page) 번호를 입력");
		this.currentPage = this.scanner.nextInt();
		
		
		ArrayList<SurveyDTO> list = this.service.surveySearchService(searchCondtion, serachWord);
		
		System.out.println("\t\t\t설문조사");
		System.out.println("-".repeat(150));
		System.out.printf("%-10s\t%-10s\t\t%-10s\t\t%-7s\t%-7s\t%s\t%-10s\t\t%s\n"
				,"설문조사번호" ,"작성자" ,"제목", "시작일" ,"종료일", "상태", "(총 투표 수)", "등록일");
		System.out.println("-".repeat(150));
		if (list == null) {
			System.out.println("\t\t >  설문조사가 존재 하지 않습니다.");
		} else {
			Iterator<SurveyDTO> it = list.iterator();
			while (it.hasNext()) {
				SurveyDTO dto = it.next();
				System.out.printf("%-10s\t%-10s\t\t%-10s\t\t%-7s\t%-7s\t%s\t%-10s\t\t%s\n"
						,dto.getSurveyId() 
						,dto.getUserId()
						,dto.getTitle()
						,dto.getStartDate()
						,dto.getEndDate()
						,dto.getState()
						,dto.getSurveyAllcnt()
						,dto.getRegdate());
			}
		} //if

		System.out.println("-".repeat(150));
		System.out.println("\t\t\t [1] 2 3 4 5 6 7 8 9 10 >");

		System.out.println("-".repeat(150));
		일시정지();
		
		
	} //설문조사검색
	
	//===========================================================================
	private void exit() {
		DBConn.close();
		System.out.println("\t\t\t  프로그램 종료!!!");
		System.exit(-1);

	} // exit

} // class
