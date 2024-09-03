DROP DATABASE IF EXISTS live_stream_rooms;
CREATE DATABASE live_stream_rooms;
USE live_stream_rooms;
-- Create syntax for TABLE 't_live_stream_room'
CREATE TABLE `t_live_stream_room` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `anchor_id` bigint DEFAULT NULL COMMENT 'anchor id',
  `type` tinyint NOT NULL DEFAULT '0' COMMENT 'live room type(1 for normal，2 for pk)',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT 'status (0 invalid, 1 valid)',
  `room_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'EMPTY_STR' COMMENT 'live room name',
  `covert_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'live room front image',
  `watch_num` int DEFAULT '0' COMMENT 'view number',
  `good_num` int DEFAULT '0' COMMENT 'thumbs up number',
  `start_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'start time',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create syntax for TABLE 't_live_stream_room_record'
CREATE TABLE `t_live_stream_room_record` (
    `id` int unsigned NOT NULL AUTO_INCREMENT,
    `anchor_id` bigint DEFAULT NULL COMMENT 'anchor id',
    `type` tinyint NOT NULL DEFAULT '0' COMMENT 'live room type(1 for normal，2 for pk)',
    `status` tinyint NOT NULL DEFAULT '0' COMMENT 'status (0 invalid, 1 valid)',
    `room_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'EMPTY_STR' COMMENT 'live room name',
    `covert_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'live room front image',
    `watch_num` int DEFAULT '0' COMMENT 'view number',
    `good_num` int DEFAULT '0' COMMENT 'thumbs up number',
    `start_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'start time',
    `end_time` datetime DEFAULT NULL COMMENT 'end time',
    `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;