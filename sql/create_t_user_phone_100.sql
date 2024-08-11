DELIMITER ;;
CREATE DEFINER=`hang`@`%` PROCEDURE `create_t_user_phone_100`()
BEGIN
    DECLARE i INT;
    DECLARE table_name VARCHAR(30);
    DECLARE table_pre VARCHAR(30);
    DECLARE sql_text VARCHAR(3000);
    DECLARE table_body VARCHAR(2000);
    SET i=0;
    SET table_name='';
    SET sql_text='';
    SET table_body = ' (
    id bigint unsigned NOT NULL AUTO_INCREMENT COMMENT \'primary id\',
    phone varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL DEFAULT \'\' COMMENT \'phone\',
    user_id bigint DEFAULT -1 COMMENT \'user id\',
    status tinyint DEFAULT -1 COMMENT \'state(0 invalid，1 valid)\',
    create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT \' creation_date\',
    update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT \'update time\',
    PRIMARY KEY (id),
    UNIQUE KEY `udx_phone` (`phone`),
    KEY idx_user_id (user_id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;';
    WHILE i<100 DO
        IF i<10 THEN
            SET table_name = CONCAT('t_user_phone_0',i);
        ELSE
            SET table_name = CONCAT('t_user_phone_',i);
        END IF;

        SET sql_text=CONCAT('CREATE TABLE ',table_name,table_body);
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
call create_t_user_phone_100();