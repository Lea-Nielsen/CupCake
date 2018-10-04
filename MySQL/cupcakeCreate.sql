-- MySQL Workbench Forward Engineering

-- SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
-- SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
-- SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP DATABASE IF EXISTS cupcake;

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema cupcake
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema cupcake
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `cupcake` DEFAULT CHARACTER SET utf8 ;
USE `cupcake` ;

DROP TABLE IF EXISTS orderdetails;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS cupcake;
DROP TABLE IF EXISTS toppings;
DROP TABLE IF EXISTS bottoms;

-- -----------------------------------------------------
-- Table `cupcake`.`bottoms`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cupcake`.`bottoms` (
  `bottomname` VARCHAR(45) NOT NULL,
  `bottomprice` FLOAT NOT NULL,
  PRIMARY KEY (`bottomname`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `cupcake`.`toppings`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cupcake`.`toppings` (
  `toppingname` VARCHAR(45) NOT NULL,
  `toppingprice` FLOAT NOT NULL,
  PRIMARY KEY (`toppingname`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `cupcake`.`cupcake`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cupcake`.`cupcake` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `cupcakename` VARCHAR(45) NOT NULL,
  `cupcakeprice` FLOAT NOT NULL,
  `t_toppingname` VARCHAR(45) NOT NULL,
  `b_bottomname` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_cupcake_toppings1_idx` (`t_toppingname` ASC),
  INDEX `fk_cupcake_bottoms1_idx` (`b_bottomname` ASC),
  CONSTRAINT `fk_cupcake_toppings1`
    FOREIGN KEY (`t_toppingname`)
    REFERENCES `cupcake`.`toppings` (`toppingname`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_cupcake_bottoms1`
    FOREIGN KEY (`b_bottomname`)
    REFERENCES `cupcake`.`bottoms` (`bottomname`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `cupcake`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cupcake`.`user` (
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `balance` FLOAT NOT NULL,
  `email` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`username`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `cupcake`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cupcake`.`orders` (
  `order_id` INT(11) NOT NULL AUTO_INCREMENT,
  `u_username` VARCHAR(45) NOT NULL,
  `ordertotalprice` FLOAT NOT NULL,
  `orderdate` date NOT NULL,
  PRIMARY KEY (`order_id`),
  INDEX `u_username_idx` (`u_username` ASC),
  CONSTRAINT `fk_u_username`
    FOREIGN KEY (`u_username`)
    REFERENCES `cupcake`.`user` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `cupcake`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cupcake`.`orderdetails` (
  `order_id` INT(11) NOT NULL,
  `c_id` INT NOT NULL,
  `qty` INT NOT NULL,
  `totalprice` FLOAT NOT NULL,
  PRIMARY KEY (`order_id`, `c_id`),
  INDEX `c_id_idx` (`c_id` ASC),
  CONSTRAINT `fk_c_id`
    FOREIGN KEY (`c_id`)
    REFERENCES `cupcake`.`cupcake` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_id`
    FOREIGN KEY (`order_id`)
    REFERENCES `cupcake`.`orders` (`order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- SET SQL_MODE=@OLD_SQL_MODE;
-- SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
-- SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
