DROP TABLE IF EXISTS revenue_recognition;
CREATE TABLE revenue_recognition(contract LONG,recognized_on DATE,amount int,PRIMARY KEY(contract,recognized_on));