-- seed a dev user so we can create jams
insert into users (id, handle, email, password_hash)
values ('00000000-0000-0000-0000-000000000001', 'dev', 'dev@example.com', '$2a$10$seededhash')
on conflict (email) do nothing;
