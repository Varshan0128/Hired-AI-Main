-- 1. Create hiredai_intelligence database
CREATE DATABASE hiredai_intelligence;

-- 2. Create intelligence_user with a strong password
-- Note: Replace 'StrongPasswordHere123!' with your actual secure production password
CREATE USER intelligence_user WITH PASSWORD 'StrongPasswordHere123!';

-- 3. Revoke connect privileges on the new database from PUBLIC (all users by default)
REVOKE CONNECT ON DATABASE hiredai_intelligence FROM PUBLIC;

-- 4. Grant connect privilege on hiredai_intelligence database only to intelligence_user
GRANT CONNECT ON DATABASE hiredai_intelligence TO intelligence_user;

-- 5. Revoke connect privileges from intelligence_user on other databases
-- Explicitly revoke connect on default 'postgres' database
REVOKE CONNECT ON DATABASE postgres FROM intelligence_user;
-- Explicitly revoke connect on other services' databases (e.g. auth database, if they exist)
-- REVOKE CONNECT ON DATABASE hiredai_auth FROM intelligence_user;
-- REVOKE CONNECT ON DATABASE hiredai_resume FROM intelligence_user;

-- 6. Grant schema and table access permissions
-- NOTE: Connect to the hiredai_intelligence database before executing these:
-- \c hiredai_intelligence
GRANT ALL ON SCHEMA public TO intelligence_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO intelligence_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO intelligence_user;

-- Ensure that tables/sequences created in the future are also accessible by intelligence_user
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO intelligence_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO intelligence_user;

-- 7. Confirm lockdown:
-- Ensure other service users (like auth_user or resume_user) cannot access hiredai_intelligence
-- REVOKE CONNECT ON DATABASE hiredai_intelligence FROM auth_user;
-- REVOKE CONNECT ON DATABASE hiredai_intelligence FROM resume_user;
