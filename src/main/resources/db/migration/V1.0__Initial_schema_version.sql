-- MySQL dump 10.13  Distrib 5.7.19, for macos10.12 (x86_64)
--
-- Host: localhost    Database: codeservice
-- ------------------------------------------------------
-- Server version	5.7.19

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
-- Table structure for table `app`
--

DROP TABLE IF EXISTS `app`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_on` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `modified_on` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `partner_id` bigint(20) DEFAULT NULL,
  `access_token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_app_partner_id` (`partner_id`),
  CONSTRAINT `FK_app_partner_id` FOREIGN KEY (`partner_id`) REFERENCES `referral_partner` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `content`
--

DROP TABLE IF EXISTS `content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `content` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_on` datetime DEFAULT NULL,
  `eidr` varchar(255) DEFAULT NULL,
  `eidrv` varchar(255) DEFAULT NULL,
  `gtm` varchar(255) DEFAULT NULL,
  `modified_on` datetime DEFAULT NULL,
  `msrp` decimal(19,2) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `studio_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_content_studio_id` (`studio_id`),
  CONSTRAINT `FK_content_studio_id` FOREIGN KEY (`studio_id`) REFERENCES `studio` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `content_retailer`
--

DROP TABLE IF EXISTS `content_retailer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `content_retailer` (
  `content_id` bigint(20) NOT NULL,
  `retailer_id` bigint(20) NOT NULL,
  PRIMARY KEY (`content_id`,`retailer_id`),
  KEY `FK_cr_retailer_id` (`retailer_id`),
  CONSTRAINT `FK_cr_content_id` FOREIGN KEY (`content_id`) REFERENCES `content` (`id`),
  CONSTRAINT `FK_cr_retailer_id` FOREIGN KEY (`retailer_id`) REFERENCES `retailer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `flyway_schema_history`
--

# DROP TABLE IF EXISTS `flyway_schema_history`;
# /*!40101 SET @saved_cs_client     = @@character_set_client */;
# /*!40101 SET character_set_client = utf8 */;
# CREATE TABLE `flyway_schema_history` (
#   `installed_rank` int(11) NOT NULL,
#   `version` varchar(50) DEFAULT NULL,
#   `description` varchar(200) NOT NULL,
#   `type` varchar(20) NOT NULL,
#   `script` varchar(1000) NOT NULL,
#   `checksum` int(11) DEFAULT NULL,
#   `installed_by` varchar(100) NOT NULL,
#   `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
#   `execution_time` int(11) NOT NULL,
#   `success` tinyint(1) NOT NULL,
#   PRIMARY KEY (`installed_rank`),
#   KEY `flyway_schema_history_s_idx` (`success`)
# ) ENGINE=InnoDB DEFAULT CHARSET=latin1;
# /*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `master_code`
--

DROP TABLE IF EXISTS `master_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `master_code` (
  `code` varchar(255) NOT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_on` datetime DEFAULT NULL,
  `app_id` bigint(20) NOT NULL,
  `content_id` bigint(20) DEFAULT NULL,
  `partner_id` bigint(20) DEFAULT NULL,
  `status` enum('UNALLOCATED','ISSUED','PAIRED','REDEEMED') NOT NULL DEFAULT 'UNALLOCATED',
  `modified_on` datetime DEFAULT NULL,
  PRIMARY KEY (`code`),
  KEY `FK_master_app_id` (`app_id`),
  KEY `FK_master_content_id` (`content_id`),
  KEY `FK_master_partner_id` (`partner_id`),
  CONSTRAINT `FK_master_content_id` FOREIGN KEY (`content_id`) REFERENCES `content` (`id`),
  CONSTRAINT `FK_master_partner_id` FOREIGN KEY (`partner_id`) REFERENCES `referral_partner` (`id`),
  CONSTRAINT `FK_master_app_id` FOREIGN KEY (`app_id`) REFERENCES `app` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pairing`
--

DROP TABLE IF EXISTS `pairing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pairing` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `master_code` varchar(255) NOT NULL,
  `retailer_code` varchar(255) NOT NULL,
  `paired_by` varchar(255) DEFAULT NULL,
  `paired_on` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_pairing_retailer_code` (`retailer_code`),
  KEY `FK_pairing_master_code` (`master_code`),
  CONSTRAINT `FK_pairing_retailer_code` FOREIGN KEY (`retailer_code`) REFERENCES `retailer_code` (`code`),
  CONSTRAINT `FK_pairing_master_code` FOREIGN KEY (`master_code`) REFERENCES `master_code` (`code`),
  CONSTRAINT `UQ_pairing_retailer_code` UNIQUE (`retailer_code`),
  CONSTRAINT `UQ_pairing_master_code` UNIQUE (`master_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `partner_retailer`
--

DROP TABLE IF EXISTS `partner_retailer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `partner_retailer` (
  `partner_id` bigint(20) NOT NULL,
  `retailer_id` bigint(20) NOT NULL,
  PRIMARY KEY (`partner_id`,`retailer_id`),
  KEY `FK_pr_retailer_id` (`retailer_id`),
  CONSTRAINT `FK_pr_partner_id` FOREIGN KEY (`partner_id`) REFERENCES `referral_partner` (`id`),
  CONSTRAINT `FK_pr_retailer_id` FOREIGN KEY (`retailer_id`) REFERENCES `retailer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `referral_partner`
--

DROP TABLE IF EXISTS `referral_partner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `referral_partner` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `contact_email` varchar(255) DEFAULT NULL,
  `contact_name` varchar(255) DEFAULT NULL,
  `contact_phone` varchar(255) DEFAULT NULL,
  `created_on` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `modified_on` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `region_code` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `retailer`
--

DROP TABLE IF EXISTS `retailer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `retailer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_on` datetime DEFAULT NULL,
  `modified_on` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `region_code` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `retailer_code`
--

DROP TABLE IF EXISTS `retailer_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `retailer_code` (
  `code` varchar(255) NOT NULL,
  `created_on` datetime DEFAULT NULL,
  `format` varchar(255) DEFAULT NULL,
  `content_id` bigint(20) DEFAULT NULL,
  `retailer_id` bigint(20) DEFAULT NULL,
  `modified_on` datetime DEFAULT NULL,
  `status` enum('UNALLOCATED','PAIRED','REDEEMED') NOT NULL DEFAULT 'UNALLOCATED',
  PRIMARY KEY (`code`),
  KEY `FK_rc_content_id` (`content_id`),
  KEY `FK_rc_retailer_id` (`retailer_id`),
  CONSTRAINT `FK_rc_content_id` FOREIGN KEY (`content_id`) REFERENCES `content` (`id`),
  CONSTRAINT `FK_rc_retailer_id` FOREIGN KEY (`retailer_id`) REFERENCES `retailer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `studio`
--

DROP TABLE IF EXISTS `studio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `studio` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code_prefix` varchar(255) NOT NULL,
  `contact_email` varchar(255) DEFAULT NULL,
  `contact_name` varchar(255) DEFAULT NULL,
  `contact_phone` varchar(255) DEFAULT NULL,
  `created_on` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `flags` bigint(20) DEFAULT NULL,
  `modified_on` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-02-08 14:29:00
