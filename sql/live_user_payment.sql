CREATE DATABASE live_user_payment;
USE live_user_payment;
-- Create syntax for TABLE 't_payment_order'
CREATE TABLE `t_payment_order` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `order_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'order id',
  `product_id` int unsigned NOT NULL COMMENT 'product id',
  `status` tinyint unsigned NOT NULL DEFAULT '0' COMMENT 'status(0 for prepare,1 for paying,2 for payed,3 for cancelled,4 for invalid）',
  `user_id` bigint unsigned NOT NULL COMMENT 'user id',
  `pay_channel` tinyint unsigned NOT NULL COMMENT 'payment channel (0 for alipay, 1 for wechat 2 for union 3 console)',
  `source` tinyint unsigned NOT NULL COMMENT 'source',
  `pay_time` datetime DEFAULT NULL COMMENT 'payment success time',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'creation time',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_id` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=242 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create syntax for TABLE 't_currency_amount'
CREATE TABLE `t_currency` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary id',
  `name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'product name',
  `price` int DEFAULT '0' COMMENT 'product price',
  `extra` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'extention',
  `type` tinyint DEFAULT '0' COMMENT 'business type (0 for live stream)',
  `valid_status` tinyint DEFAULT '0' COMMENT 'status (0 for invalid, 1 for valid)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'creation time',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='payment product table';

INSERT INTO `t_currency` VALUES (6, '30¥', 30, '{\"coin\":300,\"url\":\"www.tttt.com\"}', 0, 1, '2024-02-22 16:32:28', '2024-02-22 16:56:22');
INSERT INTO `t_currency` VALUES (7, '35¥', 35, '{\"coin\":350,\"url\":\"www.tttt.com\"}', 0, 1, '2024-02-22 16:32:28', '2024-02-22 16:57:13');
INSERT INTO `t_currency` VALUES (8, '40¥', 40, '{\"coin\":400,\"url\":\"www.tttt.com\"}', 0, 1, '2024-02-22 16:32:28', '2024-02-22 16:57:18');
INSERT INTO `t_currency` VALUES (9, '50¥', 50, '{\"coin\":500,\"url\":\"www.tttt.com\"}', 0, 1, '2024-02-22 16:32:28', '2024-02-22 16:57:21');
INSERT INTO `t_currency` VALUES (10, '100¥', 100, '{\"coin\":1000,\"url\":\"www.tttt.com\"}', 0, 1, '2024-02-22 16:32:28', '2024-02-22 16:57:26');

-- Create syntax for TABLE 't_payment_callback_topic'
CREATE TABLE `t_payment_callback_topic` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `topic` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'mq topic',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0 for invalid, 1 for valid',
  `biz_code` int NOT NULL COMMENT 'business code',
  `remark` varchar(200) NOT NULL COMMENT 'description',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='payment topic table';

-- Create syntax for TABLE 't_balance_account'
CREATE TABLE `t_account_balance` (
  `user_id` bigint unsigned NOT NULL COMMENT 'user id',
  `current_balance` int DEFAULT NULL COMMENT 'current balance',
  `total_charged` int DEFAULT NULL COMMENT 'total charged',
  `status` tinyint DEFAULT '1' COMMENT 'account status(0 for invalid 1 for valid 2 for frozen)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='account balance table';

-- Create syntax for TABLE 't_transaction_turnover'
CREATE TABLE `t_transaction_turnover` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL COMMENT 'user id',
  `num` int DEFAULT NULL COMMENT 'turnover amount',
  `type` tinyint DEFAULT NULL COMMENT 'turnover type',
  `status` tinyint DEFAULT '1' COMMENT '0 for invalid 1 for valid',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=869 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='turnover table';