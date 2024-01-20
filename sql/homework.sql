/*
Navicat MySQL Data Transfer

Source Server         : MySQL8
Source Server Version : 80032
Source Host           : localhost:3306
Source Database       : homework

Target Server Type    : MYSQL
Target Server Version : 80032
File Encoding         : 65001

Date: 2023-09-19 11:24:44
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
drop table IF EXISTS `admin`;
create TABLE `admin` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_code` varchar(40) NOT NULL,
  `name` varchar(32) DEFAULT NULL,
  `sex` tinyint(1) DEFAULT '1',
  `enabled` tinyint(1) DEFAULT '1',
  `password` varchar(41) DEFAULT NULL,
  `department` varchar(128) DEFAULT NULL,
  `phone` varchar(32) DEFAULT NULL,
  `email` varchar(128) DEFAULT NULL,
  `admin_type` int DEFAULT 0 comment '1:教师 2:实验员 3:系统管理员',
  `created_by` int DEFAULT NULL comment '创建人',
  `updated_by` int DEFAULT NULL comment '更新人',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP comment '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk` (`user_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of admin
-- ----------------------------
insert into `admin` VALUES ('3', 'root', '管理员23', '1', '1', '*E6CC90B878B948C35E92B003C792C46C58C4AF40', null, '11111', 'aaa', 3 , null, null, '2023-06-30 09:38:57', '2023-09-18 11:30:33');

-- ----------------------------
-- Table structure for admin_priv
-- ----------------------------
drop table IF EXISTS `admin_priv`;
create TABLE `admin_priv` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `admin_id` varchar(40) NOT NULL,
  `mod_id` varchar(64) NOT NULL,
  `priv` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `iUnique` (`admin_id`,`mod_id`,`priv`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of admin_priv
-- ----------------------------

-- ----------------------------
-- Table structure for department
-- ----------------------------
drop table IF EXISTS `department`;
create TABLE `department` (
  `id` int NOT NULL AUTO_INCREMENT,
  `department_name` varchar(50) DEFAULT NULL comment '部门名称',
  `contact` varchar(50) DEFAULT NULL comment '联系人',
  `contact_phone` varchar(100) DEFAULT NULL comment '联系电话',
  `description` varchar(255) DEFAULT NULL comment '描述',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP comment '更新时间',
  `created_by` varchar(50) DEFAULT NULL comment '联系电话',
  `updated_by` varchar(50) DEFAULT NULL comment '联系电话',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of department
-- ----------------------------

-- ----------------------------
-- Table structure for dict
-- ----------------------------
drop table IF EXISTS `dict`;
create TABLE `dict` (
  `id` int unsigned NOT NULL AUTO_INCREMENT comment '字典主键',
  `dict_type` varchar(255) DEFAULT NULL comment '字典类型',
  `dict_code` tinyint DEFAULT NULL comment '字典编码',
  `dict_value` varchar(255) DEFAULT NULL comment '字典值',
  `mutable` tinyint(1) DEFAULT '1',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP comment '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of dict
-- ----------------------------
insert into `dict` VALUES ('1', 'doc_type', '1', '全部', '1', '2021-01-01 08:00:00', '2022-07-06 09:40:18');
insert into `dict` VALUES ('2', 'doc_type', '2', '期刊', '1', '2021-01-01 08:00:00', '2022-07-06 09:40:22');
insert into `dict` VALUES ('3', 'doc_type', '3', '图书', '1', '2021-01-01 08:00:00', '2022-07-06 09:40:27');
insert into `dict` VALUES ('5', 'doc_type', '5', '报刊', '1', '2021-01-01 08:00:00', '2021-10-02 17:28:28');
insert into `dict` VALUES ('6', 'doc_type', '6', '学位论文', '1', '2021-01-01 08:00:00', '2022-07-10 15:02:53');
insert into `dict` VALUES ('10', 'doc_type', '10', '音视频', '1', '2021-01-01 08:00:00', '2021-07-09 15:41:49');
insert into `dict` VALUES ('11', 'doc_type', '11', '其他', '1', '2022-07-10 15:55:08', '2022-07-10 15:55:52');
insert into `dict` VALUES ('12', 'doc_type', '12', '测试', '1', '2022-07-16 09:08:06', '2022-07-16 09:08:11');

-- ----------------------------
-- Table structure for homework
-- ----------------------------
drop table IF EXISTS `homework`;
create TABLE `homework` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `course_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `finished` tinyint(1) DEFAULT '0',
  `description` varchar(1024) DEFAULT NULL,
  `created_by` int DEFAULT NULL,
  `updated_by` int DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of homework
-- ----------------------------

-- ----------------------------
-- Table structure for homework_dept
-- ----------------------------
drop table IF EXISTS `homework_dept`;
create TABLE `homework_dept` (
  `id` int NOT NULL AUTO_INCREMENT,
  `department_id` int NOT NULL comment '部门名称',
  `homework_id` int DEFAULT NULL comment '联系人',
  `created_by` int DEFAULT NULL comment '创建人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of homework_dept
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
drop table IF EXISTS `user`;
create TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_code` varchar(50) NOT NULL,
  `password` char(100) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  `sex` tinyint DEFAULT NULL,
  `identity_number` varchar(32) DEFAULT NULL,
  `email` varchar(64) DEFAULT NULL,
  `phone` varchar(32) DEFAULT NULL,
  `expired_at` date DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT '0',
  `local` tinyint(1) DEFAULT '1',
  `description` varchar(1024) DEFAULT NULL,
  `first_login_at` datetime DEFAULT NULL,
  `last_login_at` datetime DEFAULT NULL,
  `created_by` int DEFAULT NULL,
  `updated_by` int DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`user_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of user
-- ----------------------------

-- ----------------------------
-- Table structure for user_department
-- ----------------------------
drop table IF EXISTS `user_department`;
create TABLE `user_department` (
  `id` int NOT NULL AUTO_INCREMENT,
  `department_id` int NOT NULL comment '部门名称',
  `user_id` int DEFAULT NULL comment '联系人',
  `created_by` int DEFAULT NULL comment '创建人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of user_department
-- ----------------------------

-- ----------------------------
-- Table structure for user_homework
-- ----------------------------
drop table IF EXISTS `user_homework`;
create TABLE `user_homework` (
  `id` int NOT NULL AUTO_INCREMENT,
  `homework_id` int DEFAULT NULL,
  `sha1` varchar(60) DEFAULT '0',
  `file_size` int DEFAULT NULL,
  `file_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `ip_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `created_by` int DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of user_homework
-- ----------------------------

-- ----------------------------
-- New tables
-- ----------------------------

-- ----------------------------
-- Table structure for lab
-- ----------------------------
drop table IF EXISTS `lab`;
create TABLE `lab` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) DEFAULT NULL comment '实验室名称',
  `address` VARCHAR(50) DEFAULT NULL comment '实验室地点',
  `description` VARCHAR(100) DEFAULT NULL comment '实验室设施描述',
  `capacity` int DEFAULT NULL comment '实验室最大容纳人数',
  `area` int DEFAULT NULL comment '面积',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP comment '更新时间',
  `created_by` int DEFAULT NULL comment '创建者',
  `updated_by` int DEFAULT NULL comment '更新者',
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- ----------------------------
-- Records of lab
-- ----------------------------

-- ----------------------------
-- Table structure for admin_lab
-- ----------------------------
drop table IF EXISTS `admin_lab`;
create TABLE `admin_lab` (
  `id` int NOT NULL AUTO_INCREMENT,
  `admin_id` int DEFAULT NULL comment '关联用户' ,
  `lab_id` int NOT NULL comment '实验室id',
  `created_by` int DEFAULT NULL comment '创建人',
  unique KEY (admin_id, lab_id),
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- ----------------------------
-- Records of admin_lab
-- ----------------------------

-- ----------------------------
-- Table structure for schedule_lab
-- ----------------------------
drop table IF EXISTS `schedule_lab`;
create TABLE `schedule_lab` (
  `id` int ,
  `schedule_id` int ,
  `lab_id` int comment '实验室id',
  `created_by` int DEFAULT NULL comment '创建人',
  unique KEY (schedule_id, lab_id),
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- ----------------------------
-- Records of schedule_lab
-- ----------------------------

-- ----------------------------
-- Table structure for season
-- ----------------------------
drop table IF EXISTS `season`;
create TABLE `season` (
  `id` int NOT NULL AUTO_INCREMENT,
  `season_name` VARCHAR(100) DEFAULT NULL comment '学期名称',
  `started_at` DATETIME DEFAULT NULL comment '第一周开始日期',
  `enabled` tinyint(1) DEFAULT 1 comment '是否可用',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP comment '更新时间',
  `created_by` int DEFAULT NULL comment '创建者',
  `updated_by` int DEFAULT NULL comment '更新者',
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- ----------------------------
-- Records of season
-- ----------------------------

-- ----------------------------
-- Table structure for course_schedule
-- ----------------------------
drop table IF EXISTS `course_schedule`;
create TABLE `course_schedule` (
  `id` int NOT NULL AUTO_INCREMENT,
  `course_name` VARCHAR(50) DEFAULT NULL comment '课程名',
  `section` int DEFAULT NULL comment '课节(二进制位表示)',
  `week_range` int DEFAULT NULL comment '上课周范围(二进制位表示)',
  `week_day` int DEFAULT NULL comment '上课星期(二进制位表示)',
  `student_count` int DEFAULT NULL comment '上课人数',
  `status` int DEFAULT 0 NOT NULL comment '审核状态(0:未审核 1:通过 2:待修改 3:被拒绝)',
  `season_id` int DEFAULT NULL ,
  `description` VARCHAR(100) DEFAULT NULL comment '申请时的备注',
  `reason` VARCHAR(100) DEFAULT NULL comment '申请未通过的原因,由实验员(technician)填写',
  `room_count` int DEFAULT 0 comment '需要房间数',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP comment '更新时间',
  `created_by` int DEFAULT NULL comment '创建者(申请人)',
  `updated_by` int DEFAULT NULL comment '更新者',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- ----------------------------
-- Records of course_schedule
-- ----------------------------