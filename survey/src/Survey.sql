DROP TABLE user_info;
DROP TABLE SURVEY;
DROP TABLE vote;
DROP TABLE SURVEY_OPTION;
DROP SEQUENCE vote_seq;
DROP SEQUENCE survey_seq;


CREATE TABLE user_info(
    user_id NVARCHAR2(20) CONSTRAINT pk_user_info_user_id PRIMARY KEY CHECK(LENGTH(user_id) >= 4)
    , password NVARCHAR2(20) NOT NULL CHECK(LENGTH(password) >= 8)
    , name NVARCHAR2(5) NOT NULL
    , email NVARCHAR2(20) NOT NULL UNIQUE
    , ROLE NVARCHAR2(3) DEFAULT '일반' NOT NULL
);
----------------------------------------
CREATE TABLE SURVEY
(
SURVEY_ID NUMBER PRIMARY KEY NOT NULL 
,USER_ID NVARCHAR2(20)
,START_DATE DATE DEFAULT SYSDATE 
,END_DATE DATE NOT NULL
,TITLE VARCHAR2(100) NOT NULL
,STATE VARCHAR(20) DEFAULT '진행중'
,SURVEY_ALLCNT NUMBER(10) DEFAULT 0 
,CONSTRAINT FK_SURVEY_USER_ID FOREIGN KEY(USER_ID) REFERENCES USER_INFO(user_id) ON DELETE SET NULL
,REGDATE DATE DEFAULT SYSDATE
);
----------------------------------------
CREATE TABLE SURVEY_OPTION
(
SURVEY_ID NUMBER NOT NULL
,CONTENT_ID NUMBER
,OPTION_CONTENT VARCHAR2(20)
,CONSTRAINT FK_OPTION_SURVEY_ID FOREIGN KEY(SURVEY_ID) REFERENCES SURVEY(SURVEY_ID) ON DELETE CASCADE
, CONSTRAINT survey_option_pk PRIMARY KEY(content_id)
);


----------------------------------------
CREATE TABLE vote(
    user_id NVARCHAR2(20) CONSTRAINT fk_user_info_user_id REFERENCES user_info(user_id) CHECK(LENGTH(user_id) >= 4)
    , SURVEY_ID NUMBER NOT NULL 
    , ITEM_ID NUMBER NOT NULL     
    ,FOREIGN KEY (ITEM_ID) REFERENCES SURVEY_OPTION(CONTENT_ID)
    ,FOREIGN KEY (SURVEY_ID) REFERENCES SURVEY(SURVEY_ID)
    , CONSTRAINT vote_pk PRIMARY KEY(user_id)
); 
----------------------------------------
CREATE SEQUENCE vote_seq
       INCREMENT BY 1
       START WITH 1
       MINVALUE 1
       MAXVALUE 9999
       NOCYCLE
       NOCACHE
       NOORDER;
       

-- survey 시퀀스
CREATE  SEQUENCE survey_seq
INCREMENT BY 1
START WITH 1
MINVALUE 1
MAXVALUE 9999
NOCYCLE
NOCACHE
NOORDER;
-- survey_vote 시퀀스
CREATE  SEQUENCE survey_vote_seq
INCREMENT BY 1
START WITH 1
MINVALUE 1
MAXVALUE 9999
NOCYCLE
NOCACHE
NOORDER;       

Grant create SEQUENCE to scott;

INSERT INTO USER_INFO VALUES ('teamleader','pw123123','신기범','tlsrlqja@gmail.com','관리자');
INSERT INTO USER_INFO VALUES ('team1','pw124124','고경림','rhrudfla@gmail.com','회원');
INSERT INTO USER_INFO VALUES ('team2','pw125125','송해영','thdgodud@gmail.com','회원');
INSERT INTO USER_INFO VALUES ('team3','pw126126','이경서','dlrudtj@gmail.com','회원');
INSERT INTO USER_INFO VALUES ('team4','pw127127','이상문','dltkdans@naver.com','회원');
INSERT INTO USER_INFO VALUES ('team5','pw128128','이주영','dlwndud@gmail.com','회원');
INSERT INTO USER_INFO VALUES ('team6','pw129129','임경재','dlarudwo@gmail.com','회원');
COMMIT;

INSERT INTO SURVEY VALUES (survey_seq.NEXTVAL,'team1',SYSDATE,'2023/9/15','조장 잘하는지', '진행중',7,SYSDATE);
SELECT * FROM survey;
INSERT INTO survey_option VALUES(1, survey_vote_seq.nextval, '약간 잘함');
INSERT INTO survey_option VALUES(1, survey_vote_seq.nextval, '약간 부족');
INSERT INTO survey_option VALUES( (SELECT max(survey_id) FROM survey_option) , survey_vote_seq.nextval, '약간 부족');
commit;

alter table survey_option
add option_cnt NUMBER default 0;


UPDATE survey s
set SURVEY_ALLCNT = NVL((SELECT SUM(OPTION_cnt) FROM survey_option so where s.survey_id = so.survey_id group by survey_id),0);
commit;

GRANT CREATE TRIGGER TO SCOTT;
CREATE OR REPLACE TRIGGER tr_survey_option
AFTER INSERT OR DELETE ON survey_option
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        UPDATE survey s
        set SURVEY_ALLCNT = NVL((SELECT SUM(OPTION_cnt) FROM survey_option so where s.survey_id = so.survey_id group by survey_id),0);
    ELSIF DELETING THEN
       UPDATE survey s
        set SURVEY_ALLCNT = NVL((SELECT SUM(OPTION_cnt) FROM survey_option so where s.survey_id = so.survey_id group by survey_id),0);
    END IF;
END;
DROP TRIGGER tr_survey_option;
commit;
SELECT * 
				FROM ( 
				      SELECT ROWNUM no, t.* 
				       FROM (  
			             SELECT survey_id, user_id, start_date, end_date, title, state, survey_allcnt, regdate   
				         FROM survey 
			              ORDER BY survey_id DESC 
				      ) t 
				)  b ;
                
select SUM(OPtion_cnt) from survey_option where survey_id = 13;

select * from survey;
delete from survey
where survey_id = 19;

commit;

