-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 12, 2023 at 11:04 AM
-- Server version: 10.4.19-MariaDB
-- PHP Version: 8.0.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `digiverse`
--

-- --------------------------------------------------------

--
-- Table structure for table `answer_detail`
--

CREATE TABLE `answer_detail` (
                                 `AnswerID` char(36) NOT NULL,
                                 `QuestionID` char(36) NOT NULL,
                                 `AnswerText` varchar(1000) DEFAULT NULL,
                                 `Score` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `answer_file`
--

CREATE TABLE `answer_file` (
                               `AnswerID` char(36) NOT NULL,
                               `FileID` char(36) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



--
-- Table structure for table `answer_header`
--

CREATE TABLE `answer_header` (
                                 `AnswerID` char(36) NOT NULL,
                                 `TaskID` char(36) NOT NULL,
                                 `UserID` char(36) NOT NULL,
                                 `Finished` tinyint(1) DEFAULT NULL,
                                 `Score` double DEFAULT NULL,
                                 `CreatedAt` datetime DEFAULT NULL,
                                 `FinishedAt` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- --------------------------------------------------------

--
-- Table structure for table `authcheck`
--

CREATE TABLE `authcheck` (
                             `DeviceName` varchar(255) NOT NULL,
                             `UserID` char(36) NOT NULL,
                             `expired` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `class_forum`
--

CREATE TABLE `class_forum` (
                               `ClassID` char(36) NOT NULL,
                               `ForumID` char(36) NOT NULL,
                               `UserID` char(36) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- --------------------------------------------------------

--
-- Table structure for table `class_member`
--

CREATE TABLE `class_member` (
                                `ClassID` char(36) NOT NULL,
                                `UserID` char(36) NOT NULL,
                                `Role` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Table structure for table `class_task`
--

CREATE TABLE `class_task` (
                              `ClassID` char(36) NOT NULL,
                              `TaskID` char(36) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- --------------------------------------------------------

--
-- Table structure for table `forum_comment`
--

CREATE TABLE `forum_comment` (
                                 `ForumID` char(36) NOT NULL,
                                 `CommentID` char(36) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `msclass`
--

CREATE TABLE `msclass` (
                           `ClassID` char(36) NOT NULL,
                           `ClassName` varchar(255) NOT NULL,
                           `ClassDesc` varchar(500) NOT NULL,
                           `ClassCode` varchar(10) DEFAULT NULL,
                           `ClassSubject` varchar(255) DEFAULT NULL,
                           `ClassImage` blob DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- --------------------------------------------------------

--
-- Table structure for table `mscomment`
--

CREATE TABLE `mscomment` (
                             `CommentID` char(36) NOT NULL,
                             `ReplyID` char(36) DEFAULT NULL,
                             `CommentText` varchar(1000) NOT NULL,
                             `UserID` char(36) NOT NULL,
                             `CreatedAt` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `msfile`
--

CREATE TABLE `msfile` (
                          `FileID` char(36) NOT NULL,
                          `FileName` varchar(255) NOT NULL,
                          `FileBlob` blob NOT NULL,
                          `FileType` char(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `msforum`
--

CREATE TABLE `msforum` (
                           `ForumID` char(36) NOT NULL,
                           `ForumText` varchar(1000) NOT NULL,
                           `CreatedAt` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- --------------------------------------------------------

--
-- Table structure for table `msquestion`
--

CREATE TABLE `msquestion` (
                              `QuestionID` char(36) NOT NULL,
                              `TaskID` char(36) NOT NULL,
                              `QuestionType` varchar(36) NOT NULL,
                              `QuestionText` varchar(1000) NOT NULL,
                              `QuestionChoice` varchar(1000) DEFAULT NULL,
                              `QuestionAnswer` varchar(36) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `mstask`
--

CREATE TABLE `mstask` (
                          `TaskID` char(36) NOT NULL,
                          `UserID` char(36) NOT NULL,
                          `TaskTitle` varchar(1000) DEFAULT NULL,
                          `TaskDesc` varchar(1000) DEFAULT NULL,
                          `TaskType` varchar(36) DEFAULT NULL,
                          `FileID` char(36) DEFAULT NULL,
                          `CreatedAt` datetime NOT NULL,
                          `DeadlineAt` datetime NOT NULL,
                          `Scored` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `msuser`
--

CREATE TABLE `msuser` (
                          `UserID` char(36) NOT NULL,
                          `UserName` varchar(255) NOT NULL,
                          `UserEmail` varchar(255) NOT NULL,
                          `UserPassword` varchar(255) NOT NULL,
                          `UserDOB` date NOT NULL,
                          `UserProfile` longblob DEFAULT NULL,
                          `UserColor` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- --------------------------------------------------------

--
-- Table structure for table `task_comment`
--

CREATE TABLE `task_comment` (
                                `TaskID` char(36) NOT NULL,
                                `CommentID` char(36) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `answer_detail`
--
ALTER TABLE `answer_detail`
    ADD PRIMARY KEY (`AnswerID`,`QuestionID`),
  ADD KEY `AnswerID` (`AnswerID`),
  ADD KEY `QuestionID` (`QuestionID`);

--
-- Indexes for table `answer_file`
--
ALTER TABLE `answer_file`
    ADD PRIMARY KEY (`AnswerID`,`FileID`);

--
-- Indexes for table `answer_header`
--
ALTER TABLE `answer_header`
    ADD PRIMARY KEY (`AnswerID`),
  ADD KEY `TaskID` (`TaskID`),
  ADD KEY `UserID` (`UserID`);

--
-- Indexes for table `authcheck`
--
ALTER TABLE `authcheck`
    ADD PRIMARY KEY (`DeviceName`),
  ADD KEY `UserID` (`UserID`);

--
-- Indexes for table `class_forum`
--
ALTER TABLE `class_forum`
    ADD PRIMARY KEY (`ClassID`,`ForumID`),
  ADD KEY `ForumID` (`ForumID`),
  ADD KEY `UserID` (`UserID`);

--
-- Indexes for table `class_member`
--
ALTER TABLE `class_member`
    ADD PRIMARY KEY (`ClassID`,`UserID`),
  ADD KEY `UserID` (`UserID`);

--
-- Indexes for table `class_task`
--
ALTER TABLE `class_task`
    ADD PRIMARY KEY (`ClassID`,`TaskID`),
  ADD KEY `TaskID` (`TaskID`);

--
-- Indexes for table `forum_comment`
--
ALTER TABLE `forum_comment`
    ADD PRIMARY KEY (`ForumID`,`CommentID`),
  ADD KEY `CommentID` (`CommentID`);

--
-- Indexes for table `msclass`
--
ALTER TABLE `msclass`
    ADD PRIMARY KEY (`ClassID`);

--
-- Indexes for table `mscomment`
--
ALTER TABLE `mscomment`
    ADD PRIMARY KEY (`CommentID`),
  ADD KEY `ReplyID` (`ReplyID`),
  ADD KEY `UserID` (`UserID`);

--
-- Indexes for table `msfile`
--
ALTER TABLE `msfile`
    ADD PRIMARY KEY (`FileID`);

--
-- Indexes for table `msforum`
--
ALTER TABLE `msforum`
    ADD PRIMARY KEY (`ForumID`);

--
-- Indexes for table `msquestion`
--
ALTER TABLE `msquestion`
    ADD PRIMARY KEY (`QuestionID`),
  ADD KEY `TaskId` (`TaskID`);

--
-- Indexes for table `mstask`
--
ALTER TABLE `mstask`
    ADD PRIMARY KEY (`TaskID`),
  ADD KEY `UserID` (`UserID`),
  ADD KEY `FileID` (`FileID`);

--
-- Indexes for table `msuser`
--
ALTER TABLE `msuser`
    ADD PRIMARY KEY (`UserID`),
  ADD UNIQUE KEY `UserEmail` (`UserEmail`);

--
-- Indexes for table `task_comment`
--
ALTER TABLE `task_comment`
    ADD PRIMARY KEY (`TaskID`,`CommentID`),
  ADD KEY `CommentID` (`CommentID`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `answer_detail`
--
ALTER TABLE `answer_detail`
    ADD CONSTRAINT `answer_detail_ibfk_1` FOREIGN KEY (`AnswerID`) REFERENCES `answer_header` (`AnswerID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `answer_detail_ibfk_2` FOREIGN KEY (`QuestionID`) REFERENCES `msquestion` (`QuestionID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `answer_header`
--
ALTER TABLE `answer_header`
    ADD CONSTRAINT `answer_header_ibfk_1` FOREIGN KEY (`TaskID`) REFERENCES `mstask` (`TaskID`),
  ADD CONSTRAINT `answer_header_ibfk_2` FOREIGN KEY (`UserID`) REFERENCES `msuser` (`UserID`);

--
-- Constraints for table `authcheck`
--
ALTER TABLE `authcheck`
    ADD CONSTRAINT `authcheck_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `msuser` (`UserID`);

--
-- Constraints for table `class_forum`
--
ALTER TABLE `class_forum`
    ADD CONSTRAINT `class_forum_ibfk_1` FOREIGN KEY (`ClassID`) REFERENCES `msclass` (`ClassID`),
  ADD CONSTRAINT `class_forum_ibfk_2` FOREIGN KEY (`ForumID`) REFERENCES `msforum` (`ForumID`),
  ADD CONSTRAINT `class_forum_ibfk_3` FOREIGN KEY (`UserID`) REFERENCES `msuser` (`UserID`);

--
-- Constraints for table `class_member`
--
ALTER TABLE `class_member`
    ADD CONSTRAINT `FK_Classroom` FOREIGN KEY (`ClassID`) REFERENCES `msclass` (`ClassID`) ON DELETE CASCADE,
  ADD CONSTRAINT `class_member_ibfk_1` FOREIGN KEY (`ClassID`) REFERENCES `msclass` (`ClassID`),
  ADD CONSTRAINT `class_member_ibfk_2` FOREIGN KEY (`UserID`) REFERENCES `msuser` (`UserID`);

--
-- Constraints for table `class_task`
--
ALTER TABLE `class_task`
    ADD CONSTRAINT `class_task_ibfk_1` FOREIGN KEY (`ClassID`) REFERENCES `msclass` (`ClassID`),
  ADD CONSTRAINT `class_task_ibfk_2` FOREIGN KEY (`TaskID`) REFERENCES `mstask` (`TaskID`);

--
-- Constraints for table `forum_comment`
--
ALTER TABLE `forum_comment`
    ADD CONSTRAINT `forum_comment_ibfk_1` FOREIGN KEY (`ForumID`) REFERENCES `msforum` (`ForumID`),
  ADD CONSTRAINT `forum_comment_ibfk_2` FOREIGN KEY (`CommentID`) REFERENCES `mscomment` (`CommentID`);

--
-- Constraints for table `mscomment`
--
ALTER TABLE `mscomment`
    ADD CONSTRAINT `mscomment_ibfk_1` FOREIGN KEY (`ReplyID`) REFERENCES `mscomment` (`CommentID`),
  ADD CONSTRAINT `mscomment_ibfk_2` FOREIGN KEY (`UserID`) REFERENCES `msuser` (`UserID`);

--
-- Constraints for table `msquestion`
--
ALTER TABLE `msquestion`
    ADD CONSTRAINT `msquestion_ibfk_1` FOREIGN KEY (`TaskID`) REFERENCES `mstask` (`TaskID`);

--
-- Constraints for table `mstask`
--
ALTER TABLE `mstask`
    ADD CONSTRAINT `answer_detail_ibfk_3` FOREIGN KEY (`FileID`) REFERENCES `msfile` (`FileID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `mstask_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `msuser` (`UserID`);

--
-- Constraints for table `task_comment`
--
ALTER TABLE `task_comment`
    ADD CONSTRAINT `task_comment_ibfk_1` FOREIGN KEY (`TaskID`) REFERENCES `mstask` (`TaskID`),
  ADD CONSTRAINT `task_comment_ibfk_2` FOREIGN KEY (`CommentID`) REFERENCES `mscomment` (`CommentID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
