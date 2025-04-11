use RentApi;

select * from Reserva;

select * from Habitacion;

describe habitacion;

insert into habitacion
(numero,piso,tipo)
values
(101,1,"Basico"),
(102,1,"Basico"),
(103,1,"Basico"),
(201,2,"Medium"),
(202,2,"Medium"),
(203,2,"Medium"),
(301,3,"Vip"),
(302,3,"Vip"),
(303,3,"Vip");

select * from habitacion;

describe reserva;

select * from reserva;

-- CONFIRMADA = 0
INSERT INTO reserva (fecha_inicio, fecha_fin, habitacion_id, estado)
VALUES ('2025-04-20', '2025-04-24', 2, 'RESERVADA');


-- query para ver si la habitaciones disponibles

