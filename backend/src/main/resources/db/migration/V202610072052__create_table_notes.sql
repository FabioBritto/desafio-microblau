CREATE TABLE notes (id VARCHAR(255) PRIMARY KEY, site VARCHAR(255) NOT NULL,
                    equipment VARCHAR(255) NOT NULL, variable VARCHAR(255) NOT NULL,
                    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    author VARCHAR(255) NOT NULL, message TEXT NOT NULL);