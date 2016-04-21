CREATE TABLE USER_TEST (
  USER_ID VARCHAR(40) NOT NULL,
  FIRST_NAME VARCHAR (40) NOT NULL,
  LAST_NAME VARCHAR (40) NOT NULL,
  EMAIL VARCHAR (40) NOT NULL,
  PRIMARY KEY (USER_ID)
);


insert into USER_TEST values('1', 'firstName1', 'lastName1', 'email1');
insert into USER_TEST values('2', 'firstName2', 'lastName2', 'email2');
