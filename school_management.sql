-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Client :  127.0.0.1
-- Généré le :  Lun 16 Juin 2025 à 18:19
-- Version du serveur :  5.6.17
-- Version de PHP :  5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données :  `school_management`
--

-- --------------------------------------------------------

--
-- Structure de la table `absences`
--

CREATE TABLE IF NOT EXISTS `absences` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` datetime(6) NOT NULL,
  `reason` varchar(255) NOT NULL,
  `classe_id` bigint(20) NOT NULL,
  `student_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK25so1arjhu48hftdyk303jme3` (`classe_id`),
  KEY `FKrpq757dcd0kr3im7c2bjc5hkw` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `classes`
--

CREATE TABLE IF NOT EXISTS `classes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `capacity` int(11) NOT NULL,
  `level` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `teacher_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKpgs3gcxax70h9jugbt24ugwcg` (`name`),
  KEY `FK8td8h5k21lq8jax2h6oobm9l0` (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `notes`
--

CREATE TABLE IF NOT EXISTS `notes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `comment` varchar(255) DEFAULT NULL,
  `date` datetime(6) NOT NULL,
  `value` double NOT NULL,
  `classe_id` bigint(20) NOT NULL,
  `student_id` bigint(20) NOT NULL,
  `subject_id` bigint(20) NOT NULL,
  `teacher_id` bigint(20) NOT NULL,
  `term` enum('FIRST_TERM','SECOND_TERM','THIRD_TERM') NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKngiedqx0jgt4do780m0rb7d9k` (`classe_id`),
  KEY `FKiqrho6jkif8kmmnu3bglnl4bd` (`student_id`),
  KEY `FKh4rhmcyry4jlp1f6fht2xvag3` (`subject_id`),
  KEY `FKqc8x85fedia66robupoaj4eq4` (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `parents`
--

CREATE TABLE IF NOT EXISTS `parents` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKc1t2v6wf187l8w0yew9sph3l4` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `schedules`
--

CREATE TABLE IF NOT EXISTS `schedules` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `day_of_week` enum('FRIDAY','MONDAY','SATURDAY','SUNDAY','THURSDAY','TUESDAY','WEDNESDAY') DEFAULT NULL,
  `end_time` time(6) DEFAULT NULL,
  `room` varchar(255) DEFAULT NULL,
  `start_time` time(6) DEFAULT NULL,
  `class_id` bigint(20) DEFAULT NULL,
  `subject_id` bigint(20) DEFAULT NULL,
  `teacher_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKe9px6t0yucpeap743s7dvjnr1` (`class_id`),
  KEY `FK7tl4569066j839d7hd28xwuw8` (`subject_id`),
  KEY `FK66835t8mhqb22dmdyx8p6etjf` (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `students`
--

CREATE TABLE IF NOT EXISTS `students` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `matricule` varchar(255) DEFAULT NULL,
  `class_id` bigint(20) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK1psraxut9bwn2why6ex4xf1en` (`matricule`),
  UNIQUE KEY `UKg4fwvutq09fjdlb4bb0byp7t` (`user_id`),
  KEY `FKhnslh0rm5bthlble8vjunbnwe` (`class_id`),
  KEY `FK7bbpphkk8f0aoav3iiih3mh4e` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `subjects`
--

CREATE TABLE IF NOT EXISTS `subjects` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `coefficient` double DEFAULT NULL,
  `description` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKaodt3utnw0lsov4k9ta88dbpr` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `teachers`
--

CREATE TABLE IF NOT EXISTS `teachers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `specialization` varchar(255) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKcd1k6xwg9jqtiwx9ybnxpmoh9` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `teacher_subjects`
--

CREATE TABLE IF NOT EXISTS `teacher_subjects` (
  `teacher_id` bigint(20) NOT NULL,
  `subject_id` bigint(20) NOT NULL,
  PRIMARY KEY (`teacher_id`,`subject_id`),
  KEY `FKdweqkwxroox2u7pbmksehx04i` (`subject_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `avatar` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` enum('ADMIN','PARENT','STUDENT','TEACHER') DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

--
-- Contenu de la table `users`
--

INSERT INTO `users` (`id`, `avatar`, `email`, `firstname`, `lastname`, `password`, `role`, `enabled`) VALUES
(1, NULL, 'admin@school.com', 'Admin', 'System', '$2a$10$8jOs7qNmmLmL/baaVcGMCu9a295vP1Sq0SHhf4/qTDZgu3ouflrRy', 'ADMIN', b'1'),
(2, NULL, 'student@school.com', 'John', 'Student', '$2a$10$.6vl/puTZhCtKhiCICShSeX52fv3.DIZMeNfH0dqPT3cfPLMC5XOu', 'STUDENT', b'1'),
(3, NULL, 'parent@school.com', 'Jane', 'Parent', '$2a$10$0JVfsv390iOkvphq0wkfsOWkBLqTqt/gkrleVG9.IQhhTufVAZa1q', 'PARENT', b'1'),
(4, NULL, 'teacher@school.com', 'Robert', 'Teacher', '$2a$10$yiZx6Jw3mTqgpL.QfBb21uclsPT4/IIBV9xnITbmAXHzmSkBVlYmy', 'TEACHER', b'1');

--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `absences`
--
ALTER TABLE `absences`
  ADD CONSTRAINT `FKrpq757dcd0kr3im7c2bjc5hkw` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`),
  ADD CONSTRAINT `FK25so1arjhu48hftdyk303jme3` FOREIGN KEY (`classe_id`) REFERENCES `classes` (`id`);

--
-- Contraintes pour la table `classes`
--
ALTER TABLE `classes`
  ADD CONSTRAINT `FK8td8h5k21lq8jax2h6oobm9l0` FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`id`);

--
-- Contraintes pour la table `notes`
--
ALTER TABLE `notes`
  ADD CONSTRAINT `FKh4rhmcyry4jlp1f6fht2xvag3` FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`id`),
  ADD CONSTRAINT `FKiqrho6jkif8kmmnu3bglnl4bd` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`),
  ADD CONSTRAINT `FKngiedqx0jgt4do780m0rb7d9k` FOREIGN KEY (`classe_id`) REFERENCES `classes` (`id`),
  ADD CONSTRAINT `FKqc8x85fedia66robupoaj4eq4` FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`id`);

--
-- Contraintes pour la table `parents`
--
ALTER TABLE `parents`
  ADD CONSTRAINT `FKchh8tf8w072tapgqoijrahojk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Contraintes pour la table `schedules`
--
ALTER TABLE `schedules`
  ADD CONSTRAINT `FK66835t8mhqb22dmdyx8p6etjf` FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`id`),
  ADD CONSTRAINT `FK7tl4569066j839d7hd28xwuw8` FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`id`),
  ADD CONSTRAINT `FKe9px6t0yucpeap743s7dvjnr1` FOREIGN KEY (`class_id`) REFERENCES `classes` (`id`);

--
-- Contraintes pour la table `students`
--
ALTER TABLE `students`
  ADD CONSTRAINT `FKdt1cjx5ve5bdabmuuf3ibrwaq` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FK7bbpphkk8f0aoav3iiih3mh4e` FOREIGN KEY (`parent_id`) REFERENCES `parents` (`id`),
  ADD CONSTRAINT `FKhnslh0rm5bthlble8vjunbnwe` FOREIGN KEY (`class_id`) REFERENCES `classes` (`id`);

--
-- Contraintes pour la table `teachers`
--
ALTER TABLE `teachers`
  ADD CONSTRAINT `FKb8dct7w2j1vl1r2bpstw5isc0` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Contraintes pour la table `teacher_subjects`
--
ALTER TABLE `teacher_subjects`
  ADD CONSTRAINT `FK6dcl3ihufp4v0j1fuxlw4ksoj` FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`id`),
  ADD CONSTRAINT `FKdweqkwxroox2u7pbmksehx04i` FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
