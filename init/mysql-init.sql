CREATE TABLE IF NOT EXISTS staff (
    id VARCHAR(36) PRIMARY KEY,
    login VARCHAR(100),
    first_name VARCHAR(100),
    last_name VARCHAR(100)
);

INSERT INTO staff (id, login, first_name, last_name) VALUES
 ('55555555-5555-5555-5555-555555555555', 'john', 'John', 'Doe'),
 ('66666666-6666-6666-6666-666666666666', 'jane', 'Jane', 'Smith');
