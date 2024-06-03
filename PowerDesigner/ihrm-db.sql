/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2024/5/24 15:53:36                           */
/*==============================================================*/


drop table if exists co_company;

drop table if exists co_department;

/*==============================================================*/
/* Table: co_company                                            */
/*==============================================================*/
create table co_company
(
   id                   varchar(40) not null,
   name                 varchar(80),
   company_area         varchar(80),
   company_address      varchar(80),
   legal_representative varchar(80),
   company_phone        varchar(80),
   mailbox              varchar(80),
   industry             varchar(80),
   create_time          datetime,
   primary key (id)
);

/*==============================================================*/
/* Table: co_department                                         */
/*==============================================================*/
create table co_department
(
   id                   varchar(64) not null,
   co__id               varchar(40),
   company_id           varchar(64),
   parent_id            varchar(64),
   department_name      varchar(80),
   department_code      varchar(20),
   department_type      varchar(40),
   city                 varchar(20),
   introduction         varchar(255),
   create_time          datetime,
   primary key (id)
);

alter table co_department add constraint FK_FK_FK_COMPANY_DEPARTMENT foreign key (co__id)
      references co_company (id) on delete restrict on update restrict;

