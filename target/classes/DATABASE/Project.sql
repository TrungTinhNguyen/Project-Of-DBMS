create database Coffee_Chill;

use coffee_chill;

create table Nhan_Vien(
	id smallint not null auto_increment Primary key,
    hoten nvarchar(30) not null unique,
    chucvu nvarchar(30) not null,
    ngaysinh date not null check(ngaysinh <='2002-01-01'), 
	phone_number int,
    diachi nvarchar(50),
    ngaybd date not null
);
create table Tai_Khoan(
	username varchar(20) not null primary key,
    password varchar(6) not null,
    id int references Nhan_Vien(id),
    level TINYINT(1) DEFAULT '0' check(level<=2)			/*admin la 1, nhan vien la 2*/
);

create table Luong(		/*muc nay chi admin moi co quyen truy cap, thay doi va xem tat ca, nhan vien chi duoc xem luong ca nhan*/
	id int references Nhan_Vien(id),
    muc_luong float not null,
    thuong float not null
);
create table Loai_Tp(
	id_loai smallint not null primary key,
    Ten_loai varchar(10) check(food or drinks) not null    
);
create table Food(
	id_food smallint not null primary key,
    ten_food nvarchar(40) not null unique,
    id_loai smallint references Loai_Tp(id_loai),
    gia float not null
);
create table Drinks(
	id_drinks smallint not null primary key,
    ten_drinks nvarchar(40) not null unique,
    id_loai smallint references Loai_Tp(id_loai),
    gia float not null
);
create table Ban(
	id_ban tinyint not null primary key,
    kt_ban varchar(10) check(con_ban or het_ban),
    id_bill smallint references Bill(id_bill)
);
create table Bill(
	id_bill smallint not null primary key,
    id_ban tinyint references Ban(id_ban),
    id smallint references Nhan_Vien(id),
    ngay date not null check(ngay <= curdate()),
    id_food smallint references Food(id_food),
    id_drinks smallint references Drinks(id_drinks),
    count int default 0 not null
)


	






