CREATE TABLE IF NOT EXISTS category
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS product
(
    id          BIGSERIAL PRIMARY KEY,
    category_id BIGINT           NOT NULL,
    name        VARCHAR(255)     NOT NULL,
    price       DOUBLE PRECISION NOT NULL,
    CONSTRAINT fk_product_category_id FOREIGN KEY (category_id) REFERENCES category (id)
);
