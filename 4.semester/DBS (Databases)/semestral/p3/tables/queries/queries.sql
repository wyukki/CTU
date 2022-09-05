--INNER JOIN
--Shows a doctor's name, who has a telephone number
SELECT NAME, phone
FROM person
    JOIN doctor
    ON ( (doctor.pass = person.pass) AND (person.phone IS NOT NULL)
    )
LIMIT(20);

--OUTER JOIN
--Shows the address of branch, amount of trasported blood and the name of the hospital, to which blood was transported
SELECT city, street, zip, amount, hospital
FROM branch
    LEFT OUTER JOIN transfer
    ON (transfer.branchnumber = branch.branchnumber)
LIMIT(20);

--Data condtion
--Shows donor's info (name, phone if has one, med. card), who has donated blood of first group and positive Rh
SELECT donormedcard, bloodGroup, Rh, NAME, phone
FROM blood
    JOIN donor
    ON (blood.donormedcard = donor.medcard)
    JOIN person
    ON (person.pass = donor.pass)
WHERE (bloodGroup = 1) AND (Rh = TRUE) AND (phone IS NOT NULL)
LIMIT(20)
OFFSET 2;

--Agregation, condition on agregation function, sorting a paging
--Shows how many times each donor has donated a blood, sorted in descending order
SELECT NAME, COUNT(*) AS counter
FROM blood
    JOIN donor
    ON (blood.donormedcard = donor.medcard)
    JOIN person
    ON (donor.pass = person.pass)
GROUP BY (NAME)
HAVING (NAME NOT LIKE 'A%')
ORDER BY (counter) DESC
LIMIT(15)

