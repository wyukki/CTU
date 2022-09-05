create function new_transfer(brNumber INTEGER, artKey INTEGER, CID integer, ceo varchar(100))
returns boolean
as $$
	declare
		usedPlace INTEGER;
		capacity INTEGER;
	begin
		if (brNumber is none) then return false;
		else
			usedPlace := (select bankUsedPlace from branch where(branch.branchNumber = brNumber));
			capacity := (select bankCapacity from branch where(branch.branchNumber = brNumber));
			if (usedPlace < capacity) then return false; end if;
			update branch set usedPlace = 0 where (branch.branchNumber = brNumber);
			insert into transfers values (artKey, brNumber, cid, ceo, usedPlace);
			return true;
		end if;
	end;
	$$
language plpgsql;
begin transaction isolation level serializable;
select new_transfer(0, 105, 6618, 'semenvol');
commit transaction;
