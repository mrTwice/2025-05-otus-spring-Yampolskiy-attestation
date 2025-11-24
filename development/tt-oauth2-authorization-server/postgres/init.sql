CREATE USER tt_oauth2_authorization_server_admin PASSWORD 'tt_oauth2_authorization_server_admin';
CREATE DATABASE tt_oauth2_authorization_server OWNER tt_oauth2_authorization_server_admin ENCODING 'UTF8' LC_COLLATE='ru_RU.utf8' LC_CTYPE='ru_RU.utf8' TEMPLATE='template0' CONNECTION LIMIT 10;
GRANT ALL PRIVILEGES ON DATABASE tt_oauth2_authorization_server TO tt_oauth2_authorization_server_admin;