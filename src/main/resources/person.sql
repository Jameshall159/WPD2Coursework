CREATE TABLE IF NOT EXISTS person (
  id int AUTO_INCREMENT PRIMARY KEY,
  first VARCHAR(255),
  last VARCHAR(255),
  email VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS message (
  id int AUTO_INCREMENT PRIMARY KEY,
  subject VARCHAR(55),
  contents VARCHAR(255),
  p_id int,
  FOREIGN KEY (p_id) REFERENCES person(id)
);
