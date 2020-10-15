DROP DATABASE IF EXISTS PoisePMS;
CREATE DATABASE PoisePMS;
USE PoisePMS;

CREATE TABLE Architect(
ArchitectID INT AUTO_INCREMENT,
ArchitectName VARCHAR(100),
ArchitectTel VARCHAR(50),
ArchitectEmail VARCHAR(100),
ArchitectAddress VARCHAR(100),
PRIMARY KEY (ArchitectID)
);

INSERT INTO Architect (ArchitectName, ArchitectTel, ArchitectEmail, ArchitectAddress) 
VALUES ('Jake Morris', '07776 891 979', 'jakem@morrisarch.com', '56 Galley Rd'),
('Mark Jeffrey', '07826 877 134', 'mark.architect@gmail.com', '12 Station Rd'),
('Lucy Long', '07143 878 111', 'lucy@longarch.com', '44 Cliff View Rd');

CREATE TABLE Contractor(
ContractorID INT AUTO_INCREMENT,
ContractorName VARCHAR(100),
ContractorTel VARCHAR(50),
ContractorEmail VARCHAR(100),
ContractorAddress VARCHAR(100),
PRIMARY KEY (ContractorID)
);

INSERT INTO Contractor (ContractorName, ContractorTel, ContractorEmail, ContractorAddress) 
VALUES ('Sam Doe', '07745 865 999', 'sdoe@hotmail.com', '6 Arch Rd'),
('Elliot Gooch', '07143 821 123', 'e.gooch@gmail.com', '2 Revel Rd'),
('Ray Daniels', '07133 818 121', 'rayd@danielscontracting.com', '4 Time Sq');

CREATE TABLE Customer(
CustomerID INT AUTO_INCREMENT,
CustomerName VARCHAR(100),
CustomerTel VARCHAR(50),
CustomerEmail VARCHAR(100),
CustomerAddress VARCHAR(100),
PRIMARY KEY (CustomerID)
);

INSERT INTO Customer (CustomerName, CustomerTel, CustomerEmail, CustomerAddress)
VALUES ('Caroline Brown', '07666 811 559', 'cazbrown@hotmail.com', '211 Sparry Court'),
('Chris Weighill', '07826 873 224', 'chrisw@gmail.com', '1 Longcross Rd'),
('Jenny Hill', '07143 999 222', 'jenny98@gmail.com', '44 Cliff View Rd');

CREATE TABLE ProjectManager(
ProjectManagerID INT AUTO_INCREMENT,
ProjectManagerName VARCHAR(100),
ProjectManagerTel VARCHAR(50),
ProjectManagerEmail VARCHAR(100),
ProjectManagerAddress VARCHAR(100),
PRIMARY KEY (ProjectManagerID)
);

INSERT INTO ProjectManager (ProjectManagerName, ProjectManagerTel, ProjectManagerEmail, ProjectManagerAddress)
VALUES ('Lauren Hopkins', '07555 861 444', 'lhopkins@poised.com', '2 Purley Ave'),
('Allen Sugar', '07826 778 333', 'asugar@poised.com', '7 Langhouse Rd'),
('James Willis', '07143 878 176', 'jwillis@poised.com', '23 Main Street');

CREATE TABLE StructuralEngineer(
StructuralEngID INT AUTO_INCREMENT,
StructuralEngName VARCHAR(100),
StructuralEngTel VARCHAR(50),
StructuralEngEmail VARCHAR(100),
StructuralEngAddress VARCHAR(100),
PRIMARY KEY (StructuralEngID)
);

INSERT INTO StructuralEngineer (StructuralEngName, StructuralEngTel, StructuralEngEmail, StructuralEngAddress)
VALUES ('Mich Griffith', '07776 891 979', 'mg@poised.com', '88 Stan Street'),
('Amy Douglas', '07826 877 134', 'ad@poised.com', '91 Lower Abbot Rd'),
('Harley Saunders', '07143 878 111', 'hs@poised.com', '1 Byford Ave');

CREATE TABLE Project(
ProjectID INT AUTO_INCREMENT,
ProjectName VARCHAR(100),
BuildingType VARCHAR(100),
Address VARCHAR(200),
ERFNumber INT,
TotalFee DOUBLE(12,2),
TotalPaid DOUBLE(12,2),
Deadline DATE,
ArchitectID INT,
ContractorID INT,
CustomerID INT,
ProjectManagerID INT,
StructuralEngID INT,
Completed BOOLEAN,
CompletedDate DATE,
PRIMARY KEY (ProjectID),
FOREIGN KEY (ArchitectID) REFERENCES Architect(ArchitectID),
FOREIGN KEY (ContractorID) REFERENCES Contractor(ContractorID),
FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID),
FOREIGN KEY (ProjectManagerID) REFERENCES ProjectManager(ProjectManagerID),
FOREIGN KEY (StructuralEngID) REFERENCES StructuralEngineer(StructuralEngID)
);

INSERT INTO Project (ProjectName, BuildingType, Address, ERFNumber, TotalFee, TotalPaid, Deadline, ArchitectID, ContractorID, CustomerID, 
ProjectManagerID, StructuralEngID, Completed, CompletedDate) 
VALUES 
(
'House Clifton',
'House',
'88 Losely Close',
1235,
10000.00,
8000.00,
'2020-06-22',
1,
1,
1,
1,
1,
0,
null
),

(
'Flat Drew',
'Flat',
'7 Tilly Lane',
1785,
55000.00,
13000.00,
'2020-08-02',
2,
2,
3,
2,
1,
0,
null
),

(
'House Marks',
'House',
'65 Farside Close',
1900,
200000.00,
88000.00,
'2025-10-02',
3,
2,
2,
3,
1,
0,
null
);