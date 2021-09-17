drop table member;
drop sequence member_id_seq;
create table member(
  id        number(8),
  email     varchar2(40),
  pw        varchar2(10) constraint member_pw_nn not null,
  tel       varchar2(13),
  nickname  varchar2(30),--한글 3byte
  gender    char(3),
  region    varchar2(11),
  birth     date,
  letter    char(1),
  fid       number(10),
  status    char(1),
  cdate     timestamp default systimestamp,
  udate     timestamp,
  constraint member_id_pk primary key(id),
  constraint member_email_uk unique (email),
  constraint member_region_fk foreign key (region) references code(code),
  --constraint member_pw_nn not null ,
  constraint member_gender_ck check (gender in ('남','여')),
  constraint member_letter_ck check (letter in ('1','0'))
);
create sequence member_id_seq;


drop table code;
create table code(
  code VARCHAR2(11),		--코드
  decode varchar2(30),		--코드명
  descript clob,			--코드설명
  pcode varchar2(11),		--상위코드
  use_yn char(1),			--사용여부
  cdate timestamp default systimestamp,
  udate timestamp,
  constraint code_code_pk primary key(code),
  constraint code_pcode_fk foreign key(code) references code,
  constraint code_decode_uk unique (decode),
  constraint code_use_yn_ck check(use_yn in('Y','N'))
);

insert into code(code,decode,pcode,use_yn) values ('A01','취미',NULL,'Y'); 
insert into code(code,decode,pcode,use_yn) values ('A02','성별',NULL,'Y');
insert into code(code,decode,pcode,use_yn) values ('A03','지역',NULL,'Y');
insert into code(code,decode,pcode,use_yn) values ('A04','첨부파일',NULL,'Y');
insert into code(code,decode,pcode,use_yn) values ('A0101','수영','A01','Y');
insert into code(code,decode,pcode,use_yn) values ('A0102','골프','A01','Y');
insert into code(code,decode,pcode,use_yn) values ('A0103','영화','A01','Y');
insert into code(code,decode,pcode,use_yn) values ('A0201','남','A02','Y');
insert into code(code,decode,pcode,use_yn) values ('A0202','여','A02','Y');
insert into code(code,decode,pcode,use_yn) values ('A0301','서울','A03','Y');
insert into code(code,decode,pcode,use_yn) values ('A0302','부산','A03','Y');
insert into code(code,decode,pcode,use_yn) values ('A0303','울산','A03','Y');
insert into code(code,decode,pcode,use_yn) values ('A0401','회원이미지','A04','Y');
insert into code(code,decode,pcode,use_yn) values ('A0402','상품이미지','A04','Y');

drop table hobby;
create table hobby (
  member_id number(8),
  code_code varchar2(11),
  constraint hobby_pk primary key(member_id,code_code)
);


--게시판
drop table board;
create table board(
  BNUM	    NUMBER(10),
  BCATEGORY	VARCHAR2(11),
  BTITLE	  VARCHAR2(150),
  BID	      NUMBER(8),
  BEMAIL	  VARCHAR2(40),
  BNICKNAME	VARCHAR2(30),
  BHIT	    NUMBER(5),
  BCONTENT	CLOB,
  PBNUM	    NUMBER(10),
  BGROUP	  NUMBER(10),
  BSTEP	    NUMBER(3),
  BINDENT	  NUMBER(3),
  STATUS	  CHAR(1),
  BCDATE	  TIMESTAMP default systimestamp,
  BUDATE	  TIMESTAMP,
  constraint board_bnum_pk primary key(bnum),
  constraint board_bcategory_fk foreign key(bcategory) references code(code),
  constraint board_bid_fk foreign key(bid) references member(id),
  constraint board_bemail_fk foreign key(bemail) references member(email)
);
drop sequence board_bnum_seq;
create sequence board_bnum_seq
increment by 1 --증감치
start with 1 --시작값
maxvalue 9999999999 --최대값
nocycle;  --순환하지않음

--첨부파일
drop table uploadfile;
create table uploadfile(
  fid number(10),
  rid varchar2(10) not null,
  code varchar2(11) not null,
  store_fname varchar2(50) not null,
  upload_fname varchar2(50) not null,
  fsize varchar2(45) not null,
  ftype varchar2(100) not null,
  cdate timestamp default systimestamp,
  udate timestamp,
  constraint uploadfile_fid_pk primary key(fid),  
  constraint uploadfile_code_fk2 FOREIGN key(code) references code
);

drop sequence uploadfile_fid_seq;
create sequence uploadfile_fid_seq
increment by 1 --증감치
start with 1 --시작값
maxvalue 9999999999 --최대값
nocycle;  --순환하지않음