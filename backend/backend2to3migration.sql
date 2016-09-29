insert into networkproperty (value,network_id,property_id)
select false, id, 'RUN_VALIDATION'  from network;

insert into networkproperty (value,network_id,property_id)
select false, id, 'RUN_TRANSFORMATION'  from network;

insert into networkproperty (value,network_id,property_id)
select false, id, 'RUN_VUFIND_INDEXING'  from network;

insert into networkproperty (value,network_id,property_id)
select false, id, 'RUN_XOAI_INDEXING'  from network;

insert into networkproperty (value,network_id,property_id)
select false, id, 'RUN_DIAGNOSTIC'  from network;

