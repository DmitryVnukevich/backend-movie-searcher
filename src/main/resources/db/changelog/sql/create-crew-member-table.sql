CREATE TABLE crew_member (
                             id BIGSERIAL PRIMARY KEY,
                             firstname VARCHAR(50) NOT NULL,
                             lastname VARCHAR(50) NOT NULL,
                             birth_date DATE NOT NULL,
                             bio VARCHAR(255) NOT NULL,
                             photo TEXT NOT NULL
);