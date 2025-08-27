# 数据库初始化
# @author xucheng
# @since 2025-6-29

-- 创建数据库
CREATE DATABASE IF NOT EXISTS xcoj DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE xcoj;

-- 用户表
CREATE TABLE user (
    userId BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户id',
    username VARCHAR(256) NOT NULL COMMENT '用户名',
    password VARCHAR(512) NOT NULL COMMENT '用户密码(加密后)',
    userPhone VARCHAR(30) NULL COMMENT '用户电话号码',
    userEmail VARCHAR(128) NULL COMMENT '用户邮箱',
    avatarUrl VARCHAR(128) NULL COMMENT '用户头像链接，方便后续扩展业务',
    introduction VARCHAR(1024) NULL COMMENT '用户简洁，方便后续扩展业务',
    userRole VARCHAR(256) DEFAULT 'user' COMMENT '用户权限',
    acceptedNum INT DEFAULT 0 COMMENT '用户通过题目数',
    submitNum INT DEFAULT 0 COMMENT '用户提交题目数',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '用户创建时间',
    isDelete TINYINT DEFAULT 0 COMMENT '是否删除',
    UNIQUE KEY uq_user_name (username),
    UNIQUE KEY uq_user_email (userEmail),
    UNIQUE KEY uq_user_phone (userPhone)
) COMMENT='用户表';

-- 题目表
CREATE TABLE problem (
    problemId BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '题目id',
    title VARCHAR(512) NOT NULL COMMENT '题目标题',
    description TEXT NOT NULL COMMENT '题目描述',
    inputDescription TEXT NOT NULL COMMENT '输入描述',
    outputDescription TEXT NOT NULL COMMENT '输出描述',
    note TEXT NULL COMMENT '题目备注',
    sample TEXT NULL COMMENT '题目样例（json）',
    timeLimit int NOT NULL COMMENT '题目时间限制(MS)',
    memoryLimit int NOT NULL COMMENT '空间限制(KB)',
    judgeCaseURL VARCHAR(1024) NOT NULL COMMENT '判题数据地址',
    acceptedNum INT DEFAULT 0 COMMENT '通过数',
    submitNum INT DEFAULT 0 COMMENT '提交数',
    answerId BIGINT NULL COMMENT '题解文章id',
    userId BIGINT NOT NULL COMMENT '创建人id',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    isHidden TINYINT DEFAULT 1 COMMENT '是否隐藏',
    isDelete TINYINT DEFAULT 0 COMMENT '是否删除',
    INDEX idx_problem (userId)
) COMMENT='题目表';

-- 提交记录表
CREATE TABLE submission (
    submissionId BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '提交id',
    userId BIGINT NOT NULL COMMENT '用户id',
    problemId BIGINT NOT NULL COMMENT '题目id',
    contestId BIGINT NULL COMMENT '比赛id, 如果不为空, 则表示为比赛中的提交',
    lang varchar(64) NOT NULL COMMENT '编程语言',
    code TEXT NOT NULL COMMENT '提交代码',
    judgeResult VARCHAR(256) DEFAULT 'In queue' COMMENT '判题结果',
    usedTime INT DEFAULT 0 COMMENT '时间使用',
    usedMemory INT DEFAULT 0 COMMENT '空间使用',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    isDelete TINYINT DEFAULT 0 COMMENT '是否删除',
    INDEX idx_userId (userId),
    INDEX idx_problemId (problemId),
    INDEX idx_contestId (contestId)
) COMMENT='提交记录表';

-- 用户通过表
CREATE TABLE user_accept (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'id',
    userId BIGINT NOT NULL COMMENT '用户id',
    problemId BIGINT NOT NULL COMMENT '题目id',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_userId (userId),
    INDEX idx_problemId (problemId),
    UNIQUE KEY uq_user_problem (userId, problemId)
) COMMENT='用户通过题目表';

-- 比赛表
CREATE TABLE contest (
    contestId BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '比赛id',
    contestTitle VARCHAR(128) NOT NULL COMMENT '比赛名称',
    contestDescription TEXT NULL COMMENT '比赛描述',
    beginTime DATETIME NOT NULL COMMENT '比赛开始时间',
    endTime DATETIME NOT NULL COMMENT '比赛结束时间',
    userId BIGINT NOT NULL COMMENT '创建人id',
    isVisible TINYINT DEFAULT 0 COMMENT '是否对普通用户可见'
) COMMENT='比赛表';

-- 比赛题目映射表
CREATE TABLE contest_problem (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键id',
    contestId BIGINT NOT NULL COMMENT '比赛id',
    problemId BIGINT NOT NULL COMMENT '题目id',
    problemIndex VARCHAR(10) COMMENT '题目在比赛中的编号（如 A、B、C）',
    acceptedNum INT DEFAULT 0 COMMENT '通过数',
    submitNum INT DEFAULT 0 COMMENT '提交数',
    INDEX idx_contest (contestId),
    INDEX idx_problem (problemId),
    UNIQUE KEY uq_contest_problem (contestId, problemId)
) COMMENT='比赛题目映射表';

-- 比赛中用户每题的状态表
CREATE TABLE contest_problem_status (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    contestId BIGINT NOT NULL COMMENT '比赛ID',
    userId BIGINT NOT NULL COMMENT '用户ID',
    problemId BIGINT NOT NULL COMMENT '题目ID',
    problemIndex CHAR(10) NOT NULL COMMENT '比赛题号（如 A、B、C）',
    firstAcTime INT DEFAULT NULL COMMENT '首次AC所用时间（分钟）',
    wrongCount INT DEFAULT 0 COMMENT 'AC前的错误提交次数',
    isAc TINYINT DEFAULT 0 COMMENT '是否通过（1表示通过，0表示未通过）',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    UNIQUE KEY uq_contest_user_problem (contestId, userId, problemId),
    INDEX idx_contest_user (contestId, userId),
    INDEX idx_problem (problemId)
) COMMENT='比赛中用户每题的状态表';

-- 标签表
CREATE TABLE tag (
    tagId BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '标签id',
    name VARCHAR(128) NOT NULL UNIQUE COMMENT '标签名称'
) COMMENT='标签表';

-- 题目标签映射表
CREATE TABLE problem_tag (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键id',
    problemId BIGINT NOT NULL COMMENT '题目id',
    tagId BIGINT NOT NULL COMMENT '标签id',
    INDEX idx_problem (problemId),
    INDEX idx_tag (tagId),
    UNIQUE KEY uq_problem_tag (problemId, tagId)
) COMMENT='题目标签映射表';

-- 博客文章表
CREATE TABLE article (
    articleId BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '文章id',
    title VARCHAR(256) NOT NULL COMMENT '文章标题',
    content LONGTEXT NOT NULL COMMENT '文章内容',
    authorId BIGINT NOT NULL COMMENT '作者id',
    isPublic TINYINT DEFAULT 1 COMMENT '是否公开',
    viewCount INT DEFAULT 0 COMMENT '浏览量',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    isDelete TINYINT DEFAULT 0 COMMENT '是否删除',
    INDEX idx_author (authorId)
) COMMENT='博客文章表';

-- 文字评论表
CREATE TABLE article_comment (
    commentId BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评论id',
    articleId BIGINT NOT NULL COMMENT '评论所属文章',
    parentId BIGINT DEFAULT NULL COMMENT '父评论id',
    userId BIGINT NOT NULL COMMENT '评论用户id',
    content TEXT NOT NULL COMMENT '评论内容',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
    isDelete TINYINT DEFAULT 0 COMMENT '是否删除',
    INDEX idx_article (articleId),
    INDEX idx_parent (parentId),
    INDEX idx_article_parent (articleId, parentId)
) COMMENT='博客评论表';

