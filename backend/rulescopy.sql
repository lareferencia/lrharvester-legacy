insert into validatorrule ( jsonserialization, description, mandatory, name, quantifier, validator_id)
(select jsonserialization, description, mandatory, name, quantifier, 2
from validatorrule
where validator_id = 1)