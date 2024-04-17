, alter table Port add containerCranes integer;
alter table Port add constraint Port_containerCranes_id_fk 
	foreign key (containerCranes) references containerCranes;, alter table Port add containers integer;
alter table Port add constraint Port_containers_id_fk 
	foreign key (containers) references containers;, 