CREATE TABLE candidate (
   id SERIAL PRIMARY KEY,
   photo bytea,
   name TEXT,
   visible BOOLEAN,
   description TEXT,
   city_id SMALLINT,
   created TIMESTAMP
);