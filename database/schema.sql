CREATE DATABASE IF NOT EXISTS medicore_db;
USE medicore_db;

CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL UNIQUE,
    password CHAR(32) NOT NULL,
    role VARCHAR(20) NOT NULL,
    is_active TINYINT(1) NOT NULL DEFAULT 1,
    failed_attempts INT NOT NULL DEFAULT 0,
    locked_until TIMESTAMP NULL DEFAULT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS departments (
    dept_id INT PRIMARY KEY AUTO_INCREMENT,
    dept_name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS patients (
    patient_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL UNIQUE,
    dob DATE NULL,
    gender VARCHAR(20) NULL,
    blood_group VARCHAR(10) NULL,
    address TEXT NULL,
    emergency_contact VARCHAR(20) NULL,
    CONSTRAINT fk_patients_user
        FOREIGN KEY (user_id) REFERENCES users(user_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS doctors (
    doctor_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL UNIQUE,
    dept_id INT NOT NULL,
    specialization VARCHAR(120) NOT NULL,
    qualification VARCHAR(120) NOT NULL,
    experience_yrs INT NOT NULL DEFAULT 0,
    consultation_fee DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    available_days VARCHAR(120) NULL,
    CONSTRAINT fk_doctors_user
        FOREIGN KEY (user_id) REFERENCES users(user_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_doctors_department
        FOREIGN KEY (dept_id) REFERENCES departments(dept_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS appointments (
    appt_id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appt_date DATE NOT NULL,
    appt_time TIME NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'Pending',
    reason TEXT NULL,
    notes TEXT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_appointments_patient
        FOREIGN KEY (patient_id) REFERENCES patients(patient_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_appointments_doctor
        FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS medical_records (
    record_id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appt_id INT NULL,
    diagnosis TEXT NOT NULL,
    symptoms TEXT NULL,
    treatment TEXT NOT NULL,
    record_date DATE NOT NULL,
    CONSTRAINT fk_records_patient
        FOREIGN KEY (patient_id) REFERENCES patients(patient_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_records_doctor
        FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_records_appointment
        FOREIGN KEY (appt_id) REFERENCES appointments(appt_id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS prescriptions (
    prescription_id INT PRIMARY KEY AUTO_INCREMENT,
    record_id INT NOT NULL,
    medicine_name VARCHAR(120) NOT NULL,
    dosage VARCHAR(80) NULL,
    frequency VARCHAR(80) NULL,
    duration VARCHAR(80) NULL,
    instructions TEXT NULL,
    CONSTRAINT fk_prescriptions_record
        FOREIGN KEY (record_id) REFERENCES medical_records(record_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS bills (
    bill_id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id INT NOT NULL,
    appt_id INT NULL UNIQUE,
    consultation_fee DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    medicine_fee DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    test_fee DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    late_fee DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    total_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    paid_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    status VARCHAR(30) NOT NULL DEFAULT 'Unpaid',
    bill_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_bills_patient
        FOREIGN KEY (patient_id) REFERENCES patients(patient_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_bills_appointment
        FOREIGN KEY (appt_id) REFERENCES appointments(appt_id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

INSERT IGNORE INTO departments (dept_id, dept_name, description) VALUES
    (1, 'Cardiology', 'Heart and cardiovascular care'),
    (2, 'Neurology', 'Brain, nerve, and spinal care'),
    (3, 'Orthopedics', 'Bone, joint, and muscle care'),
    (4, 'Pediatrics', 'Child healthcare and development'),
    (5, 'General Medicine', 'Primary medical consultation and treatment');

INSERT IGNORE INTO users (
    user_id,
    full_name,
    email,
    phone,
    password,
    role,
    is_active,
    failed_attempts,
    locked_until
) VALUES (
    1,
    'System Admin',
    'admin@medicore.com',
    '9999999999',
    '0e7517141fb53f21ee439b355b5a1d0a',
    'admin',
    1,
    0,
    NULL
);
