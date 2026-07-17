
CREATE TABLE tags (
    "id" BIGSERIAL NOT NULL PRIMARY KEY,
    "owner_id" BIGINT NOT NULL REFERENCES users(id),
    "name" TEXT NOT NULL,
    CONSTRAINT owner_tag_unique UNIQUE (owner_id, name)
);

CREATE TABLE note_tags (
    "note_id" BIGINT NOT NULL REFERENCES notes(id) ON DELETE CASCADE,
    "tag_id" BIGINT NOT NULL REFERENCES tags(id) ON DELETE CASCADE,
    PRIMARY KEY(note_id, tag_id)
);

