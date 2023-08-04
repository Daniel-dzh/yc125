#银行事务管理,存款,取款,转账

#创建数据库
create database testBank;
use testBank;
#账号信息表 auto_increament: 使用java 的 UUID--》字符串类型
create table accounts
(
    accountid int primary key auto_increment,
    balance numeric(10,2)
);

#创建流水表
create table oprecord
(
    id int primary key auto_increment,
    accountid int references accounts(accountid),
    opmoney numeric(10,2),
    optime datetime,
    optype enum('deposite','withdraw','transfer') not null,
    transferid varchar(50)
)

alter table accounts add constraint ck_balance check ( balance>0 )