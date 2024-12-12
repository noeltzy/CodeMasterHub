-- 用户表
-- 如果表存在则删除
DROP TABLE IF EXISTS `cmh_users`;
DROP TABLE IF EXISTS `cmh_login_logs`;

CREATE TABLE `cmh_users` (
                             `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                             `phoneNumber` varchar(11) NOT NULL COMMENT '手机号码',
                             `username` varchar(32) DEFAULT NULL COMMENT '用户名',
                             `nickname` varchar(32) NOT NULL COMMENT '用户昵称',
                             `avatar` varchar(255) DEFAULT NULL COMMENT '用户头像URL',
                             `role` varchar(20) NOT NULL DEFAULT 'USER' COMMENT '角色（ADMIN:管理员/USER:普通用户）',
                             `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态（1:正常 0:禁用）',
                             `lastLoginTime` datetime DEFAULT NULL COMMENT '最后登录时间',
                             `lastLoginIp` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
                             `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             `isDeleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除（1:是 0:否）',
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `uk_phoneNumber` (`phoneNumber`),
                             UNIQUE KEY `uk_username` (`username`),
                             KEY `idx_role_status` (`role`, `status`),
                             KEY `idx_createTime` (`createTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 用户登录日志表
CREATE TABLE `cmh_login_logs` (
                                  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                  `userId` bigint(20) NOT NULL COMMENT '用户ID',
                                  `loginIp` varchar(50) NOT NULL COMMENT '登录IP',
                                  `loginTime` datetime NOT NULL COMMENT '登录时间',
                                  `loginType` varchar(20) NOT NULL COMMENT '登录方式（手机号/用户名）',
                                  `deviceInfo` varchar(255) DEFAULT NULL COMMENT '设备信息',
                                  `status` tinyint(4) NOT NULL COMMENT '登录状态（1:成功 0:失败）',
                                  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `isDeleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除（1:是 0:否）',
                                  PRIMARY KEY (`id`),
                                  KEY `idx_userId` (`userId`),
                                  KEY `idx_loginTime` (`loginTime`),
                                  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户登录日志表';

-- 如果表存在则删除
DROP TABLE IF EXISTS `cmh_courses`;
DROP TABLE IF EXISTS `cmh_course_tags`;

-- 课程表
CREATE TABLE `cmh_courses` (
                               `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                               `name` varchar(100) NOT NULL COMMENT '课程名称',
                               `instructorId` bigint(20) NOT NULL COMMENT '讲师ID，关联讲师表',
                               `tags` varchar(255) DEFAULT NULL COMMENT '标签ID列表，多个以逗号分隔',
                               `price` decimal(10,2) NOT NULL COMMENT '课程价格',
                               `coverImage` varchar(255) NOT NULL COMMENT '封面图片URL',
                               `videoUrl` varchar(255) NOT NULL COMMENT '视频URL',
                               `duration` int(11) NOT NULL COMMENT '视频时长(秒)',
                               `description` text NOT NULL COMMENT '课程描述',
                               `studentCount` int(11) NOT NULL DEFAULT '0' COMMENT '学习人数',
                               `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态(1:上架 0:下架)',
                               `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               `isDeleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除（1:是 0:否）',
                               PRIMARY KEY (`id`),
                               KEY `idx_instructor` (`instructorId`),
                               KEY `idx_status` (`status`),
                               KEY `idx_create_time` (`createTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- 课程标签表
CREATE TABLE `cmh_course_tags` (
                                   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                   `name` varchar(32) NOT NULL COMMENT '标签名称',
                                   `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序号',
                                   `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态(1:启用 0:禁用)',
                                   `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                   `isDeleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除（1:是 0:否）',
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `uk_name` (`name`),
                                   KEY `idx_sort` (`sort`),
                                   KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程标签表';

-- 插入一些测试标签数据
INSERT INTO `cmh_course_tags` (`name`, `sort`, `status`) VALUES
                                                             ('Java基础', 1, 1),
                                                             ('Spring Boot', 2, 1),
                                                             ('微服务', 3, 1),
                                                             ('数据库', 4, 1),
                                                             ('前端开发', 5, 1),
                                                             ('DevOps', 6, 1),
                                                             ('架构设计', 7, 1),
                                                             ('项目实战', 8, 1);

-- 插入一些测试课程数据
INSERT INTO `cmh_courses` (`name`, `instructorId`, `tags`, `price`, `coverImage`, `videoUrl`, `duration`, `description`, `studentCount`, `status`) VALUES
                                                                                                                                                       ('Spring Boot入门到精通', 1, '2,8', 199.00, 'https://example.com/covers/springboot.jpg', 'https://example.com/videos/springboot.mp4', 3600, 'Spring Boot框架详细教程', 100, 1),
                                                                                                                                                       ('Java核心技术', 1, '1,8', 299.00, 'https://example.com/covers/java.jpg', 'https://example.com/videos/java.mp4', 7200, 'Java编程基础与进阶', 150, 1),
                                                                                                                                                       ('微服务架构实战', 2, '3,7,8', 399.00, 'https://example.com/covers/microservice.jpg', 'https://example.com/videos/microservice.mp4', 5400, '微服务架构设计与实现', 80, 1),
                                                                                                                                                       ('MySQL性能优化', 2, '4,7', 299.00, 'https://example.com/covers/mysql.jpg', 'https://example.com/videos/mysql.mp4', 4800, 'MySQL数据库优化技巧', 120, 1);
-- 课程收藏表
DROP TABLE IF EXISTS `cmh_course_favorites`;
CREATE TABLE `cmh_course_favorites` (
                                        `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                        `userId` bigint(20) NOT NULL COMMENT '用户ID',
                                        `courseId` bigint(20) NOT NULL COMMENT '课程ID',
                                        `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
                                        `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                        `isDeleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除（1:是 0:否）',
                                        PRIMARY KEY (`id`),
                                        UNIQUE KEY `uk_user_course` (`userId`, `courseId`),
                                        KEY `idx_user_id` (`userId`),
                                        KEY `idx_course_id` (`courseId`),
                                        KEY `idx_create_time` (`createTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程收藏表';