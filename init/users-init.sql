CREATE TABLE IF NOT EXISTS users (
         user_id UUID PRIMARY KEY,
         login TEXT NOT NULL,
         first_name TEXT NOT NULL,
         last_name TEXT NOT NULL
);

INSERT INTO users (user_id, login, first_name, last_name) VALUES
  ('11111111-1111-1111-1111-111111111111', 'ivan', 'Ivan', 'Ivanov'),
  ('22222222-2222-2222-2222-222222222222', 'olga', 'Olga', 'Petrova');
