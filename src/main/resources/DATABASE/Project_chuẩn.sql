CREATE DATABASE QuanLyQuanCafe;

USE QuanLyQuanCafe;

CREATE TABLE Staff (
    id_staff INT AUTO_INCREMENT PRIMARY KEY,
    fullname NVARCHAR(50) NOT NULL,
    possition NVARCHAR(50) NOT NULL DEFAULT N'Nhân viên',
    birthday DATE NOT NULL,
    phone_number int null,
    address nvarchar(50),
    date_start date not null
);

CREATE TABLE Account (
    username NVARCHAR(50) PRIMARY KEY,
    idStaff INT,
    password NVARCHAR(8) NOT NULL,
    level tinyint(1) NOT NULL DEFAULT 0,
    FOREIGN KEY (idStaff)
        REFERENCES Staff (id_staff)
);

CREATE TABLE Salary (
    idStaff INT PRIMARY KEY,
    salary float NOT NULL,
    bonus float not null default 0,
    FOREIGN KEY (idStaff)
        REFERENCES Staff (id_staff)
);

CREATE TABLE DrinksCategory (
    id smallint AUTO_INCREMENT PRIMARY KEY,
    name NVARCHAR(40) NOT NULL DEFAULT N'NUL'
);

CREATE TABLE Drinks (
    id_drinks smallint AUTO_INCREMENT PRIMARY KEY,
    name NVARCHAR(50) NOT NULL DEFAULT N'NUl' unique,
    idCategory smallint,
    price float UNSIGNED NOT NULL DEFAULT 0,
    FOREIGN KEY (idCategory)
        REFERENCES DrinksCategory (id)
);

CREATE TABLE TableDrinks (
    id_table tinyint AUTO_INCREMENT PRIMARY KEY,
    name NVARCHAR(30) NOT NULL DEFAULT N'NUL',
    status smallint NOT NULL DEFAULT 0
 /*   id_bill smallint references Bill(id_bill) */
);

CREATE TABLE Bill (
	id_bill smallint AUTO_INCREMENT PRIMARY KEY,
    id_table tinyint,
	idStaff int,
    dateCheckIn DATE not NULL ,
    status INT NOT NULL DEFAULT 0, -- 0: CHUA THANH TOAN, 1: DA THANH TOAN
    FOREIGN KEY (id_table)
		REFERENCES TableDrinks (id_table),
	FOREIGN KEY (idStaff)
        REFERENCES Staff (id_staff)
);

CREATE TABLE BillInfo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idBill smallint,
    idDrinks smallint,
    amountOf INT NOT NULL DEFAULT 0,
    FOREIGN KEY (idBill)
        REFERENCES Bill (id_bill),
    FOREIGN KEY (idDrinks)
        REFERENCES Drinks (id_Drinks)
);
	-- insert values to staff
insert into staff (fullname, possition, birthday, phone_number, address, date_start)
 value (N'Nguyễn Thị Linh', N'Quản lí', '1995-01-02', 0336666999, 'Quận Ninh Kiều-Cần Thơ', '2019-01-01');
insert into staff (fullname, possition, birthday, phone_number, address, date_start)
 value (N'Trần Hùng', N'Bảo vệ', '1999-02-04', 0339999111, 'Phong Điền-Cần Thơ', '2020-03-01');
 insert into staff (fullname, possition, birthday, phone_number, address, date_start)
 value (N'Phan Hữu Nhân', N'Phục vụ', '2000-02-05', 0339999222, 'Giồng Riềng-Kiên Giang', '2020-05-05');
 insert into staff (fullname, possition, birthday, phone_number, address, date_start)
 value (N'Huỳnh Kim Vy', N'Phục vụ', '2000-02-02', 0339999444, 'Giồng Riềng-Kiên Giang', '2020-04-01');
 insert into staff (fullname, possition, birthday, phone_number, address, date_start)
 value (N'Vương Như Hảo', N'Thu Ngân', '2000-06-04', 0339999555, 'Phong Điền-Cần Thơ', '2020-03-05');
 insert into staff (fullname, possition, birthday, phone_number, address, date_start)
 value (N'Nguyễn Thị Pha Chế', N'Pha chế', '1999-02-04', 0339999999, 'Cái Răng-Cần Thơ', '2019-07-07');
	-- insert values to account
insert into account (username, idStaff, password, level)
value (n'QLlinh', 01, 123456, 0);
insert into account (username, idStaff, password, level)
value (n'BVhung', 02, 123456, 1);
insert into account (username, idStaff, password, level)
value (n'PVnhan', 03, 123456, 1);
insert into account (username, idStaff, password, level)
value (n'PVvy', 04, 123456, 1);
insert into account (username, idStaff, password, level)
value (n'TNhao', 05, 123456, 1);
insert into account (username, idStaff, password, level)
value (n'PCche', 06, 123456, 1);
select * from drinks;
	-- insert values to Salary
insert into salary (idStaff, salary, bonus) value (2, 2500000, 0);
insert into salary (idStaff, salary, bonus) value (3, 2500000, 0);
insert into salary (idStaff, salary, bonus) value (4, 2500000, 0);
insert into salary (idStaff, salary, bonus) value (5, 3000000, 0);
insert into salary (idStaff, salary, bonus) value (6, 4000000, 0);
	-- insert values to tabledrinks
insert into TableDrinks (name) values (n'Bàn 1');
insert into TableDrinks (name) values (n'Bàn 2');
insert into TableDrinks (name) values (n'Bàn 3');
insert into TableDrinks (name) values (n'Bàn 4');
insert into TableDrinks (name) values (n'Bàn 5');
insert into TableDrinks (name) values (n'Bàn 6');
insert into TableDrinks (name) values (n'Bàn 7');
insert into TableDrinks (name) values (n'Bàn 8');
insert into TableDrinks (name) values (n'Bàn 9');
	-- insert values to DrinksCategory
insert into DrinksCategory (name) values (n'Cà phê');
insert into DrinksCategory (name) values (n'Nước ngọt');
insert into DrinksCategory (name) values (n'Trà Sữa');
	-- insert values to Drinks
insert into Drinks (name, idCategory, price) values (n'Cà phê đen',1 ,20000);
insert into Drinks (name, idCategory, price) values (n'Cà phê sữa',1 ,28000);
insert into Drinks (name, idCategory, price) values (n'Espresso',1 ,20000);
insert into Drinks (name, idCategory, price) values (n'Sting đỏ',2 ,15000);
insert into Drinks (name, idCategory, price) values (n'Mirinda Cam',2 ,15000);
insert into Drinks (name, idCategory, price) values (n'Mirinda Đá Me',2 ,15000);
insert into Drinks (name, idCategory, price) values (n'Mirinda Soda Kem',2 ,15000);
insert into Drinks (name, idCategory, price) values (n'Mirinda Xá Xị',2 ,15000);
insert into Drinks (name, idCategory, price) values (n'Trà Đào',3 ,28000);
insert into Drinks (name, idCategory, price) values (n'Trà Sữa Matcha',3 ,30000);
insert into Drinks (name, idCategory, price) values (n'Trà Sữa Vị Socola',3 ,30000);
insert into Drinks (name, idCategory, price) values (n'Trà Sữa Việt Quất',3 ,30000);
insert into Drinks (name, idCategory, price) values (n'Trà Sữa Trân Châu Đường Đen',3 ,35000);
insert into Drinks (name, idCategory, price) values (n'Trà Sữa Trân Châu Đường Nâu',3 ,35000);
insert into Drinks (name, idCategory, price) values (n'Cacao Sữa Nóng',3 ,30000);
