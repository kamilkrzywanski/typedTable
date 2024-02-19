CREATE TABLE IF NOT EXISTS TestModel (
    id serial PRIMARY KEY NOT NULL,
    columnA VARCHAR(255),
    columnB DECIMAL,
    columnC VARCHAR(255),
    myDate TIMESTAMP,
    testEnum SMALLINT,
    testFormatClass VARCHAR(255),
    booleanValue BOOLEAN
);