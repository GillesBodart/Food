


DROP TABLE IF EXISTS FOODUSER CASCADE ; 

DROP TABLE IF EXISTS CATEGORY CASCADE; 

DROP TABLE IF EXISTS UNIT CASCADE; 

DROP TABLE IF EXISTS PROVIDER CASCADE;  

DROP TABLE IF EXISTS RECIPE CASCADE; 

DROP TABLE IF EXISTS INGREDIENT CASCADE; 

DROP TABLE IF EXISTS RECIPELIST CASCADE; 

DROP TABLE IF EXISTS ORDERS CASCADE; 

DROP TABLE IF EXISTS SEQUENCES CASCADE; 

DROP TABLE IF EXISTS ALIMENT CASCADE;

CREATE TABLE FOODUSER 
  ( 
     usrid        DECIMAL(10, 0) PRIMARY KEY, 
     usrfirstname VARCHAR(255) NOT NULL, 
     usrlastname  VARCHAR(255) NOT NULL, 
     usrpassword  VARCHAR(255) NOT NULL, 
     usraddress   VARCHAR(255) NOT NULL, 
     usremail     VARCHAR(255) NOT NULL, 
     usrtel       VARCHAR(255) NOT NULL 
  ); 

CREATE TABLE ALIMENT 
  ( 
     aliid       DECIMAL(10, 0) PRIMARY KEY, 
     aliname     VARCHAR(255) NOT NULL, 
     aliprovider DECIMAL(10, 0), 
     alicategory DECIMAL(10, 0) NOT NULL, 
     aliprix     DECIMAL(10, 4) NOT NULL, 
     aliunit     DECIMAL(10, 0) NOT NULL 
  ); 

CREATE TABLE CATEGORY 
  ( 
     catid     DECIMAL(10, 0) PRIMARY KEY, 
     catname   VARCHAR (255) NOT NULL, 
     catparent DECIMAL(10, 0), 
     CONSTRAINT categoryunique UNIQUE (catname) 
  ); 

CREATE TABLE RECIPE 
  ( 
     recid      DECIMAL(10, 0) PRIMARY KEY, 
     recname    VARCHAR(255) NOT NULL, 
     recprice   DECIMAL(10, 4) NOT NULL, 
     recserving DECIMAL(10, 0) NOT NULL, 
     recowner   DECIMAL(10, 0) NOT NULL, 
     recprivate DECIMAL(1) NOT NULL 
  ); 

CREATE TABLE INGREDIENT 
  ( 
     ingid   DECIMAL(10, 0) PRIMARY KEY, 
     ingrec  DECIMAL(10, 0) NOT NULL, 
     ingali  DECIMAL(10, 0) NOT NULL, 
     ingqty  DECIMAL(10, 0) NOT NULL, 
     ingunit DECIMAL(10, 0) NOT NULL, 
     CONSTRAINT ingredientunique UNIQUE (ingrec, ingali) 
  ); 

CREATE TABLE RECIPELIST 
  ( 
     lstid     DECIMAL(10, 0) PRIMARY KEY, 
     lstord    DECIMAL(10, 0) NOT NULL, 
     lstrec    DECIMAL(10, 0) NOT NULL, 
     lstrecqty DECIMAL(10, 0) NOT NULL, 
     CONSTRAINT listerecetteunique UNIQUE (lstord, lstrec) 
  ); 

CREATE TABLE ORDERS 
  ( 
     ordid    DECIMAL(10, 0) PRIMARY KEY, 
     ordname  VARCHAR(255) NOT NULL, 
     ordprice DECIMAL(10, 4), 
     ordowner DECIMAL(10, 0) NOT NULL 
  ); 

CREATE TABLE SEQUENCES 
  ( 
     seq_name  VARCHAR(255) PRIMARY KEY, 
     seq_count DECIMAL(10, 0) NOT NULL 
  ); 

CREATE TABLE UNIT 
  ( 
     unitid     DECIMAL(10, 0) PRIMARY KEY, 
     unitname   VARCHAR(255) NOT NULL, 
     unitreport DECIMAL(10, 10) NOT NULL, 
     unitparent DECIMAL(10, 0) 
  ); 

CREATE TABLE PROVIDER 
  ( 
     proid      DECIMAL(10, 0) PRIMARY KEY, 
     proname    VARCHAR(255) NOT NULL, 
     proaddress VARCHAR(130) NOT NULL 
  ); 

ALTER TABLE RECIPELIST 
  ADD CONSTRAINT recipelistfkrecipe FOREIGN KEY (lstrec) REFERENCES RECIPE(recid); 

ALTER TABLE RECIPELIST 
  ADD CONSTRAINT recipelistfkorder FOREIGN KEY (lstord) REFERENCES ORDERS(ordid); 

ALTER TABLE INGREDIENT 
  ADD CONSTRAINT ingredientfkrecipe FOREIGN KEY (ingrec) REFERENCES RECIPE(recid); 

ALTER TABLE INGREDIENT 
  ADD CONSTRAINT ingredientfkaliment FOREIGN KEY (ingali) REFERENCES ALIMENT(aliid); 

ALTER TABLE INGREDIENT 
  ADD CONSTRAINT ingredientfkunit FOREIGN KEY (ingunit) REFERENCES UNIT(unitid); 

ALTER TABLE RECIPE 
  ADD CONSTRAINT recipefkuser FOREIGN KEY (recowner) REFERENCES FOODUSER(usrid); 

ALTER TABLE CATEGORY 
  ADD CONSTRAINT subcategory FOREIGN KEY (catparent) REFERENCES CATEGORY(catid); 

ALTER TABLE ALIMENT 
  ADD CONSTRAINT alimentfkunit FOREIGN KEY (aliunit) REFERENCES UNIT(unitid); 

ALTER TABLE ALIMENT 
  ADD CONSTRAINT alimentfkcategory FOREIGN KEY (alicategory) REFERENCES CATEGORY(catid);  

ALTER TABLE ALIMENT 
  ADD CONSTRAINT alimentfkprovider FOREIGN KEY (aliprovider) REFERENCES PROVIDER(proid);

ALTER TABLE UNIT 
  ADD CONSTRAINT subunit FOREIGN KEY (unitparent) REFERENCES UNIT(unitid); 

ALTER TABLE ORDERS 
  ADD CONSTRAINT orderfkuser FOREIGN KEY (ordowner) REFERENCES FOODUSER(usrid); 