--insert some sample Data
insert into address (street, streetnumber, zipcode, city, country)
values
( 'Gärtnerhof','8', 25462, 'Rellingen', 'Deutschland'),
( 'Berliner Tor','5', 20099, 'Hamburg', 'Deutschland');


insert into user (username, password, firstname, lastname, email, gender, profilepicture, birthdate, address_id)
values
('user', '$2a$10$MeWhrWoE2yvclNulZyCZb.IDxlJIhQDMbm8pOVhH3DRGFaz7ZpaaS', 'Jill', 'Haseluff', 'j.h@using.com', 'm', 'user1.jpg', CURRENT_DATE, 1); -- corresponds to password:'password'
insert into user (username, password, firstname, lastname, email, gender, profilepicture, birthdate, address_id)
values
('user2', '$2a$10$MeWhrWoE2yvclNulZyCZb.IDxlJIhQDMbm8pOVhH3DRGFaz7ZpaaS', 'Max', 'Power', 'power@POWER.com', 'm', 'user2.jpg', CURRENT_DATE, 2); -- corresponds to password:'password'


insert into item (title, description, available_from, available_to, picture, user_id)
values
( 'Rasenmäher-Roboter','Ich liebe diesen Roboter und finde jeder sollte mal die Chance gehabt haben so einen in seinem Garten fahren zu lassen!' ,'2021-02-02' , DATEADD('DAY', + 10, '2021-02-02') , 'rasenmaehrer_roboter.jpg', 1),
( 'Schubkarre','Bis zu 200kg Tragekapazität. Bitte poliert zurück!' ,'2021-02-02' , DATEADD('DAY', + 15, '2021-02-02'),'schubkarre.jpg', 2),
( 'Akkuschrauber','So viele Schrauben wurden hiermit geschraubt. Helft diesem Stück seine Legende fortzuschreiben','2021-02-02' , DATEADD('DAY', + 20, '2021-02-02'),'schrauber.jpg', 1),
( 'Roomba','Dieser Staubsaugerroboter ist der Wahnsinns schlechthin! Er hat eine eigene Persönlichkeit und ist immer da um einen aufzuheitern. VORSICHT! Er verträgt sich nicht gut mit Rasenmäher-Robotern. Wenn er einen Solchen sieht fängt er an wild rumzupiepsen und verliert all seinen Staub.' ,'2021-02-02' , DATEADD('DAY', + 10, '2021-02-02'), 'roomba.jpg', 2),
( 'Kaffemaschine','Nicht die neuste Maschine, tut aber was sie soll, lecker schwarzer Kaffee.' ,'2021-02-02' , DATEADD('DAY', + 15, '2021-02-02'),'kaffeemaschine.jpg', 1),
( 'Bohrmaschine','Wie ein Mann kannst du auch mit dieser Maschine aussehen.' ,'2021-02-05' , DATEADD('DAY', + 11, '2021-02-05'),'bohrmaschine.jpg', 1),
( 'Schraubenzieher','Ein wirklich großes Set einwandfreier Schraubenzieher.' ,'2021-02-02' , DATEADD('DAY', + 15, '2021-02-02'),'schraubenzieher.jpg', 1),
( 'Thermomix','Ich hasse dieses Teil, kannst du auch geschenkt haben!' ,'2021-02-05' , DATEADD('DAY', + 20, '2021-02-05'),'thermomix.jpg', 1);

