-- =====================================
-- Beat Layer v1: Minimal Core Schema
-- - Unified Reddit-style thread items (AUDIO or COMMENT)
-- - Audio assets stored separately
-- - Audio item details stored separately (non-destructive edits + mix)
-- - Minimal constraints; business rules enforced in backend
-- =====================================

-- UUID support
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- =====================================
-- USERS
-- =====================================
CREATE TABLE IF NOT EXISTS users (
  id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  handle      VARCHAR(50) UNIQUE NOT NULL,
  email       VARCHAR(255) UNIQUE,
  created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- =====================================
-- JAMS
-- root_item_id is added after thread_items exists.
-- loop_length_ms is canonical for MVP (simple for looping).
-- =====================================
CREATE TABLE IF NOT EXISTS jams (
  id                 UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  created_by_user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,

  title              VARCHAR(255) NOT NULL,
  description        TEXT,

  loop_length_ms     INT NOT NULL CHECK (loop_length_ms > 0),

  -- Optional musical metadata (helpful for browsing/UI)
  bpm                INT CHECK (bpm BETWEEN 40 AND 300),
  musical_key        TEXT,
  genre              TEXT,
  instrument_hint    TEXT,

  visibility         TEXT NOT NULL DEFAULT 'public'
                     CHECK (visibility IN ('public','unlisted','private')),

  created_at         TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at         TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_jams_visibility_created_at
  ON jams(visibility, created_at DESC);

CREATE INDEX IF NOT EXISTS idx_jams_created_by_created_at
  ON jams(created_by_user_id, created_at DESC);

-- =====================================
-- THREAD ITEMS (Unified tree: AUDIO + COMMENT)
-- This is the "Reddit-like" structure.
-- Backend enforces:
-- - root must be AUDIO
-- - parent must be in same jam
-- - playback path logic (audio-only ancestors)
-- DB enforces:
-- - basic type validity
-- - comment body required when COMMENT
-- - 1 root per jam (parent_item_id IS NULL)
-- =====================================
CREATE TABLE IF NOT EXISTS thread_items (
  id                 UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  jam_id             UUID NOT NULL REFERENCES jams(id) ON DELETE CASCADE,
  parent_item_id     UUID REFERENCES thread_items(id) ON DELETE SET NULL,

  created_by_user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,

  item_type          TEXT NOT NULL CHECK (item_type IN ('AUDIO','COMMENT')),
  body               TEXT, -- used for COMMENT; can be NULL for AUDIO

  created_at         TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at         TIMESTAMPTZ NOT NULL DEFAULT now(),

  CONSTRAINT chk_comment_body_required
    CHECK (
      (item_type = 'COMMENT' AND body IS NOT NULL AND length(trim(body)) > 0)
      OR
      (item_type = 'AUDIO')
    )
);

CREATE INDEX IF NOT EXISTS idx_thread_items_jam_created_at
  ON thread_items(jam_id, created_at ASC);

CREATE INDEX IF NOT EXISTS idx_thread_items_parent
  ON thread_items(parent_item_id);

-- Enforce: only ONE root item per jam (the item with parent_item_id IS NULL)
CREATE UNIQUE INDEX IF NOT EXISTS ux_thread_items_one_root_per_jam
  ON thread_items(jam_id)
  WHERE parent_item_id IS NULL;

-- Now that thread_items exists, add jams.root_item_id (nullable during creation flow)
ALTER TABLE jams
  ADD COLUMN IF NOT EXISTS root_item_id UUID;

ALTER TABLE jams
  ADD CONSTRAINT fk_jams_root_item
  FOREIGN KEY (root_item_id) REFERENCES thread_items(id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS idx_jams_root_item
  ON jams(root_item_id);

-- =====================================
-- AUDIO ASSETS (Recorded file metadata)
-- storage_locator can be local path in dev or cloud key/url in prod.
-- =====================================
CREATE TABLE IF NOT EXISTS audio_assets (
  id                 UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  created_by_user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,

  storage_locator    TEXT NOT NULL,      -- e.g. /files/abc.wav OR s3://bucket/key OR blob URL
  mime_type          TEXT NOT NULL,      -- audio/wav, audio/mpeg, etc.
  duration_ms        INT NOT NULL CHECK (duration_ms > 0),

  sample_rate        INT,
  channels           INT CHECK (channels IS NULL OR channels IN (1,2)),
  bytes              BIGINT CHECK (bytes IS NULL OR bytes >= 0),

  created_at         TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_audio_assets_created_by_created_at
  ON audio_assets(created_by_user_id, created_at DESC);

-- =====================================
-- AUDIO ITEM DETAILS (1:1 with an AUDIO thread_item)
-- DB does NOT enforce that thread_item is AUDIO; backend enforces it.
-- =====================================
CREATE TABLE IF NOT EXISTS audio_item_details (
  thread_item_id     UUID PRIMARY KEY REFERENCES thread_items(id) ON DELETE CASCADE,
  audio_asset_id     UUID NOT NULL REFERENCES audio_assets(id) ON DELETE RESTRICT,

  -- Loop-aligned by default; allow shifting within loop if desired
  start_offset_ms    INT NOT NULL DEFAULT 0 CHECK (start_offset_ms >= 0),

  -- Non-destructive trim inside the asset
  trim_start_ms      INT NOT NULL DEFAULT 0 CHECK (trim_start_ms >= 0),
  trim_end_ms        INT CHECK (trim_end_ms IS NULL OR trim_end_ms > trim_start_ms),

  -- Mix controls
  gain_db            NUMERIC(5,2) NOT NULL DEFAULT 0 CHECK (gain_db BETWEEN -60 AND 12),
  pan                NUMERIC(4,3) NOT NULL DEFAULT 0 CHECK (pan BETWEEN -1 AND 1),
  muted              BOOLEAN NOT NULL DEFAULT FALSE,

  instrument         TEXT,
  notes              TEXT,

  created_at         TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at         TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_audio_item_details_audio_asset
  ON audio_item_details(audio_asset_id);

-- =====================================
-- REACTIONS (optional, simple "like")
-- Works great for UI polish; backend can expand types later.
-- =====================================
CREATE TABLE IF NOT EXISTS reactions (
  id                   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  user_id              UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  target_thread_item_id UUID NOT NULL REFERENCES thread_items(id) ON DELETE CASCADE,
  reaction_type        TEXT NOT NULL DEFAULT 'LIKE' CHECK (reaction_type IN ('LIKE')),
  created_at           TIMESTAMPTZ NOT NULL DEFAULT now(),

  UNIQUE (user_id, target_thread_item_id, reaction_type)
);

CREATE INDEX IF NOT EXISTS idx_reactions_target_created_at
  ON reactions(target_thread_item_id, created_at DESC);

CREATE INDEX IF NOT EXISTS idx_reactions_user_created_at
  ON reactions(user_id, created_at DESC);
