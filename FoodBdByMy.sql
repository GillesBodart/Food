
-- phpMyAdmin SQL Dump
-- version 3.4.11.1deb2+deb7u1
-- http://www.phpmyadmin.net
--
-- Client: localhost
-- Généré le: Mer 11 Mars 2015 à 19:47
-- Version du serveur: 5.5.41
-- Version de PHP: 5.4.36-0+deb7u3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données: `Food`
--

-- --------------------------------------------------------

--
-- Structure de la table `ALIMENT`
--

CREATE TABLE IF NOT EXISTS `ALIMENT` (
  `aliid` decimal(10,0) NOT NULL,
  `aliname` varchar(255) NOT NULL,
  `aliprovider` decimal(10,0) DEFAULT NULL,
  `alicategory` decimal(10,0) NOT NULL,
  `aliprix` decimal(10,4) NOT NULL,
  `aliunit` decimal(10,0) NOT NULL,
  PRIMARY KEY (`aliid`),
  KEY `alimentfkunit` (`aliunit`),
  KEY `alimentfkcategory` (`alicategory`),
  KEY `alimentfkprovider` (`aliprovider`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `CATEGORY`
--

CREATE TABLE IF NOT EXISTS `CATEGORY` (
  `catid` decimal(10,0) NOT NULL,
  `catname` varchar(255) NOT NULL,
  `catparent` decimal(10,0) DEFAULT NULL,
  PRIMARY KEY (`catid`),
  UNIQUE KEY `categoryunique` (`catname`),
  KEY `subcategory` (`catparent`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `FOODUSER`
--

CREATE TABLE IF NOT EXISTS `FOODUSER` (
  `usrid` decimal(10,0) NOT NULL,
  `usrfirstname` varchar(255) NOT NULL,
  `usrlastname` varchar(255) NOT NULL,
  `usrpassword` varchar(255) NOT NULL,
  `usraddress` varchar(255) NOT NULL,
  `usremail` varchar(255) NOT NULL,
  `usrtel` varchar(255) NOT NULL,
  PRIMARY KEY (`usrid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `INGREDIENT`
--

CREATE TABLE IF NOT EXISTS `INGREDIENT` (
  `ingid` decimal(10,0) NOT NULL,
  `ingrec` decimal(10,0) NOT NULL,
  `ingali` decimal(10,0) NOT NULL,
  `ingqty` decimal(10,0) NOT NULL,
  `ingunit` decimal(10,0) NOT NULL,
  PRIMARY KEY (`ingid`),
  UNIQUE KEY `ingredientunique` (`ingrec`,`ingali`),
  KEY `ingredientfkaliment` (`ingali`),
  KEY `ingredientfkunit` (`ingunit`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `ORDERS`
--

CREATE TABLE IF NOT EXISTS `ORDERS` (
  `ordid` decimal(10,0) NOT NULL,
  `ordname` varchar(255) NOT NULL,
  `ordprice` decimal(10,4) DEFAULT NULL,
  `ordowner` decimal(10,0) NOT NULL,
  PRIMARY KEY (`ordid`),
  KEY `orderfkuser` (`ordowner`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `PROVIDER`
--

CREATE TABLE IF NOT EXISTS `PROVIDER` (
  `proid` decimal(10,0) NOT NULL,
  `proname` varchar(255) NOT NULL,
  `proaddress` varchar(130) NOT NULL,
  PRIMARY KEY (`proid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `RECIPE`
--

CREATE TABLE IF NOT EXISTS `RECIPE` (
  `recid` decimal(10,0) NOT NULL,
  `recname` varchar(255) NOT NULL,
  `recprice` decimal(10,4) NOT NULL,
  `recserving` decimal(10,0) NOT NULL,
  `recowner` decimal(10,0) NOT NULL,
  `recprivate` decimal(1,0) NOT NULL,
  PRIMARY KEY (`recid`),
  KEY `recipefkuser` (`recowner`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `RECIPELIST`
--

CREATE TABLE IF NOT EXISTS `RECIPELIST` (
  `lstid` decimal(10,0) NOT NULL,
  `lstord` decimal(10,0) NOT NULL,
  `lstrec` decimal(10,0) NOT NULL,
  `lstrecqty` decimal(10,0) NOT NULL,
  PRIMARY KEY (`lstid`),
  UNIQUE KEY `listerecetteunique` (`lstord`,`lstrec`),
  KEY `recipelistfkrecipe` (`lstrec`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `SEQUENCES`
--

CREATE TABLE IF NOT EXISTS `SEQUENCES` (
  `seq_name` varchar(255) NOT NULL,
  `seq_count` decimal(10,0) NOT NULL,
  PRIMARY KEY (`seq_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `UNIT`
--

CREATE TABLE IF NOT EXISTS `UNIT` (
  `unitid` decimal(10,0) NOT NULL,
  `unitname` varchar(255) NOT NULL,
  `unitreport` decimal(10,10) NOT NULL,
  `unitparent` decimal(10,0) DEFAULT NULL,
  PRIMARY KEY (`unitid`),
  KEY `subunit` (`unitparent`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `ALIMENT`
--
ALTER TABLE `ALIMENT`
  ADD CONSTRAINT `alimentfkprovider` FOREIGN KEY (`aliprovider`) REFERENCES `PROVIDER` (`proid`),
  ADD CONSTRAINT `alimentfkcategory` FOREIGN KEY (`alicategory`) REFERENCES `CATEGORY` (`catid`),
  ADD CONSTRAINT `alimentfkunit` FOREIGN KEY (`aliunit`) REFERENCES `UNIT` (`unitid`);

--
-- Contraintes pour la table `CATEGORY`
--
ALTER TABLE `CATEGORY`
  ADD CONSTRAINT `subcategory` FOREIGN KEY (`catparent`) REFERENCES `CATEGORY` (`catid`);

--
-- Contraintes pour la table `INGREDIENT`
--
ALTER TABLE `INGREDIENT`
  ADD CONSTRAINT `ingredientfkunit` FOREIGN KEY (`ingunit`) REFERENCES `UNIT` (`unitid`),
  ADD CONSTRAINT `ingredientfkaliment` FOREIGN KEY (`ingali`) REFERENCES `ALIMENT` (`aliid`),
  ADD CONSTRAINT `ingredientfkrecipe` FOREIGN KEY (`ingrec`) REFERENCES `RECIPE` (`recid`);

--
-- Contraintes pour la table `ORDERS`
--
ALTER TABLE `ORDERS`
  ADD CONSTRAINT `orderfkuser` FOREIGN KEY (`ordowner`) REFERENCES `FOODUSER` (`usrid`);

--
-- Contraintes pour la table `RECIPE`
--
ALTER TABLE `RECIPE`
  ADD CONSTRAINT `recipefkuser` FOREIGN KEY (`recowner`) REFERENCES `FOODUSER` (`usrid`);

--
-- Contraintes pour la table `RECIPELIST`
--
ALTER TABLE `RECIPELIST`
  ADD CONSTRAINT `recipelistfkorder` FOREIGN KEY (`lstord`) REFERENCES `ORDERS` (`ordid`),
  ADD CONSTRAINT `recipelistfkrecipe` FOREIGN KEY (`lstrec`) REFERENCES `RECIPE` (`recid`);

--
-- Contraintes pour la table `UNIT`
--
ALTER TABLE `UNIT`
  ADD CONSTRAINT `subunit` FOREIGN KEY (`unitparent`) REFERENCES `UNIT` (`unitid`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
