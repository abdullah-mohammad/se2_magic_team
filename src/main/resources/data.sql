--insert some sample Data
insert into item (title, description, published)
values
( 'Rasenmaeher','Gutes Geraet, ich liebe es, liebst du es auch?', true),
( 'Ruehrer','Ruehrt wie am ersten Tag. Ich will auch ein Stueck vom Kuchen!', true),
( 'Bohrmaschine','Hoer mal wer da bohrt.', true);

insert into user (username, password)
values
('user', '$2a$10$MeWhrWoE2yvclNulZyCZb.IDxlJIhQDMbm8pOVhH3DRGFaz7ZpaaS'); -- corresponds to password:'password'

