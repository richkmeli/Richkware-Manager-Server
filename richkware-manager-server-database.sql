CONNECT 'jdbc:derby://192.168.99.100:1527/RichkwareMS;create=true;user=richk;password=richk';

CREATE TABLE Device (
  Name           VARCHAR(50) NOT NULL PRIMARY KEY,
  IP             VARCHAR(25) NOT NULL,
  ServerPort     VARCHAR(10),
  LastConnection VARCHAR(25)
);