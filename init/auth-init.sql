CREATE TABLE IF NOT EXISTS employees (
     emp_id UUID PRIMARY KEY,
     uname TEXT NOT NULL,
     fname TEXT NOT NULL,
     lname TEXT NOT NULL
);

INSERT INTO employees (emp_id, uname, fname, lname) VALUES
    ('33333333-3333-3333-3333-333333333333', 'peter', 'Petr', 'Sidorov'),
    ('44444444-4444-4444-4444-444444444444', 'mary', 'Maria', 'Kuznetsova');
