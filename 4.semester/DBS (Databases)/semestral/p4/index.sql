drop index if exists bloodGroupIdx;
drop index if exists RhIdx;
drop index if exists phoneIdx;
create index bloodGroupIdx on blood(bloodGroup);
create index RhIdx on blood(Rh);
create index phoneIdx on person(phone);
explain
SELECT donormedcard, bloodGroup, Rh, NAME, phone
FROM blood
    JOIN donor
    ON (blood.donormedcard = donor.medcard)
    JOIN person
    ON (person.pass = donor.pass)
WHERE (bloodGroup = 1) AND (Rh = TRUE) AND (phone IS NOT NULL)
OFFSET 2;