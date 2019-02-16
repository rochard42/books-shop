create table author
(
  id          bigserial     not null,
  name        varchar(256)  not null,
  description varchar(4096) not null
);

alter table author
  add constraint author_pk primary key (id);

create table book
(
  id          bigserial     not null,
  name        varchar(256)  not null,
  description varchar(4096) not null,
  author      bigint        not null
);

alter table book
  add constraint book_pk primary key (id);

alter table book
  add constraint book_author_fk foreign key (author) references author (id);
