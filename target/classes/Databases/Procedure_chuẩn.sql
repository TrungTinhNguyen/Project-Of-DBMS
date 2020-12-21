USE QuanLyQuanCafe;
-- thủ tục Login
delimiter //
drop procedure if exists Login;
create procedure Login(in user_name nvarchar(50), in passwd nvarchar(8), out check_a int)
begin
    set check_a =-1;
    select level into check_a from  Account
    where  user_name=username and passwd = password;
end //
delimiter ;
call login('TNhao', 123456, @check_a);
select @check_a;
-- tạo thủ tục get_user
delimiter //
drop procedure if exists get_user;
create procedure get_user (in user_name nvarchar(50))
begin
    select id_staff, fullname, possition, birthday, phone_number, address, date_start, username, password, level  from Staff, Account, Salary
    where id_staff = Account.idStaff and id_Staff=Salary.idStaff and username = user_name;
end //
delimiter ;
call get_user ('PVnhan');
-- tạo hàm thống kê
delimiter //
drop procedure if exists thong_ke;
create procedure thong_ke (in user_name nvarchar(50), in date_check date)
begin
    select c.id_bill, c.dateCheckIn, a.fullname, e.price from Staff a, Account b, Bill c, BillInfo d, Drinks e
    where  username = user_name and a.id_staff = b.idStaff and c.id_bill=d.idBill and d.idDrinks=e.id_drinks and dateCheckIn = date_check;
end //
delimiter ;
call thong_ke ('TNhao', '2020-12-17');
-- drop procedure thong_ke ;
