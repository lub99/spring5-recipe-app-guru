--
-- Run mysql docker container
-- docker run --name some-mysql3 -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -p 3306:3306 -d  mysql

CREATE DATABASE sfg_dev;
CREATE DATABASE sfg_prod;

CREATE USER 'sfg_dev_user'@'localhost' IDENTIFIED BY 'lubs';
CREATE USER 'sfg_prod_user'@'localhost' IDENTIFIED BY 'lubs';
CREATE USER 'sfg_dev_user'@'%' IDENTIFIED BY 'lubs';
CREATE USER 'sfg_prod_user'@'%' IDENTIFIED BY 'lubs';

GRANT SELECT ON sfg_dev.* to 'sfg_dev_user'@'localhost';
GRANT INSERT ON sfg_dev.* to 'sfg_dev_user'@'localhost';
GRANT UPDATE ON sfg_dev.* to 'sfg_dev_user'@'localhost';
GRANT DELETE ON sfg_dev.* to 'sfg_dev_user'@'localhost';
GRANT SELECT ON sfg_prod.* to 'sfg_prod_user'@'localhost';
GRANT INSERT ON sfg_prod.* to 'sfg_prod_user'@'localhost';
GRANT UPDATE ON sfg_prod.* to 'sfg_prod_user'@'localhost';
GRANT DELETE ON sfg_prod.* to 'sfg_prod_user'@'localhost';

GRANT SELECT ON sfg_dev.* to 'sfg_dev_user'@'%';
GRANT INSERT ON sfg_dev.* to 'sfg_dev_user'@'%';
GRANT UPDATE ON sfg_dev.* to 'sfg_dev_user'@'%';
GRANT DELETE ON sfg_dev.* to 'sfg_dev_user'@'%';
GRANT SELECT ON sfg_prod.* to 'sfg_prod_user'@'%';
GRANT INSERT ON sfg_prod.* to 'sfg_prod_user'@'%';
GRANT UPDATE ON sfg_prod.* to 'sfg_prod_user'@'%';
GRANT DELETE ON sfg_prod.* to 'sfg_prod_user'@'%';
