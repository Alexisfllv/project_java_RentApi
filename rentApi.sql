use RentApi;

select * from reservas;

select * from habitaciones;

INSERT INTO habitaciones (numero, piso, tipo, estado) VALUES
(101, 1, 'Suite', 'DISPONIBLE'),
(102, 1, 'Medium', 'DISPONIBLE'),
(201, 2, 'Standard', 'DISPONIBLE'),
(202, 2, 'Medium', 'DISPONIBLE'),
(301, 3, 'Suite', 'DISPONIBLE');




-- CONFIRMADA = 0
INSERT INTO reserva (fecha_inicio, fecha_fin, habitacion_id, estado)
VALUES ('2025-04-20', '2025-04-24', 2, 'RESERVADA');


-- query para ver si la habitaciones disponibles

SELECT * FROM reservas 
WHERE fecha_inicio BETWEEN '2025-04-01' AND '2025-04-30';


