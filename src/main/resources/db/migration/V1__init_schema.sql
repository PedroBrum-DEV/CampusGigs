CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(120) NOT NULL,
                       username VARCHAR(60) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(20) NOT NULL DEFAULT 'STUDENT',
                       created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE gigs (
                      id BIGSERIAL PRIMARY KEY,
                      title VARCHAR(150) NOT NULL,
                      description TEXT NOT NULL,
                      category VARCHAR(60) NOT NULL,
                      price NUMERIC(10,2) NOT NULL,
                      status VARCHAR(20) NOT NULL DEFAULT 'OPEN',
                      owner_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                      created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE applications (
                              id BIGSERIAL PRIMARY KEY,
                              gig_id BIGINT NOT NULL REFERENCES gigs(id) ON DELETE CASCADE,
                              applicant_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                              message TEXT,
                              status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
                              created_at TIMESTAMP NOT NULL DEFAULT now(),
                              CONSTRAINT uq_application_gig_applicant UNIQUE (gig_id, applicant_id)
);

CREATE INDEX idx_gigs_owner ON gigs(owner_id);
CREATE INDEX idx_applications_gig ON applications(gig_id);
CREATE INDEX idx_applications_applicant ON applications(applicant_id);