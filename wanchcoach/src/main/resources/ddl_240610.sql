DROP DATABASE IF EXISTS `wanchcoach`;
CREATE DATABASE `wanchcoach`;
use wanchcoach;

CREATE TABLE `member` (
	`member_id`	BIGINT 	AUTO_INCREMENT PRIMARY KEY,
	`id`	VARCHAR(30)	NOT NULL,
	`encrypted_pwd`	VARCHAR(255)	NOT NULL,
	`name`	VARCHAR(30)	NOT NULL,
	`email`	VARCHAR(100)	NOT NULL,
	`birth_date`	DATE	NOT NULL,
	`gender`	TINYINT	NOT NULL,
	`phone_number`	VARCHAR(30)	NOT NULL,
	`active`	TINYINT	NOT NULL,
	`refresh_token`	VARCHAR(255) 	NULL,
	`login_type`	TINYINT	NOT NULL,
	`location_permission`	TINYINT	NOT NULL DEFAULT 0,
	`call_permission`	TINYINT	NOT NULL DEFAULT 0,
    `camera_permission`	TINYINT NOT NULL DEFAULT 0,
    `created_date`  DATETIME NOT NULL DEFAULT NOW(),
    `modified_date` DATETIME NOT NULL DEFAULT NOW()
);

CREATE TABLE `member_device_token` (
	`device_token_id`	BIGINT  AUTO_INCREMENT	PRIMARY KEY,
	`member_id`	BIGINT 	NOT NULL,
	`device_token`	VARCHAR(255)	NOT NULL,
    `created_date`  DATETIME NOT NULL DEFAULT NOW(),
    `modified_date` DATETIME NOT NULL DEFAULT NOW()
);

CREATE TABLE `family` (
	`family_id`	BIGINT 	PRIMARY KEY,
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
	`taken`	TINYINT	NOT NULL,
	`alarm`	TINYINT	NOT NULL,
	`symptom`	VARCHAR(100)	NULL,
    `created_date`  DATETIME NOT NULL DEFAULT NOW(),
    `modified_date` DATETIME NOT NULL DEFAULT NOW()
);

CREATE TABLE `prescription` (
	`prescription_id`	BIGINT  AUTO_INCREMENT 	PRIMARY KEY,
	`pharmacy_id`	BIGINT 	NOT NULL,
    `remains`	INT	NULL,
	`taking`	TINYINT	NULL,
	`end_date`	DATE	NULL,
	`image_file_name`	VARCHAR(255)	NULL,
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
	`alarm`	TINYINT	NOT NULL,
    `created_date`  DATETIME NOT NULL DEFAULT NOW(),
    `modified_date` DATETIME NOT NULL DEFAULT NOW()
);

CREATE TABLE `drug administration time` (
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
	`pharmacy_id`	BIGINT  AUTO_INCREMENT 	PRIMARY KEY,
	`name`	VARCHAR(30)	NOT NULL,
	`address`	VARCHAR(100)	NOT NULL,
	`phone_number`	VARCHAR(30)	NOT NULL,
	`longitude`	DECIMAL(15, 12)	NOT NULL,
	`latitude`	DECIMAL(15, 13)	NOT NULL,
	`post_cdn1`	INT	NOT NULL,
	`post_cdn2`	INT	NOT NULL,
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
	`hospital_id`	BIGINT  AUTO_INCREMENT 	PRIMARY KEY,
	`name`	VARCHAR(30)	NOT NULL,
	`address`	VARCHAR(100)	NOT NULL,
	`phone_number`	VARCHAR(30)	NOT NULL,
	`longitude`	DECIMAL(15, 12)	NOT NULL,
	`latitude`	DECIMAL(15, 13)	NOT NULL,
	`post_cdn1`	INT	NOT NULL,
	`post_cdn2`	INT	NOT NULL,
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

ALTER TABLE `member_device_token` ADD CONSTRAINT `FK_member_TO_member_device_token_1` FOREIGN KEY (
	`member_id`
)
REFERENCES `member` (
	`member_id`
);

ALTER TABLE `drug administration time` ADD CONSTRAINT `FK_family_TO_drug administration time_1` FOREIGN KEY (
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

insert into `member` (id, encrypted_pwd, name, email, birth_date, gender, phone_number, active, refresh_token, login_type)
values (1, 'test_pwd', '유호재', 'ho_0214@naver.com', now(), 0, '010-7226-0214', true, 'test_token', 1);

insert into `family` (family_id, member_id, name, birth_date, gender, image_file_name)
values (1, 1, '유호재', now(), 0, 'test_image_file_name');

insert into `hospital` (hospital_id, name, address, phone_number, longitude, latitude, post_cdn1, post_cdn2)
values (1, '세브란스병원', '광주광역시 광산구 첨단과기로', '010-2638-5572', 126.833009748716, 35.2271203871567, 622, 53);

insert into `pharmacy` (pharmacy_id, name, address, phone_number, longitude, latitude, post_cdn1, post_cdn2, created_date, modified_date)
values (1, '종현약국', '서울특별시 관악구 봉천동', '010-4064-3297', 126.941892000000, 37.4823620000000, '559', '48', '2024-06-13 20:45:22', '2024-06-13 20:45:22');

insert into `drug category` (drug_cat_id, name, created_date, modified_date) values (1, '진통제', '2024-06-13 20:42:32', '2024-06-13 20:42:32');
insert into `drug category` (drug_cat_id, name, created_date, modified_date) values (2, '소염제', '2024-06-13 20:42:32', '2024-06-13 20:42:32');

insert into `drug` (drug_id, drug_cat_id, manufacturer, name, efficacy, ingredient, image_file_name, created_date, modified_date) values (1, 1, '규람제약', '규라미논', '모든 고통을 없애줍니다.', '규라미산나트륨', null, '2024-06-13 20:43:43', '2024-06-13 20:43:43');
insert into `drug` (drug_id, drug_cat_id, manufacturer, name, efficacy, ingredient, image_file_name, created_date, modified_date) values (2, 2, '호제약', '호재신', '모든 염증을 없애줍니다.', '호재산칼륨', null, '2024-06-13 20:43:43', '2024-06-13 20:43:43');


