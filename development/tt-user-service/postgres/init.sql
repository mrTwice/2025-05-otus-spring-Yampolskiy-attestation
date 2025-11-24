CREATE USER tt_user_service_admin PASSWORD 'tt_user_service_admin';
CREATE DATABASE tt_user_service OWNER tt_user_service_admin ENCODING 'UTF8' LC_COLLATE='ru_RU.utf8' LC_CTYPE='ru_RU.utf8' TEMPLATE='template0' CONNECTION LIMIT 10;
GRANT ALL PRIVILEGES ON DATABASE tt_user_service TO tt_user_service_admin;