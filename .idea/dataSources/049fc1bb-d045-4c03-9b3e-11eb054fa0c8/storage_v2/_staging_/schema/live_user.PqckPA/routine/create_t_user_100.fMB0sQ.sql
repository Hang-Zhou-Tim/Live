create
    definer = hang@`%` procedure create_t_user_100()
BEGIN

    DECLARE i INT;
    DECLARE table_name VARCHAR(30);
    DECLARE table_pre VARCHAR(30);
    DECLARE sql_text VARCHAR(3000);
    DECLARE table_body VARCHAR(2000);
    SET i=0;
    SET table_name='';

    SET sql_text='';
    SET table_body = '(
  user_id bigint NOT NULL DEFAULT -1 COMMENT \'User id\',
  nick_name varchar(35)  DEFAULT NULL COMMENT \'nickname\',
  avatar varchar(255)  DEFAULT NULL COMMENT \'avatar\',
  true_name varchar(20)  DEFAULT NULL COMMENT \'real name\',
  sex tinyint(1) DEFAULT NULL COMMENT \'sex 0famle，1 female\',
  born_date datetime DEFAULT NULL COMMENT \'birthdate\',
  work_city int(9) DEFAULT NULL COMMENT \'working place\',
  born_city int(9) DEFAULT NULL COMMENT \'city where user borns\',
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (user_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;';

    WHILE i<100 DO
            IF i<10 THEN
                SET table_name = CONCAT('t_user_0',i);
            ELSE
                SET table_name = CONCAT('t_user_',i);
            END IF;

            SET sql_text=CONCAT('CREATE TABLE ',table_name, table_body);
            SELECT sql_text;
            SET @sql_text=sql_text;
            PREPARE stmt FROM @sql_text;
            EXECUTE stmt;
            DEALLOCATE PREPARE stmt;
            SET i=i+1;
        END WHILE;


END;

