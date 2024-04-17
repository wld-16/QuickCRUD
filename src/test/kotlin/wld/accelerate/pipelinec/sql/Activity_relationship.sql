, , , , , create table Activity_contacts
(
    id       serial,
    Activity integer
        constraint Activity_contacts_Activity_id_fk
            references Activity,
    contacts  integer
        constraint Activity_contacts_contacts_id_fk
            references contacts
);