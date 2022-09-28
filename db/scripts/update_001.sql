CREATE TABLE post (
   id SERIAL PRIMARY KEY,
   name TEXT,
   visible BOOLEAN,
   description TEXT,
   city_id SMALLINT,
   created TIMESTAMP
);