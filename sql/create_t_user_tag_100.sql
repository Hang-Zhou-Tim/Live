DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `create_t_user_tag_100`()
BEGIN

         DECLARE i INT;
         DECLARE table_name VARCHAR(100);
         DECLARE sql_text VARCHAR(3000);
         DECLARE table_body VARCHAR(2000);
         SET i=0;
         SET sql_text='';
         SET table_body='(
   user_id bigint NOT NULL DEFAULT -1 COMMENT \'user id\',
   tag_info_01 bigint NOT NULL DEFAULT 0 COMMENT \'tags binary record\',
   tag_info_02 bigint NOT NULL DEFAULT 0 COMMENT \'tags binary record\',
   tag_info_03 bigint NOT NULL DEFAULT 0 COMMENT \'tags binary record\',
   create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT \'creation date\',
   update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT \'update date\',
  PRIMARY KEY (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT=\'user tags record\';';


            WHILE i<100 DO
                IF i<10 THEN
                    SET table_name = CONCAT('t_user_tag_0',i);
                ELSE
                    SET table_name = CONCAT('t_user_tag_',i);
                END IF;

                SET sql_text=CONCAT('CREATE TABLE ',table_name, table_body);
            SELECT sql_text;
            SET @sql_text=sql_text;
            PREPARE stmt FROM @sql_text;
            EXECUTE stmt;
            DEALLOCATE PREPARE stmt;
            SET i=i+1;
        END WHILE;

    END;;
DELIMITER ;

USE live_user;
call create_t_user_tag_100;