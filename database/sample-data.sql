USE medicore_db;

-- Demo doctor login
INSERT INTO users (full_name, email, phone, password, role, is_active, failed_attempts, locked_until)
SELECT 'Dr. Aarav Sharma', 'doctor@medicore.com', '9811111111', 'e267d2d9dade02e9558498f6e43987e2', 'doctor', 1, 0, NULL
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'doctor@medicore.com'
);

-- Demo patient login
INSERT INTO users (full_name, email, phone, password, role, is_active, failed_attempts, locked_until)
SELECT 'Priya Patient', 'patient@medicore.com', '9822222222', 'b4ace37937964957e86e3e085c0b7577', 'patient', 1, 0, NULL
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'patient@medicore.com'
);

-- Doctor profile linked with the demo doctor user
INSERT INTO doctors (user_id, dept_id, specialization, qualification, experience_yrs, consultation_fee, available_days)
SELECT u.user_id, 5, 'General Physician', 'MBBS', 6, 1200.00, 'Sunday-Friday'
FROM users u
WHERE u.email = 'doctor@medicore.com'
AND NOT EXISTS (
    SELECT 1 FROM doctors d WHERE d.user_id = u.user_id
);

-- Patient profile linked with the demo patient user
INSERT INTO patients (user_id, dob, gender, blood_group, address, emergency_contact)
SELECT u.user_id, '2001-05-14', 'Female', 'O+', 'Kathmandu, Nepal', '9800000000'
FROM users u
WHERE u.email = 'patient@medicore.com'
AND NOT EXISTS (
    SELECT 1 FROM patients p WHERE p.user_id = u.user_id
);

-- One upcoming appointment so the dashboards have visible data
INSERT INTO appointments (patient_id, doctor_id, appt_date, appt_time, status, reason, notes)
SELECT p.patient_id, d.doctor_id, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00:00', 'Confirmed', 'General health follow-up', 'Bring previous prescriptions'
FROM patients p
JOIN users pu ON p.user_id = pu.user_id
CROSS JOIN doctors d
JOIN users du ON d.user_id = du.user_id
WHERE pu.email = 'patient@medicore.com'
AND du.email = 'doctor@medicore.com'
AND NOT EXISTS (
    SELECT 1
    FROM appointments a
    WHERE a.patient_id = p.patient_id
      AND a.doctor_id = d.doctor_id
      AND a.appt_date = DATE_ADD(CURDATE(), INTERVAL 1 DAY)
      AND a.appt_time = '10:00:00'
);
