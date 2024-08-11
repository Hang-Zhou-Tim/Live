CREATE DATABASE live_msg;
USE live_msg;
CREATE TABLE `t_sms` (
                         `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
                         `code` int unsigned DEFAULT '0' COMMENT '验证码',
                         `phone` varchar(200) CHARACTER SET utf8mb4 COLLATE
                             utf8mb4_0900_ai_ci DEFAULT '' COMMENT '手机号',
                         `sendTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
                         `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE
                             CURRENT_TIMESTAMP COMMENT '更新时间',
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;