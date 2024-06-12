DROP DATABASE IF EXISTS `wanchcoach`;
CREATE DATABASE `wanchcoach`;
use wanchcoach;

CREATE TABLE `member` (
	`member_id`	BIGINT 	PRIMARY KEY,
	`id`	VARCHAR(30)	NOT NULL,
	`encrypted_pwd`	VARCHAR(255)	NOT NULL,
	`name`	VARCHAR(30)	NOT NULL,
	`email`	VARCHAR(100)	NOT NULL,
	`birth_date`	DATE	NOT NULL,
	`gender`	TINYINT	NOT NULL,
	`phone_number`	VARCHAR(30)	NOT NULL,
	`active`	TINYINT	NOT NULL,
	`refresh_token`	BIGINT 	NULL,
	`login_type`	TINYINT	NOT NULL,
	`location_permission`	TINYINT	NOT NULL DEFAULT 0,
	`call_permission`	TINYINT	NOT NULL DEFAULT 0,
    `camera_permission`	TINYINT NOT NULL DEFAULT 0
);

CREATE TABLE `member_device_token` (
	`device_token_id`	BIGINT 	PRIMARY KEY,
	`member_id`	BIGINT 	NOT NULL,
	`device_token`	VARCHAR(255)	NOT NULL
);

CREATE TABLE `family` (
	`family_id`	BIGINT 	PRIMARY KEY,
	`member_id`	BIGINT 	NOT NULL,
	`name`	VARCHAR(30)	NOT NULL,
	`birth_date`	DATE	NOT NULL,
	`gender`	TINYINT	NOT NULL,
	`image_file_name`	VARCHAR(255)	NULL
);

CREATE TABLE `treatment` (
	`treatment_id`	BIGINT 	PRIMARY KEY,
	`family_id`	BIGINT 	NOT NULL,
	`hospital_id`	BIGINT 	NOT NULL,
	`prescription_id`	BIGINT  NULL,
	`department`	VARCHAR(30)	NULL,
	`date`	DATETIME	NOT NULL,
	`taken`	TINYINT	NOT NULL,
	`alarm`	TINYINT	NOT NULL,
	`symptom`	VARCHAR(100)	NULL
);

CREATE TABLE `prescription` (
	`prescription_id`	BIGINT 	PRIMARY KEY,
	`pharmacy_id`	BIGINT 	NOT NULL,
    `remains`	INT	NULL,
	`taking`	TINYINT	NULL,
	`end_date`	DATE	NULL,
	`image_file_name`	VARCHAR(255)	NULL
);

CREATE TABLE `prescribed medicine` (
	`prescription_id`	BIGINT  NOT NULL,
	`medicine_id`	BIGINT 	NOT NULL,
	`quantity`	FLOAT	NULL,
	`frequency`	INT	NULL,
	`day`	INT	NULL,
	`direction`	VARCHAR(200)	NULL,
    PRIMARY KEY (`prescription_id`, `medicine_id`)
);

CREATE TABLE `treatment alarm` (
	`treatment_alarm_id`	BIGINT  PRIMARY KEY,
	`treatment_id`	BIGINT 	NOT NULL,
	`time`	DATETIME	NOT NULL,
	`content`	VARCHAR(50)	NOT NULL
);

CREATE TABLE `medicine record` (
	`mr_id`	BIGINT 	PRIMARY KEY,
	`family_id`	BIGINT 	NOT NULL,
	`prescription_id`	BIGINT 	NOT NULL,
	`time`	DATETIME	NULL
);

CREATE TABLE `scheduled medicine record` (
	`mr_id`	BIGINT 	PRIMARY KEY,
	`family_id`	BIGINT 	NOT NULL,
	`prescription_id`	BIGINT 	NOT NULL,
	`estimated_time`	DATETIME	NOT NULL,
	`alarm`	TINYINT	NOT NULL
);

CREATE TABLE `medicine administration time` (
	`family_id`	BIGINT 	PRIMARY KEY,
	`morning`	TIME	NULL,
	`noon`	TIME	NULL,
	`evening`	TIME	NULL,
	`before_bed`	TIME	NULL
);

CREATE TABLE `medicine` (
	`medicine_id`	BIGINT  PRIMARY KEY,
	`medicine_cat_id`	BIGINT 	NOT NULL,
	`manufacturer`	VARCHAR(30)	NOT NULL,
	`name`	VARCHAR(30)	NOT NULL,
	`efficacy`	VARCHAR(255)	NOT NULL,
	`ingredient`	VARCHAR(100)	NOT NULL,
	`image_file_name`	VARCHAR(255)	NULL
);

CREATE TABLE `medicine category` (
	`medicine_cat_id`	BIGINT  PRIMARY KEY,
	`name`	VARCHAR(30)	NOT NULL
);

CREATE TABLE `pharmacy` (
	`pharmacy_id`	BIGINT 	PRIMARY KEY,
	`name`	VARCHAR(30)	NOT NULL,
	`address`	VARCHAR(100)	NOT NULL,
	`phone_number`	VARCHAR(30)	NOT NULL,
	`longitude`	DECIMAL(15, 12)	NOT NULL,
	`latitude`	DECIMAL(15, 13)	NOT NULL,
	`post_cdn_1`	VARCHAR(3)	NOT NULL,
	`post_cdn_2`	VARCHAR(3)	NOT NULL
);

CREATE TABLE `pharmacy opening hour` (
	`pharmacy_oh_id`	BIGINT  PRIMARY KEY,
	`pharmacy_id`	BIGINT 	NOT NULL,
	`day_of_week`	TINYINT	NOT NULL,
	`start_time`	TIME	NOT NULL,
	`end_time`	TIME	NOT NULL
);

CREATE TABLE `hospital` (
	`hospital_id`	BIGINT 	PRIMARY KEY,
	`name`	VARCHAR(30)	NOT NULL,
	`address`	VARCHAR(100)	NOT NULL,
	`phone_number`	VARCHAR(30)	NOT NULL,
	`longitude`	DECIMAL(15, 12)	NOT NULL,
	`latitude`	DECIMAL(15, 13)	NOT NULL,
	`post_cdn_1`	VARCHAR(3)	NOT NULL,
	`post_cdn_2`	VARCHAR(3)	NOT NULL
);

CREATE TABLE `hospital opening hour` (
	`hospital_oh_id`	BIGINT 	PRIMARY KEY,
	`hospital_id`	BIGINT 	NOT NULL,
	`day_of_week`	TINYINT	NOT NULL,
	`start_time`	TIME	NOT NULL,
	`end_time`	TIME	NOT NULL
);

CREATE TABLE `favorite medicine` (
	`member_id`	BIGINT 	NOT NULL,
	`medicine_id`	BIGINT 	NOT NULL,
    PRIMARY KEY (`member_id`, `medicine_id`)
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

ALTER TABLE `medicine administration time` ADD CONSTRAINT `FK_family_TO_medicine administration time_1` FOREIGN KEY (
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

ALTER TABLE `medicine` ADD CONSTRAINT `FK_medicine category_TO_medicine_1` FOREIGN KEY (
	`medicine_cat_id`
)
REFERENCES `medicine category` (
	`medicine_cat_id`
);

ALTER TABLE `prescribed medicine` ADD CONSTRAINT `FK_prescription_TO_prescribed medicine_1` FOREIGN KEY (
	`prescription_id`
)
REFERENCES `prescription` (
	`prescription_id`
);

ALTER TABLE `prescribed medicine` ADD CONSTRAINT `FK_medicine_TO_prescribed medicine_1` FOREIGN KEY (
	`medicine_id`
)
REFERENCES `medicine` (
	`medicine_id`
);

ALTER TABLE `medicine record` ADD CONSTRAINT `FK_family_TO_medicine record_1` FOREIGN KEY (
	`family_id`
)
REFERENCES `family` (
	`family_id`
);

ALTER TABLE `medicine record` ADD CONSTRAINT `FK_prescription_TO_medicine record_1` FOREIGN KEY (
	`prescription_id`
)
REFERENCES `prescription` (
	`prescription_id`
);

ALTER TABLE `scheduled medicine record` ADD CONSTRAINT `FK_family_TO_scheduled medicine record_1` FOREIGN KEY (
	`family_id`
)
REFERENCES `family` (
	`family_id`
);

ALTER TABLE `scheduled medicine record` ADD CONSTRAINT `FK_prescription_TO_scheduled medicine record_1` FOREIGN KEY (
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

ALTER TABLE `favorite medicine` ADD CONSTRAINT `FK_member_TO_favorite medicine_1` FOREIGN KEY (
	`member_id`
)
REFERENCES `member` (
	`member_id`
);

ALTER TABLE `favorite medicine` ADD CONSTRAINT `FK_medicine_TO_favorite medicine_1` FOREIGN KEY (
	`medicine_id`
)
REFERENCES `medicine` (
	`medicine_id`
);