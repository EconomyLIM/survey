package com.util;

public class ConverLowerColumName {
	public static void main(String[] args) {
		
		String descEmp = "SEQ       NOT NULL NUMBER        \r\n"
				+ "WRITER    NOT NULL VARCHAR2(20)  \r\n"
				+ "PWD       NOT NULL VARCHAR2(20)  \r\n"
				+ "EMAIL              VARCHAR2(100) \r\n"
				+ "TITLE     NOT NULL VARCHAR2(200) \r\n"
				+ "WRITEDATE          DATE          \r\n"
				+ "READED             NUMBER        \r\n"
				+ "TAG                NUMBER(1)     \r\n"
				+ "CONTENT            CLOB";
			
		
		String lowerDescEmp = descEmp.toLowerCase();
		String regex = "\\s+.+\\s+";
		lowerDescEmp = lowerDescEmp.replaceAll(regex, ";\r\n");
		System.out.println(lowerDescEmp);
		
	} //main
}
