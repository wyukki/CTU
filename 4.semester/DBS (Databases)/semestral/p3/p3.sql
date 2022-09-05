CREATE TABLE savingBlood (
    CID INTEGER PRIMARY KEY, 
    CEO VARCHAR(100) NOT NULL,
    branchNumber INTEGER,
    CONSTRAINT branch_fk
        FOREIGN KEY (branchNumber) REFERENCE branch (branchNumber)
);

CREATE TABLE branch (
    branchNumber INTEGER,
    bankID INTEGER UNIQUE NOT NULL,
    bankCapacity INTEGER NOT NULL,
    bankUsedPlace INTEGER NOT NULL,
    hospital VARCHAR(100) UNIQUE NOT NULL,
    city VARCHAR(100) NOT NULL,
    street VARCHAR(100) NOT NULL,
    zip CHAR(5) NOT NULL,
    PRIMARY KEY (branchNumber),
    CONSTRAINT bank_ch_space CHECK
        (bankUsedPlace <= bankUsedPlace)
);

CREATE TABLE person (
    pass VARCHAR(10) PRIMARY KEY,
    phone VARCHAR(12),
    name VARCHAR(100) NOT NULL
);

CREATE TABLE donor (
    pass VARCHAR(10) NOT NULL UNIQUE,
    medCard VARCHAR(20),
    PRIMARY KEY (medCard),
    CONSTRAINT pass_fk
        FOREIGN KEY (pass) REFERENCE person (pass)
);

CREATE TABLE doctor (
    pass VARCHAR(10) NOT NULL UNIQUE,
    workerID INTEGER,
    PRIMARY KEY (workerID),
    CONSTRAINT pass_fk
        FOREIGN KEY (pass) REFERENCE person (pass)
);

CREATE TABLE invite (
    donorMedCard VARCHAR(10),
    inviteMedCard VARCHAR(10),
    PRIMARY KEY (donorMedCard, inviteMedCard)
    CONSTRAINT donor_fk
        FOREIGN KEY (donorMedCard) REFERENCE donor(medCard)
    CONSTRAINT invited_fk
        FOREIGN KEY (inviteMedCard) REFERENCE donor(medCard)
);

CREATE TABLE transfer (
    branchNumber INTEGER,
    amount INTEGER NOT NULL,
    PRIMARY KEY (branch),
    CONSTRAINT branch_fk
        FOREIGN KEY (branchNumber) REFERENCE branch (branchNumber)
    CONSTRAINT amount_ch CHECK
        (amount_ch >= 0)
);

CREATE TABLE workplace (
    branchNumber INTEGER,
    doctorID INTEGER,
    PRIMARY KEY (branchNumber)
    CONSTRAINT branch_fk
        FOREIGN KEY (branchNumber) REFERENCE branch (branchNumber)
    CONSTRAINT doctor_fk
        FOREIGN KEY (doctorID) REFERENCE doctor(workerID)
);

CREATE TABLE blood (
    bloodID INTEGER,
    doctorID INTEGER NOT NULL,
    donorMedCard VARCHAR(12) NOT NULL,
    branchNumber INTEGER NOT NULL,
    day SMALLINT NOT NULL,
    month SMALLINT NOT NULL,
    year SMALLINT NOT NULL,
    Rh BOOLEAN NOT NULL,
    group SMALLINT NOT NULL,
    PRIMARY KEY (bloodID),
    CONSTRAINT blood_uq
        UNIQUE (doctorID, donorMedCard, branchNumber, day, month, year),
    CONSTRAINT doctor_fk
        FOREIGN KEY (doctorID) REFERENCE doctor (workerID),
    CONSTRAINT donor_fk
        FOREIGN KEY (donorMedCard) REFERENCE donor (medCard),
    CONSTRAINT branch_fk
        FOREIGN KEY (branchNumber) REFERENCE branch (branchNumber)
);

CREATE TABLE bloodDonation (
    donorMedCard VARCHAR(12),
    doctorID INTEGER,
    bloodID INTEGER
    PRIMARY KEY (donorMedCard, doctorID, bloodID),
    CONSTRAINT doctor_fl
        FOREIGN KEY (doctorID) REFERENCE doctor (workerID),
    CONSTRAINT donor_fk
        FOREIGN KEY (donorMedCard) REFERENCE donor (medCard)
    CONSTRAINT blood_fk
        FOREIGN KEY (bloodID) REFERENCE blood (bloodID)
);

