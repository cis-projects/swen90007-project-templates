BEGIN;
INSERT INTO app.vote (id, name, email, supporting, status, created) VALUES
    (gen_random_uuid(), 'Pepe Roni', 'pepe.roni@pie.com', true, 'UNVERIFIED', now()),
    (gen_random_uuid(), 'Barbie Queue', 'b.queue@slice.com', false, 'UNVERIFIED', now());
COMMIT;