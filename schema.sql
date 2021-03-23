drop schema bookstore;
create schema bookstore;
use bookstore;

CREATE TABLE BOOK ( ISBN int PRIMARY KEY,
					Title VARCHAR(200) not null,
					PublisherID int null,
                    PubYear Date,
                    Price INT not null,
                    Quantity INT not null,
                    Threshold INT not null,
                    Category VARCHAR(200));

CREATE TABLE AUTHOR ( ISBN int,
					AuthName VARCHAR(200),
                    primary key (ISBN, AuthName));

CREATE TABLE PUBLISHER ( PublisherID int PRIMARY KEY,
					PubName VARCHAR(200) not null,
					Address VARCHAR(200),
                    Phone VARCHAR(15));

CREATE TABLE ORDERS ( OrderID int AUTO_INCREMENT PRIMARY KEY,
					 ISBN INT NOT NULL,
					 bookTitle VARCHAR(200),
					 Quantity INT NOT NULL,
                     OrderDate DATE);

CREATE TABLE USERS ( Email VARCHAR(200) PRIMARY KEY,
					 Username VARCHAR(200)  not null,
                     Password VARCHAR(200) not null,
					 Fname varchar(200) not null,
					 Lname varchar(200) not null,
                     Phone VARCHAR(15),
					 Address varchar(200));

CREATE TABLE MANAGERS ( Email VARCHAR(200) PRIMARY KEY);

create table CUSTOMER_CREDIT_CARD(
						CustomerEmail VARCHAR(200),
                        CardNumber int,
                        ExpiryDate date,
                        Password VARCHAR(10),
                        primary key (CustomerEmail, CardNumber));

ALTER TABLE BOOK ADD CONSTRAINT BFK FOREIGN KEY (PublisherID) REFERENCES PUBLISHER(PublisherID) ON DELETE SET NULL ON UPDATE SET NULL;
ALTER TABLE AUTHOR ADD CONSTRAINT AUFK FOREIGN KEY (ISBN) REFERENCES BOOK(ISBN) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE ORDERS ADD CONSTRAINT OFK FOREIGN KEY (ISBN) REFERENCES BOOK(ISBN) ON DELETE CASCADE ON UPDATE CASCADE;
alter table CUSTOMER_CREDIT_CARD ADD CONSTRAINT CCCFK FOREIGN KEY (CustomerEmail) references USERS(Email) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE MANAGERS ADD CONSTRAINT MNFK FOREIGN KEY (Email) REFERENCES USERS(Email) ON DELETE CASCADE ON UPDATE CASCADE;


DELIMITER //
CREATE TRIGGER non_negative_quantity
    BEFORE UPDATE
    ON BOOK FOR EACH ROW
begin
declare books INT default 0;

select NEW.Quantity
into books
from BOOK
where ISBN = NEW.ISBN;

if books < 0 then
  SIGNAL SQLSTATE '45000'
  SET MESSAGE_TEXT = 'Quantity cann\'t be negative';
end if ;
end //
DELIMITER ;

-- drop trigger order_if_less_threshold;

DELIMITER //
CREATE TRIGGER order_if_less_threshold
    AFTER UPDATE
    ON BOOK FOR EACH ROW
begin
declare bookQuantity, bookThreshold INT default 0;
declare bTitle varchar(200);

select NEW.Quantity, Threshold, Title
into bookQuantity, bookThreshold, bTitle
from BOOK
where ISBN = NEW.ISBN;

-- place an order if quantity less than threshold
if bookQuantity < bookThreshold then
	INSERT INTO ORDERS(ISBN, Quantity, bookTitle, OrderDate) VALUES(NEW.ISBN, bookThreshold + 10, bTitle, CURDATE());
end if ;
end //
DELIMITER ;

DELIMITER //
CREATE TRIGGER confirm_order
    BEFORE DELETE
    ON ORDERS FOR EACH ROW
begin
declare bookQuantity INT default 0;

select OLD.Quantity
into bookQuantity
from BOOK
where BOOK.ISBN = OLD.ISBN;

update BOOK
set BOOK.Quantity = BOOK.Quantity + bookQuantity
where BOOK.ISBN = OLD.ISBN;
end //
DELIMITER ;

-- ALTER USER ''root''@''localhost'' IDENTIFIED BY ''12345678'';
-- ALTER TABLE PUBLISHER MODIFY  COLUMN phone VARCHAR(15);