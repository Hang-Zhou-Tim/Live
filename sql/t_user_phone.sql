CREATE TABLE t_user_phone(
    id bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary id',
    phone varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL DEFAULT '' COMMENT 'phone',
    user_id bigint DEFAULT -1 COMMENT 'user id',
    status tinyint DEFAULT -1 COMMENT 'state(0 invalid，1 valid)',
    create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT ' creation date',
    update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update time',
    PRIMARY KEY (id),
    UNIQUE KEY `udx_phone` (`phone`),
    KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;