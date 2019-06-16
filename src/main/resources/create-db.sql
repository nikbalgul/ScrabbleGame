CREATE TABLE BOARD(BOARDID INT PRIMARY KEY ,STATUS VARCHAR(1), MOVE_SEQ INT);

CREATE SEQUENCE BOARD_SEQUENCE_ID START WITH (select max(BOARDID) + 1 from BOARD);

CREATE TABLE MOVE(MOVEID INT PRIMARY KEY, BOARDID INT, START_X INT, START_Y INT,END_X INT,END_Y INT,TEXT VARCHAR(50),MOVE_NUM INT, SUM_SCORE INT, MOVE_SEQ INT);

CREATE SEQUENCE MOVE_SEQUENCE_ID START WITH (select max(MOVEID) + 1 from MOVE);

CREATE TABLE SCORE(SCOREID INT PRIMARY KEY, MOVEID INT, VALUE INT);

CREATE SEQUENCE SCORE_SEQUENCE_ID START WITH (select max(SCOREID) + 1 from SCORE);

COMMIT;