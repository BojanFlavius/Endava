ALTER TABLE insurancepolicy ALTER COLUMN end_date SET NOT NULL;

ALTER TABLE insurancepolicy ALTER COLUMN id RESTART WITH 100;

INSERT INTO owner (id, name, email) VALUES (1, 'Ana Pop', 'ana.pop@example.com');
INSERT INTO owner (id, name, email) VALUES (2, 'Bogdan Ionescu', 'bogdan.ionescu@example.com');
INSERT INTO owner (id, name, email) VALUES (3, 'Maria Georgescu', 'maria.georgescu@example.com');
INSERT INTO owner (id, name, email) VALUES (4, 'Andrei Dumitru', 'andrei.dumitru@example.com');


INSERT INTO car (id, vin, make, model, year_of_manufacture, owner_id) VALUES (1, 'VIN12345', 'Dacia', 'Logan', 2018, 1);
INSERT INTO car (id, vin, make, model, year_of_manufacture, owner_id) VALUES (2, 'VIN67890', 'VW', 'Golf', 2021, 2);
INSERT INTO car (id, vin, make, model, year_of_manufacture, owner_id) VALUES (3, 'VIN54321', 'Renault', 'Megane', 2019, 3);
INSERT INTO car (id, vin, make, model, year_of_manufacture, owner_id) VALUES (4, 'VIN98765', 'Toyota', 'Corolla', 2020, 4);

INSERT INTO insurancepolicy (id, car_id, provider, start_date, end_date) VALUES (1, 1, 'Allianz', DATE '2024-01-01', DATE '2025-01-01');
INSERT INTO insurancepolicy (id, car_id, provider, start_date, end_date) VALUES (2, 1, 'Groupama', DATE '2025-01-01', DATE '2026-01-01');
INSERT INTO insurancepolicy (id, car_id, provider, start_date, end_date) VALUES (3, 2, 'Allianz', DATE '2025-03-01', DATE '2026-03-01');
INSERT INTO insurancepolicy (id, car_id, provider, start_date, end_date) VALUES (4, 3, 'Generali', DATE '2024-06-01', DATE '2025-06-01');
INSERT INTO insurancepolicy (id, car_id, provider, start_date, end_date) VALUES (5, 3, 'Generali', DATE '2025-06-02', DATE '2026-06-01');
INSERT INTO insurancepolicy (id, car_id, provider, start_date, end_date) VALUES (6, 4, 'CityInsurance', DATE '2024-09-01', DATE '2025-09-01');
INSERT INTO insurancepolicy (id, car_id, provider, start_date, end_date) VALUES (7, 4, 'CityInsurance', DATE '2025-09-02', DATE '2026-09-01');

INSERT INTO insurancepolicy (id, car_id, provider, start_date, end_date) VALUES (99, 1, 'TestProvider', DATE '2025-01-01', CURRENT_DATE);

INSERT INTO insuranceclaim (id, car_id, claim_date, description, amount) VALUES (1, 1, DATE '2024-03-15', 'Minor accident on street parking', 1200.00);
INSERT INTO insuranceclaim (id, car_id, claim_date, description, amount) VALUES (2, 1, DATE '2024-07-10', 'Windshield replacement', 800.00);
INSERT INTO insuranceclaim (id, car_id, claim_date, description, amount) VALUES (3, 2, DATE '2025-05-20', 'Rear bumper repair after collision', 1500.00);
INSERT INTO insuranceclaim (id, car_id, claim_date, description, amount) VALUES (4, 3, DATE '2025-01-05', 'Theft of mirrors', 600.00);
INSERT INTO insuranceclaim (id, car_id, claim_date, description, amount) VALUES (5, 4, DATE '2024-11-18', 'Hail damage on hood and roof', 2000.00);