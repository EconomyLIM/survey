package survey.controller;

import java.util.Scanner;

import survey.domain.UserDTO;
import survey.service.UserService;

public class UserController {
	
	private Scanner scanner = null;
	private UserService service;
	
	public UserController() {
		this.scanner = new Scanner(System.in);
	} // UserController

	public UserController(UserService service) {
		this();
		this.service = service;
	}
	
	public void insertUSer() {
		
		System.out.printf("ID, Password, Name, Email 입력 (띄어쓰기X, ','로 구분 ): ");
		String datas[] = this.scanner.next().split(",");
		
		String userId = datas[0];
		String password = datas[1];
		String name = datas[2];
		String email = datas[3];
		String role = "회원";
		
		if (password.length() < 8) {
			System.out.println("비밀번호는 8자리 이상 해주세요.");
			return;
		}
		
		UserDTO dto = UserDTO.builder()
				.userId(userId)
				.password(password)
				.name(name)
				.email(email)
				.role(role)
				.build();
		
		int rowCnt = this.service.insertUser(dto);
		if (rowCnt == 1) {
			System.out.println("회원가입 완료");
		} // if
		
	} 

}
