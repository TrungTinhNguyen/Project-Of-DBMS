USE QuanLyQuanCafe;
	-- thủ tục Login
delimiter //
create procedure Login(in user_name nvarchar(50), in passwd nvarchar(8), out check_a int)
begin
	set check_a =-1;
select level into check_a from  account
	where  user_name=username and passwd = password;
end //
delimiter ;
call login('TNhao', 123456, @check_a);
select @check_a;
	-- tạo thủ tục get_user
    delimiter //
create procedure get_username (in user_name nvarchar(50))
begin
	select id_staff, fullname, possition, birthday, phone_number, address, date_start from Staff join account  
	where id_staff = idStaff and username = user_name;
end //
delimiter ;
call get_username ('PVnhan');
	-- tạo hàm thống kê
  delimiter //
create procedure thong_ke (in user_name nvarchar(50), in date_check date)
begin
	select c.id_bill, c.dateCheckIn, a.fullname, e.price from Staff a, Account b, Bill c, Billinfo d, Drinks e
	where  username = user_name and a.id_staff = b.idStaff and c.id_bill=d.idBill and d.idDrinks=e.id_drinks and dateCheckIn = date_check;
end //
delimiter ;
call thong_ke ('TNhao', '2020-12-17');
-- drop procedure thong_ke ;
