
CREATE SEQUENCE  IF NOT EXISTS exam_sequence START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE  IF NOT EXISTS question_sequence START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE  IF NOT EXISTS response_sequence START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE  IF NOT EXISTS taken_exam_sequence START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE  IF NOT EXISTS user_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE exam (
  id BIGINT NOT NULL,
   owner_id BIGINT,
   exam_number VARCHAR(255),
   name VARCHAR(255),
   pass_mark DOUBLE PRECISION,
   description TEXT,
   instructions TEXT,
   start TIMESTAMP WITHOUT TIME ZONE,
   duration INTEGER,
   is_timed BOOLEAN,
   is_open BOOLEAN,
   created TIMESTAMP WITHOUT TIME ZONE,
   CONSTRAINT pk_exam PRIMARY KEY (id)
);

CREATE TABLE exam_candidates (
  exam_id BIGINT NOT NULL,
   candidates_id BIGINT NOT NULL,
   CONSTRAINT pk_exam_candidates PRIMARY KEY (exam_id, candidates_id)
);

CREATE TABLE exam_registered_candidates (
  exam_id BIGINT NOT NULL,
   registered_candidates_id BIGINT NOT NULL,
   CONSTRAINT pk_exam_registeredcandidates PRIMARY KEY (exam_id, registered_candidates_id)
);

CREATE TABLE question (
  id BIGINT NOT NULL,
   exam_id BIGINT NOT NULL,
   text TEXT,
   point DOUBLE PRECISION,
   answer TEXT,
   CONSTRAINT pk_question PRIMARY KEY (id)
);

CREATE TABLE question_options (
  question_id BIGINT NOT NULL,
   options TEXT
);

CREATE TABLE question_response (
  id BIGINT NOT NULL,
   taken_exam_id BIGINT,
   question_id BIGINT,
   user_choice VARCHAR(255),
   is_correct BOOLEAN,
   CONSTRAINT pk_questionresponse PRIMARY KEY (id)
);

CREATE TABLE taken_exam (
  id BIGINT NOT NULL,
   exam_id BIGINT,
   user_id BIGINT,
   is_passed BOOLEAN,
   total_points DOUBLE PRECISION,
   user_start_time TIMESTAMP WITHOUT TIME ZONE,
   submission_date TIMESTAMP WITHOUT TIME ZONE,
   last_modified_on TIMESTAMP WITHOUT TIME ZONE,
   CONSTRAINT pk_takenexam PRIMARY KEY (id)
);

CREATE TABLE users_table (
  id BIGINT NOT NULL,
   email VARCHAR(255) NOT NULL,
   name VARCHAR(255),
   password VARCHAR(255),
   is_enabled BOOLEAN,
   date_added TIMESTAMP WITHOUT TIME ZONE,
   role INTEGER,
   CONSTRAINT pk_users_table PRIMARY KEY (id)
);

ALTER TABLE users_table ADD CONSTRAINT uc_users_table_email UNIQUE (email);

ALTER TABLE exam ADD CONSTRAINT FK_EXAM_ON_OWNER FOREIGN KEY (owner_id) REFERENCES users_table (id);

ALTER TABLE question_response ADD CONSTRAINT FK_QUESTIONRESPONSE_ON_QUESTION FOREIGN KEY (question_id) REFERENCES question (id);

ALTER TABLE question_response ADD CONSTRAINT FK_QUESTIONRESPONSE_ON_TAKEN_EXAM FOREIGN KEY (taken_exam_id) REFERENCES taken_exam (id);

ALTER TABLE question ADD CONSTRAINT FK_QUESTION_ON_EXAM FOREIGN KEY (exam_id) REFERENCES exam (id);

ALTER TABLE taken_exam ADD CONSTRAINT FK_TAKENEXAM_ON_EXAM FOREIGN KEY (exam_id) REFERENCES exam (id);

ALTER TABLE taken_exam ADD CONSTRAINT FK_TAKENEXAM_ON_USER FOREIGN KEY (user_id) REFERENCES users_table (id);

ALTER TABLE exam_candidates ADD CONSTRAINT fk_exacan_on_exam FOREIGN KEY (exam_id) REFERENCES exam (id);

ALTER TABLE exam_candidates ADD CONSTRAINT fk_exacan_on_user FOREIGN KEY (candidates_id) REFERENCES users_table (id);

ALTER TABLE exam_registered_candidates ADD CONSTRAINT fk_exaregcan_on_exam FOREIGN KEY (exam_id) REFERENCES exam (id);

ALTER TABLE exam_registered_candidates ADD CONSTRAINT fk_exaregcan_on_user FOREIGN KEY (registered_candidates_id) REFERENCES users_table (id);

ALTER TABLE question_options ADD CONSTRAINT fk_question_options_on_question FOREIGN KEY (question_id) REFERENCES question (id);