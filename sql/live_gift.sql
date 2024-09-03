CREATE DATABASE live_gift;
USE live_gift;
-- Create syntax for TABLE 't_gift_config'
CREATE TABLE `t_gift_config` (
                                 `gift_id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'gift id',
                                 `price` int unsigned DEFAULT NULL COMMENT 'virtual money price',
                                 `gift_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'gift name',
                                 `status` tinyint unsigned DEFAULT NULL COMMENT 'status (0 invalid,1 valid)',
                                 `cover_img_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'cover img',
                                 `svga_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'svga address',
                                 `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'creation date',
                                 `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update time',
                                 PRIMARY KEY (`gift_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='gift config table';

-- Create syntax for TABLE 't_gift_record'
CREATE TABLE `t_gift_record` (
                                 `id` int unsigned NOT NULL AUTO_INCREMENT,
                                 `user_id` bigint DEFAULT NULL COMMENT 'sender',
                                 `object_id` bigint DEFAULT NULL COMMENT 'receiver',
                                 `gift_id` int DEFAULT NULL COMMENT 'gift id',
                                 `price` int DEFAULT NULL COMMENT 'sending money',
                                 `price_unit` tinyint DEFAULT NULL COMMENT 'money unit',
                                 `source` tinyint DEFAULT NULL COMMENT 'sending source',
                                 `send_time` datetime DEFAULT NULL COMMENT 'send time',
                                 `update_time` datetime DEFAULT NULL COMMENT 'update time',
                                 `json` json DEFAULT NULL,
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='sending record table';

INSERT INTO `t_gift_config` VALUES (22, 90, 'angel-2', 1, '../svga/img/angel.png', '../svga/beauty.svga', '2023-08-01 06:50:25', '2024-02-22 17:55:05');
INSERT INTO `t_gift_config` VALUES (23, 20, 'rainbow-2', 1, '../svga/img/rainbow.png', '../svga/female_avatar.svga', '2023-08-01 06:50:25', '2024-02-22 18:01:43');
INSERT INTO `t_gift_config` VALUES (24, 1, 'heart-2', 1, '../svga/img/heart.png', '../svga/heart.svga', '2023-08-01 06:50:25', '2024-02-22 18:01:52');
INSERT INTO `t_gift_config` VALUES (25, 500, 'supercar-2', 1, '../svga/img/supercar.png', '../svga/supercar.svga', '2023-08-01 06:50:25', '2024-02-22 17:49:22');
INSERT INTO `t_gift_config` VALUES (27, 45, 'rocket-2', 1, '../svga/img/rocket.jpg', '../svga/rocket.svga', '2023-08-01 06:50:25', '2024-02-22 17:50:56');
INSERT INTO `t_gift_config` VALUES (28, 45, 'halloween-2', 1, '../svga/img/halloween.png', '../svga/halloween.svga', '2023-08-01 06:50:25', '2024-02-22 17:49:18');
INSERT INTO `t_gift_config` VALUES (29, 20, 'beauty-2', 1, '../svga/img/angel.png', '../svga/avatar.svga', '2024-02-22 17:59:17', '2024-02-22 18:02:01');

-- Create syntax for TABLE 't_red_packet_config'
CREATE TABLE `t_red_packet_config` (
                                       `id` int unsigned NOT NULL AUTO_INCREMENT,
                                       `anchor_id` int NOT NULL DEFAULT '0' COMMENT 'anchor id',
                                       `start_time` datetime DEFAULT NULL COMMENT 'red packet rain start time',
                                       `total_get` int NOT NULL DEFAULT '0' COMMENT 'total number can be got',
                                       `total_get_price` int NOT NULL DEFAULT '0' COMMENT 'total price can be got',
                                       `max_get_price` int NOT NULL DEFAULT '0' COMMENT 'maximal price can be got',
                                       `status` tinyint NOT NULL DEFAULT '1' COMMENT '(1 prepare，2 for ready，3 for sent)',
                                       `total_price` int NOT NULL DEFAULT '0' COMMENT 'total price for the rain',
                                       `total_count` int unsigned NOT NULL DEFAULT '0' COMMENT 'total count for the rain',
                                       `config_code` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'unique code',
                                       `remark` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT 'remark',
                                       `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                       `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='red packet rain config';

INSERT INTO `live_gift`.`t_red_packet_config`
(`id`, `anchor_id`, `start_time`, `total_get`, `total_get_price`, `max_get_price`,
 `status`, `total_price`, `total_count`, `config_code`, `remark`, `create_time`, `update_time`)
VALUES (2, 10238, NULL, 0, 0, 0, 1, 10000, 1000, '1', 'default config',
        '2024-02-24 22:28:47', '2024-02-24 22:28:47'),
 (3, 10249, NULL, 0, 0, 0, 1, 10000, 1000, '1', 'default config',
            '2024-02-24 22:28:47', '2024-02-24 22:28:47');
