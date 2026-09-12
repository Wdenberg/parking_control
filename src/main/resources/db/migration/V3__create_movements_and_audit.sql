CREATE TABLE movements (
    id UUID PRIMARY KEY,
    vehicle_id UUID NOT NULL,
    parking_spot_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL,
    entry_at TIMESTAMP WITH TIME ZONE NOT NULL,
    exit_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_movement_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles (id),
    CONSTRAINT fk_movement_parking_spot FOREIGN KEY (parking_spot_id) REFERENCES parking_spots (id),
    CONSTRAINT ck_movement_status CHECK (status IN ('OPEN', 'CLOSED', 'CANCELLED'))
);

CREATE INDEX idx_movements_vehicle_status ON movements (vehicle_id, status);
CREATE INDEX idx_movements_parking_spot_status ON movements (parking_spot_id, status);

CREATE TABLE audit_logs (
    id UUID PRIMARY KEY,
    event_type VARCHAR(100) NOT NULL,
    actor_user_id UUID,
    details TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_audit_log_actor FOREIGN KEY (actor_user_id) REFERENCES users (id)
);

CREATE INDEX idx_audit_logs_actor_created ON audit_logs (actor_user_id, created_at);
