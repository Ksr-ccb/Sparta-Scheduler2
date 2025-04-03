CREATE TABLE `schedule` (
                            `SCHEDULE_ID` BIGINT NOT NULL COMMENT '각 스케줄별 일정 아이디',
                            `CONTENTS` VARCHAR(200) NOT NULL COMMENT '일정 내용',
                            `TITLE` VARCHAR(40) NOT NULL COMMENT '일정 제목',
                            `CREATE_DATE` DATETIME NOT NULL COMMENT '생성날짜',
                            `UPDATE_DATE` DATETIME NOT NULL COMMENT '최종 수정된 날짜',
                            `WRITER_ID` BIGINT NOT NULL COMMENT '작성자의 고유 아이디',
                            PRIMARY KEY (`SCHEDULE_ID`)
);

CREATE TABLE `user` (
                         `USER_ID` BIGINT NOT NULL COMMENT '작성자의 고유 아이디',
                         `USER_NAME` VARCHAR(20) NOT NULL COMMENT '작성자의 이름',
                         `EMAIL` VARCHAR(40) NOT NULL COMMENT '작성자 이메일',
                         `PASSWORD`	VARCHAR(200) NOT NULL COMMENT '작성자 비밀번호',
                         `UPDATE_DATE` DATETIME NOT NULL COMMENT '최종 작성자 수정날짜',
                         `CREATE_DATE` DATETIME NOT NULL COMMENT '생성날짜',
                         PRIMARY KEY (`USER_ID`)
);

CREATE TABLE `comment` (
                            `COMMENTS_ID` BIGINT NOT NULL COMMENT '덧글 아이디',
                            `SCHEDULE_ID` BIGINT NOT NULL COMMENT '각 스케줄별 일정 아이디',
                            `USER_ID` BIGINT NOT NULL COMMENT '작성자의 고유 아이디',
                            `COMMENTS` VARCHAR(200) NOT NULL COMMENT '덧글 내용',
                            `UPDATE_DATE` DATETIME NOT NULL COMMENT '최종 수정된 날짜',
                            `CREATE_DATE` DATETIME NOT NULL COMMENT '생성날짜',
                            PRIMARY KEY (`COMMENTS_ID`)
);

ALTER TABLE `schedule` ADD CONSTRAINT `FK_SCHEDULE_WRITER`
    FOREIGN KEY (`WRITER_ID`) REFERENCES `user`(`USER_ID`);

ALTER TABLE `comment` ADD CONSTRAINT `FK_COMMENTS_SCHEDULE`
    FOREIGN KEY (`SCHEDULE_ID`) REFERENCES `schedule`(`SCHEDULE_ID`);

ALTER TABLE `comment` ADD CONSTRAINT `FK_COMMENTS_USER`
    FOREIGN KEY (`USER_ID`) REFERENCES `user`(`USER_ID`);
