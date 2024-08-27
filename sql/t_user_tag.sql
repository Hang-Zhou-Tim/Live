CREATE TABLE t_user_tag(
    user_id bigint NOT NULL DEFAULT -1 COMMENT 'user id',
    tag_info_01 bigint NOT NULL DEFAULT 0 COMMENT 'tags binary record 01',
    tag_info_02 bigint NOT NULL DEFAULT 0 COMMENT 'tags binary record 02',
    tag_info_03 bigint NOT NULL DEFAULT 0 COMMENT 'tags binary record 03',
    create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'creation date',
    update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update date',
    PRIMARY KEY (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='user tags record';