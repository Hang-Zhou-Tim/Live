use live_user;
CREATE TABLE t_user(
    user_id bigint NOT NULL DEFAULT -1 COMMENT 'user id',
    nick_name varchar(35)  DEFAULT NULL COMMENT 'name',
    avatar varchar(255)  DEFAULT NULL COMMENT 'avatar',
    true_name varchar(20)  DEFAULT NULL COMMENT 'real name',
    sex tinyint(1) DEFAULT NULL COMMENT '0 for male, 1 for female',
    born_date datetime DEFAULT NULL COMMENT 'birthday',
    work_city int(9) DEFAULT NULL COMMENT 'workcity',
    born_city int(9) DEFAULT NULL COMMENT 'born_city',
    create_time datetime DEFAULT CURRENT_TIMESTAMP,
    update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;