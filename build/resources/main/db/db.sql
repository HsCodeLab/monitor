CREATE TABLE history (
     id INT AUTO_INCREMENT PRIMARY KEY,
     sn VARCHAR(50) NOT NULL,
     date DATE NOT NULL,
     voltage_input DECIMAL(4, 1) default 0.0,
     voltage_output DECIMAL(4, 1) default 0.0,
     frequency_input DECIMAL(4, 1) default 0.0,
     frequency_output DECIMAL(4, 1) default 0.0,
     battery_voltage DECIMAL(4, 1) default 0.0,
     battery_level INT default 0,
     temperature INT default 0,
     output_load_percent INT default 0
);