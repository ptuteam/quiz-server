-- MySQL dump 10.13  Distrib 5.5.46, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: quiz_db
-- ------------------------------------------------------
-- Server version	5.5.46-0ubuntu0.14.04.2

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `answer`
--

DROP TABLE IF EXISTS `answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `answer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `text` text,
  `question_id` int(11) DEFAULT NULL,
  `is_correct` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_answer_question_idx` (`question_id`),
  CONSTRAINT `fk_answer_question` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answer`
--

LOCK TABLES `answer` WRITE;
/*!40000 ALTER TABLE `answer` DISABLE KEYS */;
INSERT INTO `answer` VALUES (1,'answer 1, question 1',1,1),(2,'answer 2, question 1',1,0),(3,'answer 3, question 1',1,0),(4,'answer 4, question 1',1,0),(5,'answer 1, question 2',2,0),(6,'answer 2, question 2',2,1),(7,'answer 3, question 2',2,0),(8,'answer 4, question 2',2,0),(9,'answer 1, question 3',3,0),(10,'answer 2, question 3',3,0),(11,'answer 3, question 3',3,1),(12,'answer 4, question 3',3,0),(13,'answer 1, question 4',4,0),(14,'answer 2, question 4',4,0),(15,'answer 3, question 4',4,0),(16,'answer 4, question 4',4,1),(17,'answer 1, question 5',5,1),(18,'answer 2, question 5',5,0),(19,'answer 3, question 5',5,0),(20,'answer 4, question 5',5,0),(21,'answer 1, question 6',6,1),(22,'answer 2, question 6',6,0),(23,'answer 3, question 6',6,0),(24,'answer 4, question 6',6,0),(25,'answer 1, question 7',7,1),(26,'answer 2, question 7',7,0),(27,'answer 3, question 7',7,0),(28,'answer 4, question 7',7,0),(29,'answer 1, question 8',8,1),(30,'answer 2, question 8',8,0),(31,'answer 3, question 8',8,0),(32,'answer 4, question 8',8,0),(33,'answer 1, question 9',9,1),(34,'answer 2, question 9',9,0),(35,'answer 3, question 9',9,0),(36,'answer 4, question 9',9,0),(37,'answer 1, question 10',10,1),(38,'answer 2, question 10',10,0),(39,'answer 3, question 10',10,0),(40,'answer 4, question 10',10,0),(41,'answer 1, question 11',11,1),(42,'answer 2, question 11',11,0),(43,'answer 3, question 11',11,0),(44,'answer 4, question 11',11,0),(45,'answer 1, question 12',12,1),(46,'answer 2, question 12',12,0),(47,'answer 3, question 12',12,0),(48,'answer 4, question 12',12,0),(49,'answer 1, question 13',13,1),(50,'answer 2, question 13',13,0),(51,'answer 3, question 13',13,0),(52,'answer 4, question 13',13,0),(53,'answer 1, question 14',14,1),(54,'answer 2, question 14',14,0),(55,'answer 3, question 14',14,0),(56,'answer 4, question 14',14,0),(57,'answer 1, question 15',15,1),(58,'answer 2, question 15',15,0),(59,'answer 3, question 15',15,0),(60,'answer 4, question 15',15,0);
/*!40000 ALTER TABLE `answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `question` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `text` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES (1,'question1'),(2,'question2'),(3,'question3'),(4,'question4'),(5,'question5'),(6,'question6'),(7,'question7'),(8,'question8'),(9,'question9'),(10,'question10'),(11,'question11'),(12,'question12'),(13,'question13'),(14,'question14'),(15,'question15');
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-11-13  0:22:32
