CREATE TABLE payment (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         product_id VARCHAR(255),
                         layout_id VARCHAR(255),
                         timestamp TIMESTAMP
);

CREATE TABLE view (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      product_id VARCHAR(255),
                      layout_id VARCHAR(255),
                      timestamp TIMESTAMP
);

CREATE TABLE report_request (
                                id UUID PRIMARY KEY,
                                product_id VARCHAR(255),
                                layout_id VARCHAR(255),
                                start_date TIMESTAMP,
                                end_date TIMESTAMP,
                                status VARCHAR(50),
                                version BIGINT
);

CREATE TABLE report_result (
                               request_id UUID PRIMARY KEY,
                               conversion_ratio DOUBLE,
                               payment_count INT
);