INSERT INTO users (full_name, email, password, registration_date, username, role, enabled) VALUES
('Jonas Emil', 'jonasemil@email.com', '$2a$12$YfBGX185w6RRfa5Y8/QB5OPdymf2v9b91VVsGG0kdAT0dZ03J365a', '2022-10-28', 'emilke11', 'ADMIN', true),
('Kiss Gabor', 'kissgabor14@email.com', '$2a$12$YfBGX185w6RRfa5Y8/QB5OPdymf2v9b91VVsGG0kdAT0dZ03J365a', '2022-10-27', 'gabesz77', 'USER', true),
('Nagy István', 'nagypityu@email.com', '$2a$12$YfBGX185w6RRfa5Y8/QB5OPdymf2v9b91VVsGG0kdAT0dZ03J365a', '2022-10-27', 'pityu77', 'USER', false);

INSERT INTO auth_token (expiry_date, token, token_type, user_id) VALUES
('2022-10-27 11:42:57', 'f24a2dcd-9dc8-4772-99f2-e1dbb6555752', 'VERIFY', 3),
('2022-10-27 11:42:57', 'b83ec2e1-f6ef-4649-a614-62d31cb0d3e3', 'PASSWORD', 2);
