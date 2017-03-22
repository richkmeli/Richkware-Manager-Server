CONNECT 'jdbc:derby://localhost:1527/RichkwareMS;create=true;user=richk;password=richk';

create table Device(
    Name varchar(50) not null primary key,
    IP varchar(25) not null,
    ServerPort varchar(10),
    LastConnection varchar(25)
);