DROP DATABASE IF EXISTS `wanchcoach`;
CREATE DATABASE `wanchcoach`;
use wanchcoach;

CREATE TABLE `member` (
                          `member_id`	BIGINT 	AUTO_INCREMENT PRIMARY KEY,
                          `login_id`	VARCHAR(30)	NOT NULL,
                          `encrypted_pwd`	VARCHAR(255)	NOT NULL,
                          `name`	VARCHAR(30)	NOT NULL,
                          `email`	VARCHAR(100)	NOT NULL,
                          `birth_date`	DATE	NOT NULL,
                          `gender`	VARCHAR(1)	NOT NULL,
                          `phone_number`	VARCHAR(30)	NOT NULL,
                          `active`	BOOLEAN	NOT NULL,
                          `refresh_token`	VARCHAR(255) 	NULL,
                          `login_type`	TINYINT	NOT NULL,
                          `location_permission`	BOOLEAN	NOT NULL DEFAULT 0,
                          `call_permission`	BOOLEAN	NOT NULL DEFAULT 0,
                          `camera_permission`	BOOLEAN NOT NULL DEFAULT 0,
                          `created_date`  DATETIME NOT NULL DEFAULT NOW(),
                          `modified_date` DATETIME NOT NULL DEFAULT NOW()
);

CREATE TABLE `member device token` (
                                       `device_token_id`	BIGINT  AUTO_INCREMENT	PRIMARY KEY,
                                       `member_id`	BIGINT 	NOT NULL,
                                       `device_token`	VARCHAR(255)	NOT NULL,
                                       `created_date`  DATETIME NOT NULL DEFAULT NOW(),
                                       `modified_date` DATETIME NOT NULL DEFAULT NOW()
);

CREATE TABLE `family` (
                          `family_id`	BIGINT 	AUTO_INCREMENT PRIMARY KEY,
                          `member_id`	BIGINT 	NOT NULL,
                          `name`	VARCHAR(30)	NOT NULL,
                          `birth_date`	DATE	NOT NULL,
                          `gender`	VARCHAR(1)	NOT NULL,
                          `image_file_name`	VARCHAR(255)	NULL,
                          `created_date`  DATETIME NOT NULL DEFAULT NOW(),
                          `modified_date` DATETIME NOT NULL DEFAULT NOW()
);

CREATE TABLE `treatment` (
                             `treatment_id`	BIGINT  AUTO_INCREMENT 	PRIMARY KEY,
                             `family_id`	BIGINT 	NOT NULL,
                             `hospital_id`	BIGINT 	NOT NULL,
                             `prescription_id`	BIGINT  NULL,
                             `department`	VARCHAR(30)	NULL,
                             `date`	DATETIME	NOT NULL,
                             `taken`	BOOLEAN	NOT NULL,
                             `alarm`	BOOLEAN	NOT NULL,
                             `symptom`	VARCHAR(100)	NULL,
                             `active`    BOOLEAN NOT NULL DEFAULT TRUE,
                             `created_date`  DATETIME NOT NULL DEFAULT NOW(),
                             `modified_date` DATETIME NOT NULL DEFAULT NOW()
);

CREATE TABLE `prescription` (
                                `prescription_id`	BIGINT  AUTO_INCREMENT 	PRIMARY KEY,
                                `pharmacy_id`	BIGINT 	NOT NULL,
                                `remains`	INT	NULL,
                                `taking`	BOOLEAN	NULL,
                                `end_date`	DATE	NULL,
                                `morning`	BOOLEAN	    NOT NULL DEFAULT TRUE,
                                `noon`	BOOLEAN	    NOT NULL    DEFAULT TRUE,
                                `evening`	BOOLEAN	    NOT NULL    DEFAULT TRUE,
                                `before_bed`	BOOLEAN	NOT NULL    DEFAULT TRUE,
                                `image_file_name`	VARCHAR(255)	NULL,
                                `active`    BOOLEAN NOT NULL DEFAULT TRUE,
                                `created_date`  DATETIME NOT NULL DEFAULT NOW(),
                                `modified_date` DATETIME NOT NULL DEFAULT NOW()
);

CREATE TABLE `prescribed drug` (
                                   `prescription_id`	BIGINT  NOT NULL,
                                   `drug_id`	BIGINT 	NOT NULL,
                                   `quantity`	FLOAT	NULL,
                                   `frequency`	INT	NULL,
                                   `day`	INT	NULL,
                                   `direction`	VARCHAR(200)	NULL,
                                   `created_date`  DATETIME NOT NULL DEFAULT NOW(),
                                   `modified_date` DATETIME NOT NULL DEFAULT NOW(),
                                   PRIMARY KEY (`prescription_id`, `drug_id`)
);

CREATE TABLE `treatment alarm` (
                                   `treatment_alarm_id`	BIGINT  AUTO_INCREMENT  PRIMARY KEY,
                                   `treatment_id`	BIGINT 	NOT NULL,
                                   `time`	DATETIME	NOT NULL,
                                   `content`	VARCHAR(50)	NOT NULL,
                                   `created_date`  DATETIME NOT NULL DEFAULT NOW(),
                                   `modified_date` DATETIME NOT NULL DEFAULT NOW()
);

CREATE TABLE `drug record` (
                               `mr_id`	BIGINT 	PRIMARY KEY,
                               `family_id`	BIGINT 	NOT NULL,
                               `prescription_id`	BIGINT 	NOT NULL,
                               `time`	DATETIME	NULL,
                               `created_date`  DATETIME NOT NULL DEFAULT NOW(),
                               `modified_date` DATETIME NOT NULL DEFAULT NOW()
);

CREATE TABLE `scheduled drug record` (
                                         `mr_id`	BIGINT  AUTO_INCREMENT 	PRIMARY KEY,
                                         `family_id`	BIGINT 	NOT NULL,
                                         `prescription_id`	BIGINT 	NOT NULL,
                                         `estimated_time`	DATETIME	NOT NULL,
                                         `alarm`	BOOLEAN	NOT NULL,
                                         `created_date`  DATETIME NOT NULL DEFAULT NOW(),
                                         `modified_date` DATETIME NOT NULL DEFAULT NOW()
);

CREATE TABLE `drug_administration_time` (
                                            `family_id`	BIGINT  AUTO_INCREMENT 	PRIMARY KEY,
                                            `morning`	TIME	NULL,
                                            `noon`	TIME	NULL,
                                            `evening`	TIME	NULL,
                                            `before_bed`	TIME	NULL,
                                            `created_date`  DATETIME NOT NULL DEFAULT NOW(),
                                            `modified_date` DATETIME NOT NULL DEFAULT NOW()
);

CREATE TABLE `drug` (
                        `drug_id`	BIGINT  AUTO_INCREMENT  PRIMARY KEY,
                        `drug_cat_id`	BIGINT 	NOT NULL,
                        `manufacturer`	VARCHAR(30)	NOT NULL,
                        `name`	VARCHAR(30)	NOT NULL,
                        `efficacy`	VARCHAR(255)	NOT NULL,
                        `ingredient`	VARCHAR(100)	NOT NULL,
                        `image_file_name`	VARCHAR(255)	NULL,
                        `created_date`  DATETIME NOT NULL DEFAULT NOW(),
                        `modified_date` DATETIME NOT NULL DEFAULT NOW()
);

CREATE TABLE `drug category` (
                                 `drug_cat_id`	BIGINT  AUTO_INCREMENT  PRIMARY KEY,
                                 `name`	VARCHAR(30)	NOT NULL,
                                 `created_date`  DATETIME NOT NULL DEFAULT NOW(),
                                 `modified_date` DATETIME NOT NULL DEFAULT NOW()
);

CREATE TABLE `pharmacy` (
                            `pharmacy_id`	BIGINT  PRIMARY KEY,
                            `name`	VARCHAR(60)	NOT NULL,
                            `address`	VARCHAR(100)	NOT NULL,
                            `phone_number`	VARCHAR(30)	NOT NULL,
                            `longitude`	DECIMAL(15, 12)	NULL,
                            `latitude`	DECIMAL(15, 13)	NULL,
                            `post_cdn`	VARCHAR(6) NULL,
                            `etc` VARCHAR(2000) NULL,
                            `hpid` VARCHAR(10) NOT NULL,
                            `created_date`  DATETIME NOT NULL DEFAULT NOW(),
                            `modified_date` DATETIME NOT NULL DEFAULT NOW()
);

CREATE TABLE `pharmacy opening hour` (
                                         `pharmacy_oh_id`	BIGINT  AUTO_INCREMENT  PRIMARY KEY,
                                         `pharmacy_id`	BIGINT 	NOT NULL,
                                         `day_of_week`	TINYINT	NOT NULL,
                                         `start_time`	TIME	NOT NULL,
                                         `end_time`	TIME	NOT NULL,
                                         `created_date`  DATETIME NOT NULL DEFAULT NOW(),
                                         `modified_date` DATETIME NOT NULL DEFAULT NOW()
);

CREATE TABLE `hospital` (
                            `hospital_id`	BIGINT  PRIMARY KEY,
                            `type_id` TINYINT NOT NULL,
                            `type` VARCHAR(50) NOT NULL,
                            `name`	VARCHAR(60)	NOT NULL,
                            `address`	VARCHAR(100)	NOT NULL,
                            `phone_number`	VARCHAR(30)	NOT NULL,
                            `longitude`	DECIMAL(15, 12)	NULL,
                            `latitude`	DECIMAL(15, 13)	NULL,
                            `has_emergency_room` TINYINT NULL,
                            `post_cdn`	VARCHAR(6)	NULL,
                            `etc` VARCHAR(2000) NULL,
                            `hpid` VARCHAR(10) NOT NULL,
                            `created_date`  DATETIME NOT NULL DEFAULT NOW(),
                            `modified_date` DATETIME NOT NULL DEFAULT NOW()
);

CREATE TABLE `hospital opening hour` (
                                         `hospital_oh_id`	BIGINT  AUTO_INCREMENT 	PRIMARY KEY,
                                         `hospital_id`	BIGINT 	NOT NULL,
                                         `day_of_week`	TINYINT	NOT NULL,
                                         `start_time`	TIME	NOT NULL,
                                         `end_time`	TIME	NOT NULL,
                                         `created_date`  DATETIME NOT NULL DEFAULT NOW(),
                                         `modified_date` DATETIME NOT NULL DEFAULT NOW()
);

CREATE TABLE `favorite drug` (
                                 `member_id`	BIGINT 	NOT NULL,
                                 `drug_id`	BIGINT 	NOT NULL,
                                 `created_date`  DATETIME NOT NULL DEFAULT NOW(),
                                 `modified_date` DATETIME NOT NULL DEFAULT NOW(),
                                 PRIMARY KEY (`member_id`, `drug_id`)
);

ALTER TABLE `family` ADD CONSTRAINT `FK_member_TO_family_1` FOREIGN KEY (
                                                                         `member_id`
    )
    REFERENCES `member` (
                         `member_id`
        );

ALTER TABLE `member device token` ADD CONSTRAINT `FK_member_TO_member_device_token_1` FOREIGN KEY (
                                                                                                   `member_id`
    )
    REFERENCES `member` (
                         `member_id`
        );

ALTER TABLE `drug_administration_time` ADD CONSTRAINT `FK_family_TO_drug_administration_time_1` FOREIGN KEY (
                                                                                                             `family_id`
    )
    REFERENCES `family` (
                         `family_id`
        );

ALTER TABLE `treatment` ADD CONSTRAINT `FK_family_TO_treatment_1` FOREIGN KEY (
                                                                               `family_id`
    )
    REFERENCES `family` (
                         `family_id`
        );

ALTER TABLE `treatment` ADD CONSTRAINT `FK_hospital_TO_treatment_1` FOREIGN KEY (
                                                                                 `hospital_id`
    )
    REFERENCES `hospital` (
                           `hospital_id`
        );

ALTER TABLE `treatment` ADD CONSTRAINT `FK_prescription_TO_treatment_1` FOREIGN KEY (
                                                                                     `prescription_id`
    )
    REFERENCES `prescription` (
                               `prescription_id`
        );

ALTER TABLE `treatment alarm` ADD CONSTRAINT `FK_treatment_TO_treatment alarm_1` FOREIGN KEY (
                                                                                              `treatment_id`
    )
    REFERENCES `treatment` (
                            `treatment_id`
        );

ALTER TABLE `prescription` ADD CONSTRAINT `FK_pharmacy_TO_prescription_1` FOREIGN KEY (
                                                                                       `pharmacy_id`
    )
    REFERENCES `pharmacy` (
                           `pharmacy_id`
        );

ALTER TABLE `drug` ADD CONSTRAINT `FK_drug category_TO_drug_1` FOREIGN KEY (
                                                                            `drug_cat_id`
    )
    REFERENCES `drug category` (
                                `drug_cat_id`
        );

ALTER TABLE `prescribed drug` ADD CONSTRAINT `FK_prescription_TO_prescribed drug_1` FOREIGN KEY (
                                                                                                 `prescription_id`
    )
    REFERENCES `prescription` (
                               `prescription_id`
        );

ALTER TABLE `prescribed drug` ADD CONSTRAINT `FK_drug_TO_prescribed drug_1` FOREIGN KEY (
                                                                                         `drug_id`
    )
    REFERENCES `drug` (
                       `drug_id`
        );

ALTER TABLE `drug record` ADD CONSTRAINT `FK_family_TO_drug record_1` FOREIGN KEY (
                                                                                   `family_id`
    )
    REFERENCES `family` (
                         `family_id`
        );

ALTER TABLE `drug record` ADD CONSTRAINT `FK_prescription_TO_drug record_1` FOREIGN KEY (
                                                                                         `prescription_id`
    )
    REFERENCES `prescription` (
                               `prescription_id`
        );

ALTER TABLE `scheduled drug record` ADD CONSTRAINT `FK_family_TO_scheduled drug record_1` FOREIGN KEY (
                                                                                                       `family_id`
    )
    REFERENCES `family` (
                         `family_id`
        );

ALTER TABLE `scheduled drug record` ADD CONSTRAINT `FK_prescription_TO_scheduled drug record_1` FOREIGN KEY (
                                                                                                             `prescription_id`
    )
    REFERENCES `prescription` (
                               `prescription_id`
        );

ALTER TABLE `hospital opening hour` ADD CONSTRAINT `FK_hospital_TO_hospital opening hour_1` FOREIGN KEY (
                                                                                                         `hospital_id`
    )
    REFERENCES `hospital` (
                           `hospital_id`
        );

ALTER TABLE `pharmacy opening hour` ADD CONSTRAINT `FK_pharmacy_TO_pharmacy opening hour_1` FOREIGN KEY (
                                                                                                         `pharmacy_id`
    )
    REFERENCES `pharmacy` (
                           `pharmacy_id`
        );

ALTER TABLE `favorite drug` ADD CONSTRAINT `FK_member_TO_favorite drug_1` FOREIGN KEY (
                                                                                       `member_id`
    )
    REFERENCES `member` (
                         `member_id`
        );

ALTER TABLE `favorite drug` ADD CONSTRAINT `FK_drug_TO_favorite drug_1` FOREIGN KEY (
                                                                                     `drug_id`
    )
    REFERENCES `drug` (
                       `drug_id`
        );

insert into wanchcoach.member (member_id, id, encrypted_pwd, name, email, birth_date, gender, phone_number, active, refresh_token, login_type, location_permission, call_permission, camera_permission, created_date, modified_date)
values  (1, '1', 'test_pwd', '유호재', 'ho_0214@naver.com', '2024-06-13', 'M', '010-7226-0214', 1, 'test_token', 1, 0, 0, 0, '2024-06-13 22:25:06', '2024-06-13 22:25:06'),
        (2, '2', 'test_pwd', '신규람', 'gyulife@naver.com', '1998-01-01', 'M', '010-1234-5678', 1, 'test_token2', 0, 1, 1, 1, '2024-06-15 04:44:55', '2024-06-15 04:44:55');

insert into wanchcoach.family (family_id, member_id, name, birth_date, gender, image_file_name, created_date, modified_date)
values  (1, 1, '유호재', '2024-06-13', 'M', 'test_image_file_name', '2024-06-13 22:25:06', '2024-06-13 22:25:06'),
        (2, 2, '신규람', '1998-01-01', 'M', null, '2024-06-15 04:45:47', '2024-06-15 04:45:47'),
        (3, 1, '배용현', '1998-01-02', 'M', null, '2024-06-15 05:22:59', '2024-06-15 05:22:59'),
        (4, 1, '나종현', '1998-01-03', 'M', null, '2024-06-15 05:22:59', '2024-06-15 05:22:59'),
        (5, 1, '곽강한', '1995-01-01', 'F', null, '2024-06-15 05:22:59', '2024-06-15 05:22:59'),
        (6, 2, '신호인', '1997-01-01', 'M', null, '2024-06-15 05:22:59', '2024-06-15 05:22:59'),
        (7, 2, '홍진식', '2024-06-15', 'F', null, '2024-06-15 05:22:59', '2024-06-15 05:22:59');

insert into `drug category` (drug_cat_id, name, created_date, modified_date) values (1, '진통제', '2024-06-13 20:42:32', '2024-06-13 20:42:32');
insert into `drug category` (drug_cat_id, name, created_date, modified_date) values (2, '소염제', '2024-06-13 20:42:32', '2024-06-13 20:42:32');

insert into `drug` (drug_id, drug_cat_id, manufacturer, name, efficacy, ingredient, image_file_name, created_date, modified_date) values (1, 1, '규람제약', '규라미논', '모든 고통을 없애줍니다.', '규라미산나트륨', null, '2024-06-13 20:43:43', '2024-06-13 20:43:43');
insert into `drug` (drug_id, drug_cat_id, manufacturer, name, efficacy, ingredient, image_file_name, created_date, modified_date) values (2, 2, '호제약', '호재신', '모든 염증을 없애줍니다.', '호재산칼륨', null, '2024-06-13 20:43:43', '2024-06-13 20:43:43');

insert into wanchcoach.prescription (prescription_id, pharmacy_id, remains, taking, end_date, image_file_name, created_date, modified_date)
values  (1, 1, 9, 1, null, null, '2024-06-15 05:20:40', '2024-06-15 05:20:40'),
        (2, 1, 6, 1, null, null, '2024-06-15 05:20:57', '2024-06-15 05:20:57');

insert into wanchcoach.treatment (treatment_id, family_id, hospital_id, prescription_id, department, date, taken, alarm, symptom, created_date, modified_date)
values  (1, 1, 1, 1, '내과', '2024-04-30 00:00:00', 1, 1, '복통', '2024-06-13 22:25:18', '2024-06-13 22:25:19'),
        (2, 1, 1, 2, '내과', '2024-05-01 00:00:00', 1, 1, '복통', '2024-06-13 22:42:42', '2024-06-13 22:42:43'),
        (3, 2, 1, null, '비뇨기과', '2024-05-02 00:00:00', 1, 1, '고환통', '2024-06-15 04:53:41', '2024-06-15 04:53:41'),
        (4, 2, 3, null, '피부과', '2024-06-30 00:00:00', 0, 1, '여드름', '2024-06-15 04:53:41', '2024-06-15 04:53:41'),
        (5, 2, 4, null, '내과', '2024-05-15 00:00:00', 1, 1, '복통', '2024-06-15 04:53:41', '2024-06-15 04:53:41'),
        (6, 3, 2, null, '피부과', '2024-05-22 00:00:00', 1, 1, '종기', '2024-06-15 04:53:41', '2024-06-15 04:53:41'),
        (7, 3, 3, null, '정형외과', '2024-06-16 00:00:00', 0, 1, '무릎 통증', '2024-06-15 04:53:41', '2024-06-15 04:53:41'),
        (8, 4, 1, null, '이비인후과', '2024-05-17 00:00:00', 1, 1, '코막힘', '2024-06-15 04:53:41', '2024-06-15 04:53:41'),
        (9, 5, 5, null, '피부과', '2024-06-20 00:00:00', 0, 1, '뾰루지', '2024-06-15 04:53:41', '2024-06-15 04:53:41'),
        (10, 6, 5, null, '이비인후과', '2024-04-30 00:00:00', 1, 1, '기침', '2024-06-15 04:53:41', '2024-06-15 04:53:41'),
        (11, 6, 2, null, '이비인후과', '2024-05-20 00:00:00', 1, 1, '인후통', '2024-06-15 04:53:41', '2024-06-15 04:53:41'),
        (12, 6, 3, null, '외과', '2024-06-27 00:00:00', 0, 1, '베임', '2024-06-15 04:53:41', '2024-06-15 04:53:41'),
        (13, 7, 4, null, '외과', '2024-06-01 00:00:00', 1, 1, '욕창', '2024-06-15 04:53:41', '2024-06-15 04:53:41'),
        (14, 7, 4, null, '비뇨기과', '2024-07-01 00:00:00', 0, 1, '포경수술', '2024-06-15 04:53:41', '2024-06-15 04:53:41');
