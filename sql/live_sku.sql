-- Create syntax for TABLE 't_sku_info'
CREATE TABLE `t_sku_info` (
                              `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary id',
                              `sku_id` int unsigned NOT NULL DEFAULT '0' COMMENT 'sku id',
                              `sku_price` int unsigned NOT NULL DEFAULT '0' COMMENT 'sku price',
                              `sku_code` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'sku code',
                              `name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT 'product name',
                              `icon_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'contracted img',
                              `original_icon_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'original img',
                              `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'product description',
                              `status` tinyint unsigned NOT NULL DEFAULT '0' COMMENT 'status(0 for invalid，1 for valid)',
                              `category_id` int NOT NULL COMMENT 'category id',
                              `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'creation date',
                              `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update time',
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='product sku information table';

-- Create syntax for TABLE 't_sku_order_info'
CREATE TABLE `t_sku_order_info` (
                                    `id` int unsigned NOT NULL AUTO_INCREMENT,
                                    `sku_id_list` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                    `user_id` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'user id',
                                    `room_id` int unsigned NOT NULL DEFAULT '0' COMMENT 'room id',
                                    `status` int unsigned NOT NULL DEFAULT '0' COMMENT 'status(0 for invalid，1 for valid)',
                                    `extra` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'remark',
                                    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='sku order table';

-- Create syntax for TABLE 't_sku_stock_info'
CREATE TABLE `t_sku_stock_info` (
                                    `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary id',
                                    `sku_id` int unsigned NOT NULL DEFAULT '0' COMMENT 'sku id',
                                    `stock_num` int unsigned NOT NULL DEFAULT '0' COMMENT 'sku remaining',
                                    `status` tinyint unsigned NOT NULL DEFAULT '0' COMMENT 'state(0 invalid, 1 for valid)',
                                    `version` int unsigned DEFAULT NULL COMMENT 'optimistic lock',
                                    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'creation date',
                                    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update time',
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='sku stock table';

-- Create syntax for TABLE 't_anchor_shop_info'
CREATE TABLE `t_anchor_shop_info` (
                                      `id` int unsigned NOT NULL AUTO_INCREMENT,
                                      `anchor_id` int unsigned NOT NULL DEFAULT '0' COMMENT 'anchor id',
                                      `sku_id` int unsigned NOT NULL DEFAULT '0' COMMENT 'sku id',
                                      `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT 'status(0 for invalid，1 for valid)',
                                      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'creation date',
                                      `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update date',
                                      PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='anchor config table';

-- Create syntax for TABLE 't_category_info'
CREATE TABLE `t_category_info` (
                                   `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary id',
                                   `level` int unsigned NOT NULL DEFAULT '0' COMMENT 'category id',
                                   `parent_id` int unsigned NOT NULL DEFAULT '0' COMMENT 'parent id',
                                   `category_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'category name',
                                   `status` tinyint unsigned NOT NULL DEFAULT '0' COMMENT 'status(0 for invalid，1 for valid)',
                                   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'creation date',
                                   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update time',
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='category table';