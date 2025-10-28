create extension if not exists "uuid-ossp";

create type musical_key as enum (
  'C','C#','D','D#','E','F','F#','G','G#','A','A#','B',
  'Cm','C#m','Dm','D#m','Em','Fm','F#m','Gm','G#m','Am','A#m','Bm'
);

create table if not exists users (
  id uuid primary key default uuid_generate_v4(),
  handle text unique not null,
  email text unique not null,
  password_hash text not null,
  created_at timestamptz not null default now()
);

create table if not exists jams (
  id uuid primary key default uuid_generate_v4(),
  title text not null,
  created_by uuid not null references users(id) on delete cascade,
  key musical_key not null,
  bpm int not null check (bpm between 40 and 240),
  genre text,
  instrument_hint text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create table if not exists layers (
  id uuid primary key default uuid_generate_v4(),
  jam_id uuid not null references jams(id) on delete cascade,
  parent_layer_id uuid references layers(id) on delete set null,
  created_by uuid not null references users(id) on delete cascade,
  s3_key_original text not null,
  s3_key_normalized text,
  duration_ms int not null check (duration_ms > 0),
  bars int not null check (bars > 0),
  start_offset_ms int not null default 0,
  gain_db numeric not null default 0,
  pan numeric not null default 0,
  key_override musical_key,
  bpm_override int,
  instrument text,
  notes text,
  created_at timestamptz not null default now()
);

create index if not exists idx_layers_jam_parent on layers(jam_id, parent_layer_id);
