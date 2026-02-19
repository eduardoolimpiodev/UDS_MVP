CREATE TABLE document_versions (
    id BIGSERIAL PRIMARY KEY,
    document_id BIGINT NOT NULL,
    version_number INT NOT NULL,
    file_key VARCHAR(500) NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_size BIGINT,
    content_type VARCHAR(100),
    uploaded_at TIMESTAMP NOT NULL DEFAULT NOW(),
    uploaded_by BIGINT,
    is_current BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_versions_document FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE,
    CONSTRAINT fk_versions_uploaded_by FOREIGN KEY (uploaded_by) REFERENCES users(id),
    CONSTRAINT uk_document_version UNIQUE (document_id, version_number)
);

CREATE INDEX idx_versions_document ON document_versions(document_id);
CREATE INDEX idx_versions_current ON document_versions(document_id, is_current);
