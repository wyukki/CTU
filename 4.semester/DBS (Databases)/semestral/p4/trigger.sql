create function check_Rh_group()
returns trigger
as $$
begin
	if (not (new.Rh between true and false) then
		raise exception 'invalid Rh factor';
	elseif (not (new.bloodGroup between 1 and 4)) then
		raise exception 'invalid blood group';
	end if;
	return new;
end;
	$$
language plpgsql;
create trigger blood_tg_Rh_group before insert or update on blood
for each row execute procedure check_Rh_group();
		
		
