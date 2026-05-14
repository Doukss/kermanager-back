CREATE TABLE agencies (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id  VARCHAR(100) NOT NULL UNIQUE,
    nom        VARCHAR(255) NOT NULL,
    adresse    TEXT,
    telephone  VARCHAR(20),
    email      VARCHAR(255),
    plan       VARCHAR(50) DEFAULT 'STARTER',
    active     BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_agencies_tenant ON agencies(tenant_id);
