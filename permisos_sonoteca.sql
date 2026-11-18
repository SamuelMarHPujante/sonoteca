-- Para permisos al usuario sonoteca
-- psql -h localhost -U postgres -d sonoteca
-- ---------------------------------------------------------------
GRANT ALL ON SCHEMA public TO sonoteca;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO sonoteca;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO sonoteca;