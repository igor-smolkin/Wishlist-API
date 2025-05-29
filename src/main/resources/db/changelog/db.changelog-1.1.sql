-- changeset ataraxii:1.1-drop-unique-folder-constraint
ALTER TABLE folder DROP CONSTRAINT folder_user_id_name_key;
