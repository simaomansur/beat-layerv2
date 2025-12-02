-- =====================================
-- Extensions
-- =====================================
create extension if not exists "uuid-ossp";

-- =====================================
-- USERS
-- =====================================
create table if not exists users (
  id uuid primary key default uuid_generate_v4(),
  handle varchar(50) unique not null,
  email varchar(255) unique not null,
  password_hash text not null,

  -- 'user'  = normal user
  -- 'creator' = can create premium or contest jams (with subscription later)
  -- 'admin' = full powers
  role text not null default 'user' check (role in ('user', 'creator', 'admin')),

  -- Stripe mapping
  stripe_customer_id text unique,

  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

-- Optional seed admin. Replace email and password_hash in real life.
insert into users (id, handle, email, password_hash, role)
values (
  '00000000-0000-0000-0000-000000000001',
  'admin',
  'admin@example.com',
  '$2a$10$replace_this_with_real_bcrypt_hash',
  'admin'
)
on conflict (email) do nothing;

-- =====================================
-- USER CREDIT BALANCES
-- =====================================
create table if not exists user_credit_balances (
  user_id uuid primary key references users(id) on delete cascade,
  balance int not null default 0 check (balance >= 0)
);

-- =====================================
-- JAMS
-- =====================================
create table if not exists jams (
  id uuid primary key default uuid_generate_v4(),

  title varchar(255) not null,
  created_by uuid not null references users(id) on delete cascade,

  musical_key text not null,      -- for example "C#m", "D Dorian"
  bpm int not null check (bpm between 40 and 300),

  genre text,
  instrument_hint text,
  base_audio_url text,            -- main or base mix URL

  -- Premium and credits
  is_premium boolean not null default false,
  layer_credit_cost int not null default 1 check (layer_credit_cost > 0),

  -- Contest
  is_contest boolean not null default false,
  contest_ends_at timestamptz,
  contest_description text,
  is_locked boolean not null default false,  -- true when contest done or jam read only

  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create index if not exists idx_jams_created_by_created_at
  on jams(created_by, created_at desc);

create index if not exists idx_jams_created_at
  on jams(created_at desc);

create index if not exists idx_jams_contest_flags
  on jams(is_contest, is_locked, contest_ends_at);

-- =====================================
-- TAGS
-- =====================================
create table if not exists tags (
  id uuid primary key default uuid_generate_v4(),
  name varchar(64) not null unique
);

create table if not exists jam_tags (
  jam_id uuid not null references jams(id) on delete cascade,
  tag_id uuid not null references tags(id) on delete cascade,
  primary key (jam_id, tag_id)
);

create index if not exists idx_jam_tags_tag
  on jam_tags(tag_id);

-- =====================================
-- LAYERS
-- =====================================
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

  gain_db numeric(5,2) not null default 0 check (gain_db between -60 and 12),
  pan numeric(4,3) not null default 0 check (pan between -1 and 1),

  key_override text,
  bpm_override int check (bpm_override between 40 and 300),

  instrument text,
  notes text,

  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create index if not exists idx_layers_jam_parent
  on layers(jam_id, parent_layer_id);

create index if not exists idx_layers_jam_created_at
  on layers(jam_id, created_at asc);

create index if not exists idx_layers_created_by_created_at
  on layers(created_by, created_at desc);

-- =====================================
-- COMMENTS ON JAMS
-- =====================================
create table if not exists jam_comments (
  id uuid primary key default uuid_generate_v4(),

  jam_id uuid not null references jams(id) on delete cascade,
  user_id uuid not null references users(id) on delete cascade,
  parent_comment_id uuid references jam_comments(id) on delete set null,

  body text not null,

  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create index if not exists idx_jam_comments_jam_created_at
  on jam_comments(jam_id, created_at asc);

create index if not exists idx_jam_comments_user_created_at
  on jam_comments(user_id, created_at desc);

-- =====================================
-- LIKES AND FAVORITES FOR JAMS
-- =====================================
create table if not exists jam_likes (
  jam_id uuid not null references jams(id) on delete cascade,
  user_id uuid not null references users(id) on delete cascade,
  created_at timestamptz not null default now(),
  primary key (jam_id, user_id)
);

create index if not exists idx_jam_likes_user_created_at
  on jam_likes(user_id, created_at desc);

create table if not exists jam_favorites (
  jam_id uuid not null references jams(id) on delete cascade,
  user_id uuid not null references users(id) on delete cascade,
  created_at timestamptz not null default now(),
  primary key (jam_id, user_id)
);

create index if not exists idx_jam_favorites_user_created_at
  on jam_favorites(user_id, created_at desc);

-- =====================================
-- LAYER VOTES FOR CONTESTS
-- =====================================
create table if not exists layer_votes (
  layer_id uuid not null references layers(id) on delete cascade,
  user_id uuid not null references users(id) on delete cascade,
  created_at timestamptz not null default now(),
  primary key (layer_id, user_id)
);

create index if not exists idx_layer_votes_layer
  on layer_votes(layer_id);

create index if not exists idx_layer_votes_user
  on layer_votes(user_id);

-- =====================================
-- CONTEST WINNERS AND TROPHIES
-- =====================================
create table if not exists jam_contest_winners (
  jam_id uuid not null references jams(id) on delete cascade,
  user_id uuid not null references users(id) on delete cascade,

  -- 1 = gold, 2 = silver, 3 = bronze
  place int not null check (place between 1 and 3),

  awarded_at timestamptz not null default now(),

  primary key (jam_id, user_id)
);

create index if not exists idx_jam_contest_winners_user
  on jam_contest_winners(user_id);

create index if not exists idx_jam_contest_winners_jam
  on jam_contest_winners(jam_id);

-- =====================================
-- CREDIT TRANSACTIONS
-- =====================================
create table if not exists credit_transactions (
  id uuid primary key default uuid_generate_v4(),

  user_id uuid not null references users(id) on delete cascade,

  -- positive = earn, negative = spend
  amount int not null check (amount <> 0),
  transaction_type text not null check (transaction_type in ('earn', 'spend', 'adjust')),

  jam_id uuid references jams(id) on delete set null,
  layer_id uuid references layers(id) on delete set null,

  description text,
  created_at timestamptz not null default now()
);

create index if not exists idx_credit_transactions_user_created_at
  on credit_transactions(user_id, created_at desc);

-- =====================================
-- STRIPE: CREDIT PACKAGES AND CREATOR PLANS
-- =====================================
create table if not exists credit_packages (
  id uuid primary key default uuid_generate_v4(),
  code text not null unique,          -- for example 'credits_small'
  name text not null,                 -- for example '50 Credits Pack'
  credits_amount int not null check (credits_amount > 0),
  stripe_price_id text not null,      -- price_XXXX
  active boolean not null default true
);

create table if not exists creator_plans (
  id uuid primary key default uuid_generate_v4(),
  code text not null unique,          -- for example 'creator_monthly'
  name text not null,                 -- for example 'Beat Layer Creator Monthly'
  stripe_price_id text not null,      -- recurring price_XXXX
  active boolean not null default true
);

create table if not exists creator_subscriptions (
  id uuid primary key default uuid_generate_v4(),

  user_id uuid not null references users(id) on delete cascade,
  plan_id uuid not null references creator_plans(id) on delete restrict,

  stripe_subscription_id text not null,      -- sub_XXXX
  status text not null,                      -- for example 'active', 'canceled'
  current_period_end timestamptz,

  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create index if not exists idx_creator_subscriptions_user
  on creator_subscriptions(user_id, created_at desc);

-- Optional Stripe payment log
create table if not exists stripe_payments (
  id uuid primary key default uuid_generate_v4(),

  user_id uuid not null references users(id) on delete cascade,

  stripe_payment_intent_id text,
  stripe_invoice_id text,
  stripe_checkout_session_id text,

  amount_total int,
  currency text,
  purpose text not null,    -- for example 'credits' or 'creator_subscription'

  raw_data jsonb,
  created_at timestamptz not null default now()
);

create index if not exists idx_stripe_payments_user
  on stripe_payments(user_id, created_at desc);

-- =====================================
-- UPDATED_AT TRIGGER
-- =====================================
create or replace function set_updated_at()
returns trigger as $$
begin
  new.updated_at = now();
  return new;
end;
$$ language plpgsql;

create trigger trg_users_set_updated_at
  before update on users
  for each row
  execute function set_updated_at();

create trigger trg_jams_set_updated_at
  before update on jams
  for each row
  execute function set_updated_at();

create trigger trg_layers_set_updated_at
  before update on layers
  for each row
  execute function set_updated_at();

create trigger trg_jam_comments_set_updated_at
  before update on jam_comments
  for each row
  execute function set_updated_at();

create trigger trg_creator_subscriptions_set_updated_at
  before update on creator_subscriptions
  for each row
  execute function set_updated_at();
