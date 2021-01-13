--insert some sample Data
insert into user (username, password, email, gender, profilepicture, birthdate)
values
('user', '$2a$10$MeWhrWoE2yvclNulZyCZb.IDxlJIhQDMbm8pOVhH3DRGFaz7ZpaaS', 'user@using.com', 'w', 'userpic.png', CURRENT_DATE); -- corresponds to password:'password'
insert into user (username, password, email, gender, profilepicture, birthdate)
values
('user2', '$2a$10$MeWhrWoE2yvclNulZyCZb.IDxlJIhQDMbm8pOVhH3DRGFaz7ZpaaS', 'user@using.com', 'w', 'userpic.png', CURRENT_DATE); -- corresponds to password:'password'




insert into item (title, description, available, picture, user_id)
values
( 'Rasenmaeher','Gutes Geraet, ich liebe es, liebst du es auch?', true, 'maeh.png', 1),
( 'Ruehrer','Ruehrt wie am ersten Tag. Ich will auch ein Stueck vom Kuchen!', true,'ruehr.png', 1),
( 'Bohrmaschine','Hoer mal wer da bohrt.', true,'bohr.png', 1);

