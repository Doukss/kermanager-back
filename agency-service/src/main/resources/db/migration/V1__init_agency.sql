CREATE EXTENSION IF NOT EXISTS pgcrypto;

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

CREATE TABLE subscriptions (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       VARCHAR(100) NOT NULL UNIQUE,
    plan            VARCHAR(50) NOT NULL DEFAULT 'STARTER',
    status          VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    monthly_price   NUMERIC(12,2) NOT NULL DEFAULT 0,
    started_at      DATE NOT NULL DEFAULT CURRENT_DATE,
    next_billing_at DATE NOT NULL DEFAULT CURRENT_DATE + INTERVAL '1 month'
);
CREATE INDEX idx_subscriptions_status ON subscriptions(status);

CREATE TABLE platform_notifications (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title      VARCHAR(255) NOT NULL,
    message    TEXT,
    type       VARCHAR(50),
    priority   VARCHAR(50),
    read       BOOLEAN NOT NULL DEFAULT false,
    target     VARCHAR(255),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
