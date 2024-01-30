CREATE TABLE IF NOT EXISTS TestModel (
    columnA VARCHAR(255),
    columnB DECIMAL,
    columnC VARCHAR(255),
    date TIMESTAMP,
    testEnum INTEGER,
    testFormatClass VARCHAR(255),
    booleanValue BOOLEAN
);

INSERT INTO TestModel (columnA, columnB, columnC, date, testEnum, testFormatClass, booleanValue) VALUES
('value1', 1.1, 'value1', CURRENT_TIMESTAMP, 1, 'format1', true),
('value2', 2.2, 'value2', CURRENT_TIMESTAMP, 2, 'format2', true),
('value3', 3.3, 'value3', CURRENT_TIMESTAMP, 3, 'format3', true),
('value4', 4.4, 'value4', CURRENT_TIMESTAMP, 4, 'format4', true),
('value5', 5.5, 'value5', CURRENT_TIMESTAMP, 5, 'format5', true),
('value6', 6.6, 'value6', CURRENT_TIMESTAMP, 6, 'format6', false),
('value7', 7.7, 'value7', CURRENT_TIMESTAMP, 7, 'format7', false),
('value8', 8.8, 'value8', CURRENT_TIMESTAMP, 8, 'format8', false),
('value9', 9.9, 'value9', CURRENT_TIMESTAMP, 9, 'format9', false),
('value10', 10.10, 'value10', CURRENT_TIMESTAMP, 10, 'format10', true);