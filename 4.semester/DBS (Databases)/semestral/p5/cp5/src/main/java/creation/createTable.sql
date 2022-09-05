CREATE TABLE branch (
                        branchNumber INTEGER,
                        CID INTEGER,
                        CEO VARCHAR(100),
                        bankID INTEGER UNIQUE NOT NULL,
                        bankCapacity INTEGER NOT NULL,
                        bankUsedPlace INTEGER NOT NULL,
                        hospital VARCHAR(100) UNIQUE NOT NULL,
                        city VARCHAR(100) NOT NULL,
                        street VARCHAR(100) NOT NULL,
                        zip CHAR(20) NOT NULL,
                        PRIMARY KEY (branchNumber, CID, CEO),
                        CONSTRAINT bank_ch_space CHECK (bankUsedPlace <= bankUsedPlace)
);

CREATE TABLE person (
                        pass INTEGER PRIMARY KEY,
                        NAME VARCHAR(100) NOT NULL,
                        phone VARCHAR(100)
);

CREATE TABLE donor (
                       pass INTEGER,
                       medCard VARCHAR(12) NOT NULL UNIQUE,
                       PRIMARY KEY (pass),
                       CONSTRAINT pass_fk FOREIGN KEY (pass) REFERENCES person (pass) ON
                           UPDATE CASCADE ON
                           DELETE CASCADE
);

CREATE TABLE doctor (
                        pass INTEGER,
                        workerID INTEGER NOT NULL UNIQUE,
                        PRIMARY KEY (pass),
                        CONSTRAINT pass_fk FOREIGN KEY (pass) REFERENCES person (pass) ON
                            UPDATE CASCADE ON
                            DELETE CASCADE
);

CREATE TABLE invite (
                        donorPass INTEGER,
                        invitedPass INTEGER,
                        PRIMARY KEY (donorPass, invitedPass),
                        CONSTRAINT donor_fk FOREIGN KEY (donorPass) REFERENCES donor(pass) ON
                            UPDATE CASCADE ON
                            DELETE CASCADE,
                        CONSTRAINT invited_fk FOREIGN KEY (invitedPass) REFERENCES donor(pass) ON
                            UPDATE CASCADE ON
                            DELETE CASCADE
);

CREATE TABLE transfer (
                          artKey INTEGER,
                          branchNumber INTEGER NOT NULL,
                          CID INTEGER NOT NULL,
                          CEO VARCHAR(100) NOT NULL,
                          amount INTEGER NOT NULL,
                          PRIMARY KEY (artKey),
                          CONSTRAINT branch_fk FOREIGN KEY (branchNumber, CID, CEO) REFERENCES branch (branchNumber, CID, CEO) ON
                              UPDATE CASCADE ON
                              DELETE CASCADE,
                          CONSTRAINT amount_ch CHECK (amount >= 0)
);

CREATE TABLE workplace (
                           branchNumber INTEGER,
                           CID INTEGER NOT NULL,
                           CEO VARCHAR(100) NOT NULL,
                           doctorPass INTEGER,
                           PRIMARY KEY (branchNumber, doctorPass),
                           CONSTRAINT branch_fk FOREIGN KEY (branchNumber, CID, CEO) REFERENCES branch (branchNumber, CID, CEO) ON
                               UPDATE CASCADE ON
                               DELETE CASCADE,
                           CONSTRAINT doctor_fk FOREIGN KEY (doctorPass) REFERENCES doctor(pass) ON
                               UPDATE CASCADE ON
                               DELETE CASCADE
);

CREATE TABLE blood (
                       bloodID INTEGER,
                       doctorPass INTEGER NOT NULL,
                       donorPass INTEGER NOT NULL,
                       branchNumber INTEGER NOT NULL,
                       CID INTEGER NOT NULL,
                       CEO VARCHAR(100) NOT NULL,
                       DAY SMALLINT NOT NULL,
                       MONTH SMALLINT NOT NULL,
                       YEAR SMALLINT NOT NULL,
                       Rh BOOLEAN NOT NULL,
                       bloodGroup SMALLINT NOT NULL,
                       PRIMARY KEY (bloodID),
                       CONSTRAINT blood_uq UNIQUE (
                                                   doctorPass,
                                                   donorPass,
                                                   branchNumber,
                                                   DAY,
                                                   MONTH,
                                                   YEAR
                           ),
                       CONSTRAINT doctor_fk FOREIGN KEY (doctorPass) REFERENCES doctor (pass) ON
                           UPDATE CASCADE ON
                           DELETE CASCADE,
                       CONSTRAINT donor_fk FOREIGN KEY (donorPass) REFERENCES donor (pass) ON
                           UPDATE CASCADE ON
                           DELETE CASCADE,
                       CONSTRAINT branch_fk FOREIGN KEY (branchNumber, CID, CEO) REFERENCES branch (branchNumber, CID, CEO) ON
                           UPDATE CASCADE ON
                           DELETE CASCADE
);

CREATE TABLE donation (
                          artKey INTEGER,
                          donorPass INTEGER NOT NULL,
                          doctorPass integer NOT NULL,
                          PRIMARY KEY (artKey),
                          CONSTRAINT doctor_fk FOREIGN KEY (doctorPass) REFERENCES doctor (pass) ON
                              UPDATE CASCADE ON
                              DELETE CASCADE,
                          CONSTRAINT donor_fk FOREIGN KEY (donorPass) REFERENCES donor (pass) ON
                              UPDATE CASCADE ON
                              DELETE CASCADE
)