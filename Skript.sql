create database geeksForLess;
use geeksForLess;

CREATE TABLE expression (
    id INT AUTO_INCREMENT PRIMARY KEY,
    expression_value VARCHAR(255) NOT NULL,
    result DOUBLE NOT NULL
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;


#can be without this
INSERT INTO `geeksForLess`.`expression` (`id`, `expression_value`, `result`) VALUES 
('1', '20-55+3+200+20-9', 179.0),
('2', '20+(50+10)-200/40', 75.0),
('3', '20-((50-10)+200)', -220.0);