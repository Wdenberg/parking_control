CREATE TABLE apartments (
    id UUID PRIMARY KEY,
    block VARCHAR(10) NOT NULL,
    number VARCHAR(10) NOT NULL,
    identifier VARCHAR(32) NOT NULL,
    active BOOLEAN NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT uq_apartment_block_number UNIQUE (block, number)
);

CREATE TABLE cameras (
    id UUID PRIMARY KEY,
    code VARCHAR(20) NOT NULL,
    host VARCHAR(100),
    operation VARCHAR(10) NOT NULL,
    description VARCHAR(200),
    enabled BOOLEAN NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT uq_camera_code UNIQUE (code),
    CONSTRAINT ck_camera_operation CHECK (operation IN ('ENTRY', 'EXIT'))
);

CREATE TABLE parking_spots (
    id UUID PRIMARY KEY,
    apartment_id UUID NOT NULL,
    identifier VARCHAR(10) NOT NULL,
    enabled BOOLEAN NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT uq_parking_spot_identifier UNIQUE (identifier),
    CONSTRAINT fk_parking_spot_apartment FOREIGN KEY (apartment_id) REFERENCES apartments (id)
);

CREATE INDEX idx_parking_spots_apartment ON parking_spots (apartment_id);

CREATE TABLE vehicles (
    id UUID PRIMARY KEY,
    apartment_id UUID NOT NULL,
    plate VARCHAR(8) NOT NULL,
    model VARCHAR(60),
    color VARCHAR(30),
    active BOOLEAN NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT uq_vehicle_normalized_plate UNIQUE (plate),
    CONSTRAINT fk_vehicle_apartment FOREIGN KEY (apartment_id) REFERENCES apartments (id)
);

CREATE INDEX idx_vehicles_apartment ON vehicles (apartment_id);
