CREATE TABLE drug_administration_time (
                                          id BIGINT NOT NULL AUTO_INCREMENT,
                                          member_id BIGINT NOT NULL,
                                          morning TIME NOT NULL,
                                          noon TIME NOT NULL,
                                          evening TIME NOT NULL,
                                          before_bed TIME NOT NULL,
                                          CREATED_DATE DATETIME NOT NULL,
                                          MODIFIED_DATE DATETIME NOT NULL,
                                          PRIMARY KEY (id),
                                          CONSTRAINT fk_member_id FOREIGN KEY (member_id) REFERENCES member (member_id)
);