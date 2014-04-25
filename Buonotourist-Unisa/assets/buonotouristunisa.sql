-- phpMyAdmin SQL Dump
-- version 4.0.4.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generato il: Apr 25, 2014 alle 16:49
-- Versione del server: 5.5.32
-- Versione PHP: 5.4.19

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `buonotouristunisa`
--
CREATE DATABASE IF NOT EXISTS `buonotouristunisa` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `buonotouristunisa`;

-- --------------------------------------------------------

--
-- Struttura della tabella `appartenenza`
--

CREATE TABLE IF NOT EXISTS `appartenenza` (
  `OrarioFermata` time DEFAULT NULL,
  `CodiceFermataA` int(11) NOT NULL DEFAULT '0',
  `CodiceCorsaRA` int(11) NOT NULL DEFAULT '0',
  `OraPartenzaA` time NOT NULL DEFAULT '00:00:00',
  `AndataRitornoA` varchar(1) NOT NULL DEFAULT '',
  PRIMARY KEY (`CodiceFermataA`,`CodiceCorsaRA`,`OraPartenzaA`,`AndataRitornoA`),
  KEY `CodiceCorsaRA` (`CodiceCorsaRA`,`OraPartenzaA`,`AndataRitornoA`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `appartenenza`
--

INSERT INTO `appartenenza` (`OrarioFermata`, `CodiceFermataA`, `CodiceCorsaRA`, `OraPartenzaA`, `AndataRitornoA`) VALUES
('07:10:00', 1, 1, '00:07:00', 'A'),
('07:20:00', 2, 1, '00:07:00', 'A');

-- --------------------------------------------------------

--
-- Struttura della tabella `corsa`
--

CREATE TABLE IF NOT EXISTS `corsa` (
  `CodiceCorsa` int(11) NOT NULL AUTO_INCREMENT,
  `NomeCorsa` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`CodiceCorsa`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dump dei dati per la tabella `corsa`
--

INSERT INTO `corsa` (`CodiceCorsa`, `NomeCorsa`) VALUES
(1, 'Pomigliano - Fisciano/Lancusi');

-- --------------------------------------------------------

--
-- Struttura della tabella `corsareale`
--

CREATE TABLE IF NOT EXISTS `corsareale` (
  `CodiceCorsaR` int(11) NOT NULL DEFAULT '0',
  `OraPartenza` time NOT NULL DEFAULT '00:00:00',
  `AndataRitorno` varchar(1) NOT NULL DEFAULT '',
  PRIMARY KEY (`OraPartenza`,`CodiceCorsaR`,`AndataRitorno`),
  KEY `CodiceCorsaR` (`CodiceCorsaR`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `corsareale`
--

INSERT INTO `corsareale` (`CodiceCorsaR`, `OraPartenza`, `AndataRitorno`) VALUES
(1, '00:07:00', 'A');

-- --------------------------------------------------------

--
-- Struttura della tabella `fermata`
--

CREATE TABLE IF NOT EXISTS `fermata` (
  `CodiceFermata` int(11) NOT NULL AUTO_INCREMENT,
  `NomeFermata` varchar(100) DEFAULT NULL,
  `FermataLatitudine` double DEFAULT NULL,
  `FermataLongitudine` double DEFAULT NULL,
  `CodicePaeseF` int(11) DEFAULT NULL,
  PRIMARY KEY (`CodiceFermata`),
  KEY `CodicePaeseF` (`CodicePaeseF`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dump dei dati per la tabella `fermata`
--

INSERT INTO `fermata` (`CodiceFermata`, `NomeFermata`, `FermataLatitudine`, `FermataLongitudine`, `CodicePaeseF`) VALUES
(1, 'Via San Massimo', 565.4342, 123.334234, 1),
(2, 'Via Ferrovia', 455.2312, 231.23123, 1);

-- --------------------------------------------------------

--
-- Struttura della tabella `paese`
--

CREATE TABLE IF NOT EXISTS `paese` (
  `CodicePaese` int(11) NOT NULL AUTO_INCREMENT,
  `NomePaese` varchar(50) DEFAULT NULL,
  `UniversitaPaese` varchar(1) DEFAULT NULL,
  `BigliettoFisciano` double DEFAULT NULL,
  `BigliettoLancusi` double DEFAULT NULL,
  `AbbonamentoFisciano` double DEFAULT NULL,
  `AbbonamentoLancusi` double DEFAULT NULL,
  PRIMARY KEY (`CodicePaese`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dump dei dati per la tabella `paese`
--

INSERT INTO `paese` (`CodicePaese`, `NomePaese`, `UniversitaPaese`, `BigliettoFisciano`, `BigliettoLancusi`, `AbbonamentoFisciano`, `AbbonamentoLancusi`) VALUES
(1, 'Nola', 'P', 3, 3, 67.5, 67.5);

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `appartenenza`
--
ALTER TABLE `appartenenza`
  ADD CONSTRAINT `appartenenza_ibfk_1` FOREIGN KEY (`CodiceFermataA`) REFERENCES `fermata` (`CodiceFermata`),
  ADD CONSTRAINT `appartenenza_ibfk_2` FOREIGN KEY (`CodiceCorsaRA`, `OraPartenzaA`, `AndataRitornoA`) REFERENCES `corsareale` (`CodiceCorsaR`, `OraPartenza`, `AndataRitorno`);

--
-- Limiti per la tabella `corsareale`
--
ALTER TABLE `corsareale`
  ADD CONSTRAINT `corsareale_ibfk_1` FOREIGN KEY (`CodiceCorsaR`) REFERENCES `corsa` (`CodiceCorsa`);

--
-- Limiti per la tabella `fermata`
--
ALTER TABLE `fermata`
  ADD CONSTRAINT `fermata_ibfk_1` FOREIGN KEY (`CodicePaeseF`) REFERENCES `paese` (`CodicePaese`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
