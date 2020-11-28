CREATE TABLE IF NOT EXISTS users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(256) NOT NULL,
  password VARCHAR(256) NOT NULL,
  enabled TINYINT(1),
  UNIQUE KEY unique_username(username)
);

CREATE TABLE IF NOT EXISTS authorities (
  username VARCHAR(256) NOT NULL,
  authority VARCHAR(256) NOT NULL,
  PRIMARY KEY(username, authority)
);