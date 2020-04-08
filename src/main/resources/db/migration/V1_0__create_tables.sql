----CREATE_STOCK------------------
CREATE TABLE STOCK (

   ID BIGINT NOT NULL,
   VERSION INTEGER NOT NULL,
   NAME VARCHAR(255 CHAR) NOT NULL,
   CURRENT_PRICE DECIMAL(19, 4) NOT NULL,
   LAST_UPDATE TIMESTAMP NOT NULL

);

ALTER TABLE STOCK ADD CONSTRAINT PK_STOCK PRIMARY KEY (ID);
----------------------------------

----CREATE_ARCHIVAL_STOCK_DATA----
CREATE TABLE ARCHIVAL_STOCK_DATA (

   RECORD_KEY VARCHAR(255 CHAR) NOT NULL,
   STOCK_ID BIGINT NOT NULL,
   PAYLOAD BLOB NOT NULL

);

ALTER TABLE ARCHIVAL_STOCK_DATA ADD CONSTRAINT PK_ARCHIVAL_STOCK_DATA PRIMARY KEY (RECORD_KEY, STOCK_ID);
----------------------------------