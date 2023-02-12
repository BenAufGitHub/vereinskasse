-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: localhost    Database: vereinskasse
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `aktion`
--

DROP TABLE IF EXISTS `aktion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `aktion` (
  `aktion_id` int unsigned NOT NULL AUTO_INCREMENT,
  `datum` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `handlung` enum('belasten','einzahlen','begleichen','verwerfen') NOT NULL,
  `user_id` int unsigned NOT NULL,
  `schuld_id` int unsigned DEFAULT NULL,
  `guthaben_id` int unsigned DEFAULT NULL,
  PRIMARY KEY (`aktion_id`),
  KEY `fk_aktion_user_idx` (`user_id`),
  KEY `fk_guthaben_aktion_idx` (`guthaben_id`),
  KEY `fk_schuld_aktion_idx` (`schuld_id`),
  CONSTRAINT `fk_guthaben_aktion` FOREIGN KEY (`guthaben_id`) REFERENCES `guthaben` (`guthaben_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_schuld_aktion` FOREIGN KEY (`schuld_id`) REFERENCES `schuld` (`schuld_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_user_aktion` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aktion`
--

LOCK TABLES `aktion` WRITE;
/*!40000 ALTER TABLE `aktion` DISABLE KEYS */;
/*!40000 ALTER TABLE `aktion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `guthaben`
--

DROP TABLE IF EXISTS `guthaben`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `guthaben` (
  `guthaben_id` int unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int unsigned NOT NULL,
  `betrag` int unsigned NOT NULL,
  `neuer_stand` int NOT NULL,
  PRIMARY KEY (`guthaben_id`),
  KEY `fk_user_guthaben_idx` (`user_id`),
  CONSTRAINT `fk_user_guthaben` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guthaben`
--

LOCK TABLES `guthaben` WRITE;
/*!40000 ALTER TABLE `guthaben` DISABLE KEYS */;
/*!40000 ALTER TABLE `guthaben` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `guthaben_AFTER_INSERT` AFTER INSERT ON `guthaben` FOR EACH ROW BEGIN
	INSERT INTO aktion (handlung, user_id, guthaben_id) VALUES ('einzahlen', NEW.user_id, NEW.guthaben_id);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `schuld`
--

DROP TABLE IF EXISTS `schuld`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `schuld` (
  `schuld_id` int unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int unsigned NOT NULL,
  `grund` varchar(100) DEFAULT '',
  `betrag` int unsigned NOT NULL,
  `abbezahlt` int DEFAULT NULL,
  PRIMARY KEY (`schuld_id`),
  KEY `fk_user_schuld_idx` (`user_id`),
  CONSTRAINT `fk_user_schuld` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schuld`
--

LOCK TABLES `schuld` WRITE;
/*!40000 ALTER TABLE `schuld` DISABLE KEYS */;
/*!40000 ALTER TABLE `schuld` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `schuld_AFTER_INSERT` AFTER INSERT ON `schuld` FOR EACH ROW BEGIN
	INSERT INTO aktion (user_id, schuld_id, handlung) VALUES (NEW.user_id, NEW.schuld_id, 'belasten');
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `schuld_AFTER_DELETE` AFTER DELETE ON `schuld` FOR EACH ROW BEGIN
	INSERT INTO aktion (user_id, handlung) VALUES (OLD.user_id, 'verwerfen');
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` int unsigned NOT NULL AUTO_INCREMENT,
  `vorname` varchar(45) NOT NULL,
  `nachname` varchar(45) NOT NULL,
  `erstellt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gutschrift` int unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `user_konten`
--

DROP TABLE IF EXISTS `user_konten`;
/*!50001 DROP VIEW IF EXISTS `user_konten`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `user_konten` AS SELECT 
 1 AS `user_id`,
 1 AS `user_name`,
 1 AS `schulden inkl. gutschrift`*/;
SET character_set_client = @saved_cs_client;

--
-- Dumping events for database 'vereinskasse'
--

--
-- Dumping routines for database 'vereinskasse'
--
/*!50003 DROP FUNCTION IF EXISTS `calc_schulden` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `calc_schulden`(id INT SIGNED) RETURNS int
BEGIN

	DECLARE length INT DEFAULT 0;
    DECLARE counter INT DEFAULT 0;
    DECLARE result INT DEFAULT 0;
    
    DECLARE temp_betrag INT DEFAULT 0;
    DECLARE temp_datum DATE;
    

    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
	BEGIN
	DROP TEMPORARY TABLE calc_schulden_temp1;
	END;
    
    CREATE TEMPORARY TABLE calc_schulden_temp1
    SELECT DATE(datum) as von, betrag 
	FROM schuld INNER JOIN aktion
	ON aktion.schuld_id = schuld.schuld_id
	WHERE aktion.user_id = id AND schuld.abbezahlt IS NULL AND aktion.handlung = 'belasten';
    
    SELECT COUNT(*) FROM calc_schulden_temp1 INTO length;
        
    WHILE counter < length DO
    
		SELECT von, betrag FROM calc_schulden_temp1 LIMIT counter, 1 INTO temp_datum, temp_betrag;
		SET result = result + get_zinsbetrag(temp_datum, temp_betrag);
        SET counter = counter + 1;
        
	END WHILE;
    DROP TEMPORARY TABLE calc_schulden_temp1;
RETURN result;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `get_zinsbetrag` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `get_zinsbetrag`(datum DATE, betrag INT) RETURNS int
BEGIN
	DECLARE result INT DEFAULT 0;
	SET result = 
		IF(
			adddate(curdate(), INTERVAL -10 DAY) <= datum, 
			betrag,
			FLOOR(betrag*1.5)
		);
RETURN result;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `beende_schuld` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `beende_schuld`(IN id INT UNSIGNED)
BEGIN
	DECLARE _user_id INT DEFAULT 0;
    DECLARE _datum TIMESTAMP;
    DECLARE _betrag INT DEFAULT 0;
    
    SELECT aktion.datum, schuld.user_id, schuld.betrag FROM schuld
    INNER JOIN aktion ON schuld.schuld_id = aktion.schuld_id
    WHERE id=schuld.schuld_id AND aktion.handlung='belasten' INTO _datum, _user_id, _betrag;
    
	UPDATE schuld SET abbezahlt=get_zinsbetrag(_datum, _betrag) WHERE id=schuld_id;
    INSERT INTO aktion (handlung, user_id, schuld_id) VALUES ('begleichen', _user_id, id);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `konto_ausgleichen` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `konto_ausgleichen`(IN id INT, IN menge INT)
BEGIN
	DECLARE _gutschrift INT DEFAULT 0;
    DECLARE _kontostand INT DEFAULT 0;
    
    SELECT gutschrift FROM user WHERE user_id=id INTO _gutschrift;
	SET _kontostand = calc_schulden(id);
	
    -- registriere gutschrift Veränderung (logging)
    IF NOT menge=0 THEN
		INSERT INTO guthaben (user_id, betrag, neuer_stand) VALUES (id, menge, menge-_kontostand);
        SET _gutschrift = _gutschrift + menge;
    END IF;
    
    -- begleiche schulden
    CALL naechste_schuld(id, @sref, @sbetrag);
    WHILE NOT isnull(@sref) AND @sbetrag <= _gutschrift DO
		SET _gutschrift = _gutschrift - @sbetrag;
        CALL beende_schuld(@sref);
        CALL naechste_schuld(id, @sref, @sbetrag);
	END WHILE;
    
    -- update gutschrift
    UPDATE user SET gutschrift = _gutschrift WHERE user_id=id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `naechste_schuld` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `naechste_schuld`(IN _user_id INT, OUT schuld_ref INT UNSIGNED, OUT schuld_betrag INT)
BEGIN
	DECLARE _datum DATE;
    
	SELECT schuld.schuld_id, betrag, datum FROM schuld
    INNER JOIN aktion ON aktion.schuld_id=schuld.schuld_id
    WHERE aktion.handlung='belasten' AND _user_id = schuld.user_id AND schuld.abbezahlt IS NULL
    ORDER BY aktion.datum, aktion.aktion_id
    LIMIT 1
    INTO schuld_ref, schuld_betrag, _datum;
    
    SET schuld_betrag = get_zinsbetrag(_datum, schuld_betrag);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Final view structure for view `user_konten`
--

/*!50001 DROP VIEW IF EXISTS `user_konten`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `user_konten` AS select `user`.`user_id` AS `user_id`,concat(`user`.`vorname`,' ',`user`.`nachname`) AS `user_name`,(`CALC_SCHULDEN`(`user`.`user_id`) - cast(`user`.`gutschrift` as signed)) AS `schulden inkl. gutschrift` from `user` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-02-12 16:06:00
