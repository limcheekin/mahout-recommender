 DROP TABLE IF EXISTS taste_preferences;
 
 CREATE TABLE taste_preferences (
   user_id BIGINT NOT NULL,
   item_id BIGINT NOT NULL,
   preference FLOAT NOT NULL,
   last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   PRIMARY KEY (user_id, item_id),
   INDEX (user_id),
   INDEX (item_id)
 );
 

