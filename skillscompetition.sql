/*
 This file is part of Skills Competition.
 Copyright (C) 2023 Melon Studio All rights reserved.

 Author:
 AbuLan <xiaofan6@foxmail.com>

 This software, including documentation, is protected by copyright controlled
 by Melon Studio All rights are reserved.

 Source Server         : SkillsCompetition
 Source Server Type    : MySQL
 Source Server Version : 80012 (8.0.12)
 Source Host           : localhost:25565
 Source Schema         : skills Competition

 Target Server Type    : MySQL
 Target Server Version : 80012 (8.0.12)
 File Encoding         : 65001

 Date: 30/11/2023 11:31:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sc_rank
-- ----------------------------
DROP TABLE IF EXISTS `sc_rank`;
CREATE TABLE `sc_rank`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `work_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '学号',
  `score` double NOT NULL COMMENT '分数',
  `ranking` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '排名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sc_score
-- ----------------------------
DROP TABLE IF EXISTS `sc_score`;
CREATE TABLE `sc_score`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '评分ID',
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户ID',
  `work_id` int(11) NULL DEFAULT NULL COMMENT '作品ID',
  `score` double NULL DEFAULT NULL COMMENT '分数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sc_system
-- ----------------------------
DROP TABLE IF EXISTS `sc_system`;
CREATE TABLE `sc_system`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '比赛届数',
  `start_time` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '比赛开始时间',
  `end_time` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '比赛截止时间',
  `file_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件上传路径',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

INSERT INTO `sc_system` VALUES (1, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sc_user
-- ----------------------------
DROP TABLE IF EXISTS `sc_user`;
CREATE TABLE `sc_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sid` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `username` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `usertype` int(11) NOT NULL,
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `college` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` int(11) NOT NULL,
  `professional` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `classes` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `grade` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `usertype`(`usertype`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

INSERT INTO `sc_user` VALUES (1, '1000000000', 'admin', '4e07f64a17adf113e575e59ba7fa6b2e', 2, NULL, NULL, 1, NULL, NULL, NULL);
-- ----------------------------
-- Table structure for sc_usertype
-- ----------------------------
DROP TABLE IF EXISTS `sc_usertype`;
CREATE TABLE `sc_usertype`  (
  `typeID` int(11) NOT NULL,
  `name` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`typeID`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sc_work
-- ----------------------------
DROP TABLE IF EXISTS `sc_work`;
CREATE TABLE `sc_work`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sid` bigint(10) NOT NULL COMMENT '绑定的SID',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '解压的路径',
  `score` double NULL DEFAULT NULL COMMENT '评分',
  `score_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '评分组成员',
  `time` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '上传日期',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作品名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

