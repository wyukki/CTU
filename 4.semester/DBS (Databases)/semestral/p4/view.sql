create view countOfDonatedBlood as
SELECT NAME, COUNT(*) AS counter
FROM blood
    JOIN donor
    ON (blood.donormedcard = donor.medcard)
    JOIN person
    ON (donor.pass = person.pass)
GROUP BY (NAME)
HAVING (NAME NOT LIKE 'A%')
ORDER BY (counter) DESC