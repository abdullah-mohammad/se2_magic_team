--insert some sample Data
insert into user (username, password, firstname, lastname, email, gender, profilepicture, birthdate)
values
('user', '$2a$10$MeWhrWoE2yvclNulZyCZb.IDxlJIhQDMbm8pOVhH3DRGFaz7ZpaaS', 'User 1', 'isLastaName', 'user@using.com', 'w', 'userpic.png', CURRENT_DATE); -- corresponds to password:'password'
insert into user (username, password, firstname, lastname, email, gender, profilepicture, birthdate)
values
('user2', '$2a$10$MeWhrWoE2yvclNulZyCZb.IDxlJIhQDMbm8pOVhH3DRGFaz7ZpaaS', 'User 2', 'isLastaName2', 'user@using.com', 'w', 'userpic.png', CURRENT_DATE); -- corresponds to password:'password'


insert into item (title, description, available, picture, user_id)
values
( 'Rasenmaeher','Gutes Geraet, ich liebe es, liebst du es auch?', true, 'maeh.jpg', 1),
( 'Ruehrer','Ruehrt wie am ersten Tag. Ich will auch ein Stueck vom Kuchen!', true,'ruehr.jpg', 2),
( 'Bohrmaschine','Hoer mal wer da bohrt.', true,'bohr.png', 1),
( 'Akku-Schrauber','Wir befinden uns noch immer in einer außergewöhnlichen Situation und es nicht sicher, dass wir in diesem Semester mit vertretbarem Risiko auf »Geschützte Präsenz« umstellen können.Le ans signification utilisée à titre provisoire pour calibrer un Lorem. Ausserdem ist dieser Text lang, also damit meine ich wirklich wirklich lang. So lang, dass man ihn gar nicht so einfach in die Datenbank einfügen könnte wenn man sie nicht so eingestellt hätte dass es funktionieren würde. Momentan ist die maximale Größe eines Strings den wir in die Description Spalte einfügen können 3000. Das reicht schon um das ein oder andere zu schreiben, das freut mich doch sehr.', false, 'mahe.png', 2),
( 'Kaffemaschine','Es ist eine echte Herausforderung für mich, dieses Kommunikation- Seminar mit Ihnen ohne persönlichen Kontakt durchzuführen.', true,'ruehr.png', 1),
( 'Bohrmaschine','Allen Widrigkeiten zum Trotze möchte ich Ihnen gerne etwas beibringen und Ihnen nach Möglichkeit auch die erfolgreiche Teilnahme bescheinigen.', false,'bohr.png', 1);


