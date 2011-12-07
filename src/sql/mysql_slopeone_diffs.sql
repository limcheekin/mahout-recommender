DROP TABLE IF EXISTS taste_slopeone_diffs;

CREATE TABLE taste_slopeone_diffs (
   item_id_a BIGINT NOT NULL,
   item_id_b BIGINT NOT NULL,
   average_diff FLOAT NOT NULL,
   standard_deviation FLOAT NOT NULL,
   count INT NOT NULL,
   PRIMARY KEY (item_id_a, item_id_b),
   INDEX (item_id_a),
   INDEX (item_id_b)
 );
