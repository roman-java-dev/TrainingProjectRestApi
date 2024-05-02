CREATE TABLE IF NOT EXISTS orders
(
    id             SERIAL        PRIMARY KEY,
    customer_id    BIGINT        NOT NULL,
    order_date     DATE          NOT NULL,
    status_payment BOOLEAN       NOT NULL,
    description    VARCHAR(255),
    total_price    DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers (id)
);
