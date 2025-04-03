CREATE TABLE autorstwo (
	Praca NUMBER(3),
	Autor VARCHAR(30) 
);

CREATE TABLE autorzy (
	ID VARCHAR(30),
	Ryzyko INT,
	Sloty VARCHAR(5)
);

CREATE TABLE prace (
	ID NUMBER(3) PRIMARY KEY,
	Tytul LONG NOT NULL,
	Rok NUMBER(4) NOT NULL,
	Autorzy NUMBER(2) NOT NULL,
	Punkty NUMBER(4) NOT NULL
);

-- AUTORSTWO
INSERT INTO autorstwo (Praca, Autor) VALUES (1, 'Schubert');
INSERT INTO autorstwo (Praca, Autor) VALUES (2, 'Clemente');
INSERT INTO autorstwo (Praca, Autor) VALUES (2, 'Czerwinski');
INSERT INTO autorstwo (Praca, Autor) VALUES (2, 'Lasota');
INSERT INTO autorstwo (Praca, Autor) VALUES (3, 'Clemente');
INSERT INTO autorstwo (Praca, Autor) VALUES (3, 'Czerwinski');
INSERT INTO autorstwo (Praca, Autor) VALUES (3, 'Lasota');
INSERT INTO autorstwo (Praca, Autor) VALUES (4, 'Clemente');
INSERT INTO autorstwo (Praca, Autor) VALUES (4, 'Lasota');
INSERT INTO autorstwo (Praca, Autor) VALUES (5, 'Czerwinski');
INSERT INTO autorstwo (Praca, Autor) VALUES (5, 'Lasota');
INSERT INTO autorstwo (Praca, Autor) VALUES (6, 'Dunin-Keplicz');
INSERT INTO autorstwo (Praca, Autor) VALUES (6, 'Szalas');
INSERT INTO autorstwo (Praca, Autor) VALUES (7, 'Dunin-Keplicz');
INSERT INTO autorstwo (Praca, Autor) VALUES (7, 'Szalas');
INSERT INTO autorstwo (Praca, Autor) VALUES (8, 'Dziubinski');
INSERT INTO autorstwo (Praca, Autor) VALUES (9, 'Gorecki');
INSERT INTO autorstwo (Praca, Autor) VALUES (10, 'Gorecki');
INSERT INTO autorstwo (Praca, Autor) VALUES (10, 'Paszek');
INSERT INTO autorstwo (Praca, Autor) VALUES (11, 'Hofman');
INSERT INTO autorstwo (Praca, Autor) VALUES (12, 'Hofman');
INSERT INTO autorstwo (Praca, Autor) VALUES (13, 'Hofman');
INSERT INTO autorstwo (Praca, Autor) VALUES (14, 'Janusz');
INSERT INTO autorstwo (Praca, Autor) VALUES (14, 'Slezak');
INSERT INTO autorstwo (Praca, Autor) VALUES (15, 'Karczmarz');
INSERT INTO autorstwo (Praca, Autor) VALUES (15, 'Sankowski');
INSERT INTO autorstwo (Praca, Autor) VALUES (16, 'Karczmarz');
INSERT INTO autorstwo (Praca, Autor) VALUES (16, 'Sankowski');
INSERT INTO autorstwo (Praca, Autor) VALUES (17, 'Klin');
INSERT INTO autorstwo (Praca, Autor) VALUES (18, 'Klin');
INSERT INTO autorstwo (Praca, Autor) VALUES (19, 'Klin');
INSERT INTO autorstwo (Praca, Autor) VALUES (20, 'Charalampopoulos');
INSERT INTO autorstwo (Praca, Autor) VALUES (20, 'Radoszewski');
INSERT INTO autorstwo (Praca, Autor) VALUES (20, 'Rytter');
INSERT INTO autorstwo (Praca, Autor) VALUES (20, 'Walen');
INSERT INTO autorstwo (Praca, Autor) VALUES (21, 'Kopczynski');
INSERT INTO autorstwo (Praca, Autor) VALUES (21, 'Torunczyk');
INSERT INTO autorstwo (Praca, Autor) VALUES (22, 'Kopczynski');
INSERT INTO autorstwo (Praca, Autor) VALUES (23, 'Celinska-Kopczynska');
INSERT INTO autorstwo (Praca, Autor) VALUES (23, 'Kopczynski');
INSERT INTO autorstwo (Praca, Autor) VALUES (24, 'Cygan');
INSERT INTO autorstwo (Praca, Autor) VALUES (24, 'Kowalik');
INSERT INTO autorstwo (Praca, Autor) VALUES (25, 'Cygan');
INSERT INTO autorstwo (Praca, Autor) VALUES (25, 'Kowalik');
INSERT INTO autorstwo (Praca, Autor) VALUES (26, 'Kowalik');
INSERT INTO autorstwo (Praca, Autor) VALUES (26, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (26, 'Wrochna');
INSERT INTO autorstwo (Praca, Autor) VALUES (27, 'Kowaluk');
INSERT INTO autorstwo (Praca, Autor) VALUES (28, 'Michalak');
INSERT INTO autorstwo (Praca, Autor) VALUES (28, 'Skibski');
INSERT INTO autorstwo (Praca, Autor) VALUES (29, 'Michalak');
INSERT INTO autorstwo (Praca, Autor) VALUES (30, 'Michalak');
INSERT INTO autorstwo (Praca, Autor) VALUES (31, 'Michalak');
INSERT INTO autorstwo (Praca, Autor) VALUES (32, 'Mucha');
INSERT INTO autorstwo (Praca, Autor) VALUES (33, 'Cygan');
INSERT INTO autorstwo (Praca, Autor) VALUES (33, 'Mucha');
INSERT INTO autorstwo (Praca, Autor) VALUES (33, 'Wegrzycki');
INSERT INTO autorstwo (Praca, Autor) VALUES (33, 'Wlodarczyk');
INSERT INTO autorstwo (Praca, Autor) VALUES (34, 'Nguyen AL');
INSERT INTO autorstwo (Praca, Autor) VALUES (35, 'Pacuk');
INSERT INTO autorstwo (Praca, Autor) VALUES (35, 'Sankowski');
INSERT INTO autorstwo (Praca, Autor) VALUES (35, 'Wegrzycki');
INSERT INTO autorstwo (Praca, Autor) VALUES (35, 'Wygocki');
INSERT INTO autorstwo (Praca, Autor) VALUES (36, 'Pilipczuk Ma');
INSERT INTO autorstwo (Praca, Autor) VALUES (37, 'Pilipczuk Ma');
INSERT INTO autorstwo (Praca, Autor) VALUES (37, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (37, 'Wrochna');
INSERT INTO autorstwo (Praca, Autor) VALUES (38, 'Pilipczuk Ma');
INSERT INTO autorstwo (Praca, Autor) VALUES (39, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (40, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (41, 'Bojanczyk');
INSERT INTO autorstwo (Praca, Autor) VALUES (41, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (42, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (42, 'Wrochna');
INSERT INTO autorstwo (Praca, Autor) VALUES (43, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (43, 'Wrochna');
INSERT INTO autorstwo (Praca, Autor) VALUES (44, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (44, 'Wrochna');
INSERT INTO autorstwo (Praca, Autor) VALUES (45, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (46, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (47, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (48, 'Radoszewski');
INSERT INTO autorstwo (Praca, Autor) VALUES (49, 'Charalampopoulos');
INSERT INTO autorstwo (Praca, Autor) VALUES (49, 'Radoszewski');
INSERT INTO autorstwo (Praca, Autor) VALUES (50, 'Charalampopoulos');
INSERT INTO autorstwo (Praca, Autor) VALUES (50, 'Radoszewski');
INSERT INTO autorstwo (Praca, Autor) VALUES (51, 'Charalampopoulos');
INSERT INTO autorstwo (Praca, Autor) VALUES (51, 'Radoszewski');
INSERT INTO autorstwo (Praca, Autor) VALUES (52, 'Rzadca');
INSERT INTO autorstwo (Praca, Autor) VALUES (53, 'Sankowski');
INSERT INTO autorstwo (Praca, Autor) VALUES (54, 'Sankowski');
INSERT INTO autorstwo (Praca, Autor) VALUES (54, 'Wegrzycki');
INSERT INTO autorstwo (Praca, Autor) VALUES (55, 'Sankowski');
INSERT INTO autorstwo (Praca, Autor) VALUES (55, 'Wygocki');
INSERT INTO autorstwo (Praca, Autor) VALUES (56, 'Schubert');
INSERT INTO autorstwo (Praca, Autor) VALUES (57, 'Skibski');
INSERT INTO autorstwo (Praca, Autor) VALUES (58, 'Skrzypczak');
INSERT INTO autorstwo (Praca, Autor) VALUES (59, 'Michalewski');
INSERT INTO autorstwo (Praca, Autor) VALUES (59, 'Skrzypczak');
INSERT INTO autorstwo (Praca, Autor) VALUES (60, 'Slezak');
INSERT INTO autorstwo (Praca, Autor) VALUES (61, 'Adamczyk');
INSERT INTO autorstwo (Praca, Autor) VALUES (61, 'Wlodarczyk');
INSERT INTO autorstwo (Praca, Autor) VALUES (62, 'Clemente');
INSERT INTO autorstwo (Praca, Autor) VALUES (62, 'Lasota');
INSERT INTO autorstwo (Praca, Autor) VALUES (63, 'Cygan');
INSERT INTO autorstwo (Praca, Autor) VALUES (63, 'Mucha');
INSERT INTO autorstwo (Praca, Autor) VALUES (63, 'Sankowski');
INSERT INTO autorstwo (Praca, Autor) VALUES (64, 'Czerwinski');
INSERT INTO autorstwo (Praca, Autor) VALUES (64, 'Parys');
INSERT INTO autorstwo (Praca, Autor) VALUES (65, 'Czerwinski');
INSERT INTO autorstwo (Praca, Autor) VALUES (65, 'Hofman');
INSERT INTO autorstwo (Praca, Autor) VALUES (66, 'Czerwinski');
INSERT INTO autorstwo (Praca, Autor) VALUES (66, 'Lasota');
INSERT INTO autorstwo (Praca, Autor) VALUES (67, 'Dziembowski');
INSERT INTO autorstwo (Praca, Autor) VALUES (68, 'Dziembowski');
INSERT INTO autorstwo (Praca, Autor) VALUES (69, 'Dziubinski');
INSERT INTO autorstwo (Praca, Autor) VALUES (70, 'Gambin');
INSERT INTO autorstwo (Praca, Autor) VALUES (70, 'Niemyska');
INSERT INTO autorstwo (Praca, Autor) VALUES (70, 'Startek');
INSERT INTO autorstwo (Praca, Autor) VALUES (71, 'Gorecki');
INSERT INTO autorstwo (Praca, Autor) VALUES (72, 'Hofman');
INSERT INTO autorstwo (Praca, Autor) VALUES (72, 'Lasota');
INSERT INTO autorstwo (Praca, Autor) VALUES (73, 'Iwanicki');
INSERT INTO autorstwo (Praca, Autor) VALUES (74, 'Iwanicki');
INSERT INTO autorstwo (Praca, Autor) VALUES (75, 'Iwanicki');
INSERT INTO autorstwo (Praca, Autor) VALUES (76, 'Iwanicki');
INSERT INTO autorstwo (Praca, Autor) VALUES (77, 'Janusz');
INSERT INTO autorstwo (Praca, Autor) VALUES (77, 'Slezak');
INSERT INTO autorstwo (Praca, Autor) VALUES (78, 'Janusz');
INSERT INTO autorstwo (Praca, Autor) VALUES (79, 'Karczmarz');
INSERT INTO autorstwo (Praca, Autor) VALUES (80, 'Karczmarz');
INSERT INTO autorstwo (Praca, Autor) VALUES (80, 'Sankowski');
INSERT INTO autorstwo (Praca, Autor) VALUES (81, 'Karczmarz');
INSERT INTO autorstwo (Praca, Autor) VALUES (82, 'Karczmarz');
INSERT INTO autorstwo (Praca, Autor) VALUES (83, 'Radoszewski');
INSERT INTO autorstwo (Praca, Autor) VALUES (83, 'Rytter');
INSERT INTO autorstwo (Praca, Autor) VALUES (83, 'Walen');
INSERT INTO autorstwo (Praca, Autor) VALUES (84, 'Radoszewski');
INSERT INTO autorstwo (Praca, Autor) VALUES (84, 'Rytter');
INSERT INTO autorstwo (Praca, Autor) VALUES (84, 'Straszynski');
INSERT INTO autorstwo (Praca, Autor) VALUES (84, 'Walen');
INSERT INTO autorstwo (Praca, Autor) VALUES (85, 'Charalampopoulos');
INSERT INTO autorstwo (Praca, Autor) VALUES (85, 'Radoszewski');
INSERT INTO autorstwo (Praca, Autor) VALUES (85, 'Rytter');
INSERT INTO autorstwo (Praca, Autor) VALUES (85, 'Walen');
INSERT INTO autorstwo (Praca, Autor) VALUES (86, 'Radoszewski');
INSERT INTO autorstwo (Praca, Autor) VALUES (86, 'Rytter');
INSERT INTO autorstwo (Praca, Autor) VALUES (86, 'Walen');
INSERT INTO autorstwo (Praca, Autor) VALUES (87, 'Charalampopoulos');
INSERT INTO autorstwo (Praca, Autor) VALUES (87, 'Radoszewski');
INSERT INTO autorstwo (Praca, Autor) VALUES (87, 'Straszynski');
INSERT INTO autorstwo (Praca, Autor) VALUES (88, 'Kowalik');
INSERT INTO autorstwo (Praca, Autor) VALUES (88, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (88, 'Wrochna');
INSERT INTO autorstwo (Praca, Autor) VALUES (89, 'Lasota');
INSERT INTO autorstwo (Praca, Autor) VALUES (90, 'Michalak');
INSERT INTO autorstwo (Praca, Autor) VALUES (91, 'Gogacz');
INSERT INTO autorstwo (Praca, Autor) VALUES (91, 'Murlak');
INSERT INTO autorstwo (Praca, Autor) VALUES (92, 'Niemyska');
INSERT INTO autorstwo (Praca, Autor) VALUES (93, 'Parys');
INSERT INTO autorstwo (Praca, Autor) VALUES (94, 'Parys');
INSERT INTO autorstwo (Praca, Autor) VALUES (95, 'Pilipczuk Ma');
INSERT INTO autorstwo (Praca, Autor) VALUES (96, 'Pilipczuk Ma');
INSERT INTO autorstwo (Praca, Autor) VALUES (96, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (97, 'Pilipczuk Ma');
INSERT INTO autorstwo (Praca, Autor) VALUES (98, 'Pilipczuk Ma');
INSERT INTO autorstwo (Praca, Autor) VALUES (99, 'Nadara');
INSERT INTO autorstwo (Praca, Autor) VALUES (99, 'Pilipczuk Ma');
INSERT INTO autorstwo (Praca, Autor) VALUES (100, 'Gajarsky');
INSERT INTO autorstwo (Praca, Autor) VALUES (100, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (100, 'Torunczyk');
INSERT INTO autorstwo (Praca, Autor) VALUES (101, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (102, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (102, 'Torunczyk');
INSERT INTO autorstwo (Praca, Autor) VALUES (103, 'Bojanczyk');
INSERT INTO autorstwo (Praca, Autor) VALUES (103, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (104, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (104, 'Torunczyk');
INSERT INTO autorstwo (Praca, Autor) VALUES (105, 'Diks');
INSERT INTO autorstwo (Praca, Autor) VALUES (105, 'Rytter');
INSERT INTO autorstwo (Praca, Autor) VALUES (106, 'Rzadca');
INSERT INTO autorstwo (Praca, Autor) VALUES (107, 'Rzadca');
INSERT INTO autorstwo (Praca, Autor) VALUES (107, 'Skowron');
INSERT INTO autorstwo (Praca, Autor) VALUES (108, 'Schmude');
INSERT INTO autorstwo (Praca, Autor) VALUES (109, 'Skowron');
INSERT INTO autorstwo (Praca, Autor) VALUES (110, 'Skowron');
INSERT INTO autorstwo (Praca, Autor) VALUES (111, 'Skowron');
INSERT INTO autorstwo (Praca, Autor) VALUES (112, 'Skowron');
INSERT INTO autorstwo (Praca, Autor) VALUES (113, 'Skowron');
INSERT INTO autorstwo (Praca, Autor) VALUES (114, 'Skowron');
INSERT INTO autorstwo (Praca, Autor) VALUES (115, 'Skrzypczak');
INSERT INTO autorstwo (Praca, Autor) VALUES (116, 'Skrzypczak');
INSERT INTO autorstwo (Praca, Autor) VALUES (117, 'Janusz');
INSERT INTO autorstwo (Praca, Autor) VALUES (117, 'Slezak');
INSERT INTO autorstwo (Praca, Autor) VALUES (118, 'Slezak');
INSERT INTO autorstwo (Praca, Autor) VALUES (119, 'Bojanczyk');
INSERT INTO autorstwo (Praca, Autor) VALUES (119, 'Torunczyk');
INSERT INTO autorstwo (Praca, Autor) VALUES (120, 'Skibski');
INSERT INTO autorstwo (Praca, Autor) VALUES (121, 'Skibski');
INSERT INTO autorstwo (Praca, Autor) VALUES (122, 'Adamczyk');
INSERT INTO autorstwo (Praca, Autor) VALUES (122, 'Wlodarczyk');
INSERT INTO autorstwo (Praca, Autor) VALUES (123, 'Bilinski');
INSERT INTO autorstwo (Praca, Autor) VALUES (124, 'Bojanczyk');
INSERT INTO autorstwo (Praca, Autor) VALUES (143, 'Dunin-Keplicz');
INSERT INTO autorstwo (Praca, Autor) VALUES (143, 'Szalas');
INSERT INTO autorstwo (Praca, Autor) VALUES (144, 'Dunin-Keplicz');
INSERT INTO autorstwo (Praca, Autor) VALUES (144, 'Szalas');
INSERT INTO autorstwo (Praca, Autor) VALUES (145, 'Dunin-Keplicz');
INSERT INTO autorstwo (Praca, Autor) VALUES (145, 'Szalas');
INSERT INTO autorstwo (Praca, Autor) VALUES (146, 'Dunin-Keplicz');
INSERT INTO autorstwo (Praca, Autor) VALUES (147, 'Dziembowski');
INSERT INTO autorstwo (Praca, Autor) VALUES (148, 'Dziembowski');
INSERT INTO autorstwo (Praca, Autor) VALUES (149, 'Dziembowski');
INSERT INTO autorstwo (Praca, Autor) VALUES (150, 'Dziembowski');
INSERT INTO autorstwo (Praca, Autor) VALUES (151, 'Fabianski');
INSERT INTO autorstwo (Praca, Autor) VALUES (151, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (151, 'Torunczyk');
INSERT INTO autorstwo (Praca, Autor) VALUES (152, 'Chung');
INSERT INTO autorstwo (Praca, Autor) VALUES (152, 'Gambin');
INSERT INTO autorstwo (Praca, Autor) VALUES (152, 'Startek');
INSERT INTO autorstwo (Praca, Autor) VALUES (153, 'Gambin');
INSERT INTO autorstwo (Praca, Autor) VALUES (153, 'Jaworski');
INSERT INTO autorstwo (Praca, Autor) VALUES (154, 'Gambin');
INSERT INTO autorstwo (Praca, Autor) VALUES (155, 'Gambin');
INSERT INTO autorstwo (Praca, Autor) VALUES (156, 'Chung');
INSERT INTO autorstwo (Praca, Autor) VALUES (156, 'Gambin');
INSERT INTO autorstwo (Praca, Autor) VALUES (156, 'Gogolewski');
INSERT INTO autorstwo (Praca, Autor) VALUES (157, 'Gambin');
INSERT INTO autorstwo (Praca, Autor) VALUES (157, 'Startek');
INSERT INTO autorstwo (Praca, Autor) VALUES (158, 'Gambin');
INSERT INTO autorstwo (Praca, Autor) VALUES (159, 'Gorecki');
INSERT INTO autorstwo (Praca, Autor) VALUES (160, 'Gorecki');
INSERT INTO autorstwo (Praca, Autor) VALUES (160, 'Paszek');
INSERT INTO autorstwo (Praca, Autor) VALUES (161, 'Gorecki');
INSERT INTO autorstwo (Praca, Autor) VALUES (162, 'Gorecki');
INSERT INTO autorstwo (Praca, Autor) VALUES (163, 'Hofman');
INSERT INTO autorstwo (Praca, Autor) VALUES (164, 'Iwanicki');
INSERT INTO autorstwo (Praca, Autor) VALUES (165, 'Kaliszyk');
INSERT INTO autorstwo (Praca, Autor) VALUES (166, 'Kaliszyk');
INSERT INTO autorstwo (Praca, Autor) VALUES (167, 'Kaliszyk');
INSERT INTO autorstwo (Praca, Autor) VALUES (168, 'Karczmarz');
INSERT INTO autorstwo (Praca, Autor) VALUES (169, 'Karczmarz');
INSERT INTO autorstwo (Praca, Autor) VALUES (169, 'Sankowski');
INSERT INTO autorstwo (Praca, Autor) VALUES (170, 'Klin');
INSERT INTO autorstwo (Praca, Autor) VALUES (171, 'Klin');
INSERT INTO autorstwo (Praca, Autor) VALUES (172, 'Klin');
INSERT INTO autorstwo (Praca, Autor) VALUES (173, 'Klin');
INSERT INTO autorstwo (Praca, Autor) VALUES (174, 'Klin');
INSERT INTO autorstwo (Praca, Autor) VALUES (174, 'Lasota');
INSERT INTO autorstwo (Praca, Autor) VALUES (174, 'Torunczyk');
INSERT INTO autorstwo (Praca, Autor) VALUES (175, 'Kopczynski');
INSERT INTO autorstwo (Praca, Autor) VALUES (176, 'Kowalik');
INSERT INTO autorstwo (Praca, Autor) VALUES (177, 'Kowalik');
INSERT INTO autorstwo (Praca, Autor) VALUES (177, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (177, 'Wrochna');
INSERT INTO autorstwo (Praca, Autor) VALUES (178, 'Kowaluk');
INSERT INTO autorstwo (Praca, Autor) VALUES (179, 'Kowaluk');
INSERT INTO autorstwo (Praca, Autor) VALUES (180, 'Kozlowski');
INSERT INTO autorstwo (Praca, Autor) VALUES (181, 'Michalak');
INSERT INTO autorstwo (Praca, Autor) VALUES (181, 'Skibski');
INSERT INTO autorstwo (Praca, Autor) VALUES (182, 'Michalak');
INSERT INTO autorstwo (Praca, Autor) VALUES (182, 'Skibski');
INSERT INTO autorstwo (Praca, Autor) VALUES (183, 'Michalak');
INSERT INTO autorstwo (Praca, Autor) VALUES (184, 'Michalak');
INSERT INTO autorstwo (Praca, Autor) VALUES (185, 'Michalak');
INSERT INTO autorstwo (Praca, Autor) VALUES (185, 'Skibski');
INSERT INTO autorstwo (Praca, Autor) VALUES (186, 'Michalak');
INSERT INTO autorstwo (Praca, Autor) VALUES (187, 'Michalak');
INSERT INTO autorstwo (Praca, Autor) VALUES (188, 'Mucha');
INSERT INTO autorstwo (Praca, Autor) VALUES (188, 'Wegrzycki');
INSERT INTO autorstwo (Praca, Autor) VALUES (188, 'Wlodarczyk');
INSERT INTO autorstwo (Praca, Autor) VALUES (189, 'Mucha');
INSERT INTO autorstwo (Praca, Autor) VALUES (189, 'Pawlewicz');
INSERT INTO autorstwo (Praca, Autor) VALUES (189, 'Wegrzycki');
INSERT INTO autorstwo (Praca, Autor) VALUES (190, 'Murlak');
INSERT INTO autorstwo (Praca, Autor) VALUES (191, 'Gogacz');
INSERT INTO autorstwo (Praca, Autor) VALUES (191, 'Murlak');
INSERT INTO autorstwo (Praca, Autor) VALUES (192, 'Gorecki');
INSERT INTO autorstwo (Praca, Autor) VALUES (193, 'Nguyen AL');
INSERT INTO autorstwo (Praca, Autor) VALUES (194, 'Nguyen AL');
INSERT INTO autorstwo (Praca, Autor) VALUES (195, 'Nguyen AL');
INSERT INTO autorstwo (Praca, Autor) VALUES (196, 'Nguyen AL');
INSERT INTO autorstwo (Praca, Autor) VALUES (197, 'Nguyen AL');
INSERT INTO autorstwo (Praca, Autor) VALUES (198, 'Nguyen AL');
INSERT INTO autorstwo (Praca, Autor) VALUES (199, 'Niemyska');
INSERT INTO autorstwo (Praca, Autor) VALUES (200, 'Niemyska');
INSERT INTO autorstwo (Praca, Autor) VALUES (201, 'Parys');
INSERT INTO autorstwo (Praca, Autor) VALUES (202, 'Parys');
INSERT INTO autorstwo (Praca, Autor) VALUES (203, 'Gorecki');
INSERT INTO autorstwo (Praca, Autor) VALUES (203, 'Paszek');
INSERT INTO autorstwo (Praca, Autor) VALUES (204, 'Gorecki');
INSERT INTO autorstwo (Praca, Autor) VALUES (204, 'Paszek');
INSERT INTO autorstwo (Praca, Autor) VALUES (205, 'Peczarski');
INSERT INTO autorstwo (Praca, Autor) VALUES (206, 'Pilipczuk Ma');
INSERT INTO autorstwo (Praca, Autor) VALUES (207, 'Pilipczuk Ma');
INSERT INTO autorstwo (Praca, Autor) VALUES (208, 'Zych-Pawlewicz');
INSERT INTO autorstwo (Praca, Autor) VALUES (209, 'Pilipczuk Ma');
INSERT INTO autorstwo (Praca, Autor) VALUES (209, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (210, 'Pilipczuk Ma');
INSERT INTO autorstwo (Praca, Autor) VALUES (210, 'Wrochna');
INSERT INTO autorstwo (Praca, Autor) VALUES (211, 'Nadara');
INSERT INTO autorstwo (Praca, Autor) VALUES (211, 'Pilipczuk Ma');
INSERT INTO autorstwo (Praca, Autor) VALUES (212, 'Pilipczuk Ma');
INSERT INTO autorstwo (Praca, Autor) VALUES (213, 'Pilipczuk Ma');
INSERT INTO autorstwo (Praca, Autor) VALUES (214, 'Pilipczuk Ma');
INSERT INTO autorstwo (Praca, Autor) VALUES (214, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (215, 'Pilipczuk Ma');
INSERT INTO autorstwo (Praca, Autor) VALUES (215, 'Sorge');
INSERT INTO autorstwo (Praca, Autor) VALUES (216, 'Pilipczuk Ma');
INSERT INTO autorstwo (Praca, Autor) VALUES (216, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (217, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (218, 'Novotná');
INSERT INTO autorstwo (Praca, Autor) VALUES (218, 'Okrasa');
INSERT INTO autorstwo (Praca, Autor) VALUES (218, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (218, 'Rzazewski');
INSERT INTO autorstwo (Praca, Autor) VALUES (219, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (219, 'Wrochna');
INSERT INTO autorstwo (Praca, Autor) VALUES (220, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (221, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (222, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (223, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (223, 'Wrochna');
INSERT INTO autorstwo (Praca, Autor) VALUES (224, 'Przybylek');
INSERT INTO autorstwo (Praca, Autor) VALUES (225, 'Radoszewski');
INSERT INTO autorstwo (Praca, Autor) VALUES (321, 'Parys');
INSERT INTO autorstwo (Praca, Autor) VALUES (322, 'Pilipczuk Ma');
INSERT INTO autorstwo (Praca, Autor) VALUES (322, 'Sorge');
INSERT INTO autorstwo (Praca, Autor) VALUES (323, 'Bellitto');
INSERT INTO autorstwo (Praca, Autor) VALUES (323, 'Li');
INSERT INTO autorstwo (Praca, Autor) VALUES (323, 'Okrasa');
INSERT INTO autorstwo (Praca, Autor) VALUES (323, 'Pilipczuk Ma');
INSERT INTO autorstwo (Praca, Autor) VALUES (323, 'Sorge');
INSERT INTO autorstwo (Praca, Autor) VALUES (324, 'Li');
INSERT INTO autorstwo (Praca, Autor) VALUES (324, 'Pilipczuk Ma');
INSERT INTO autorstwo (Praca, Autor) VALUES (325, 'Pilipczuk Ma');
INSERT INTO autorstwo (Praca, Autor) VALUES (325, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (326, 'Pilipczuk Ma');
INSERT INTO autorstwo (Praca, Autor) VALUES (326, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (327, 'Pilipczuk Ma');
INSERT INTO autorstwo (Praca, Autor) VALUES (328, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (328, 'Wegrzycki');
INSERT INTO autorstwo (Praca, Autor) VALUES (329, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (330, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (331, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (332, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (333, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (334, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (335, 'Gajarsky');
INSERT INTO autorstwo (Praca, Autor) VALUES (335, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (335, 'Torunczyk');
INSERT INTO autorstwo (Praca, Autor) VALUES (336, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (337, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (338, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (339, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (340, 'Pilipczuk Mi');
INSERT INTO autorstwo (Praca, Autor) VALUES (340, 'Wrochna');
INSERT INTO autorstwo (Praca, Autor) VALUES (341, 'Radoszewski');
INSERT INTO autorstwo (Praca, Autor) VALUES (341, 'Rytter');
INSERT INTO autorstwo (Praca, Autor) VALUES (341, 'Walen');
INSERT INTO autorstwo (Praca, Autor) VALUES (342, 'Radoszewski');
INSERT INTO autorstwo (Praca, Autor) VALUES (343, 'Charalampopoulos');
INSERT INTO autorstwo (Praca, Autor) VALUES (343, 'Radoszewski');
INSERT INTO autorstwo (Praca, Autor) VALUES (343, 'Rytter');
INSERT INTO autorstwo (Praca, Autor) VALUES (343, 'Straszynski');
INSERT INTO autorstwo (Praca, Autor) VALUES (343, 'Walen');
INSERT INTO autorstwo (Praca, Autor) VALUES (344, 'Charalampopoulos');
INSERT INTO autorstwo (Praca, Autor) VALUES (344, 'Radoszewski');
INSERT INTO autorstwo (Praca, Autor) VALUES (345, 'Radoszewski');
INSERT INTO autorstwo (Praca, Autor) VALUES (345, 'Straszynski');
INSERT INTO autorstwo (Praca, Autor) VALUES (346, 'Charalampopoulos');
INSERT INTO autorstwo (Praca, Autor) VALUES (346, 'Radoszewski');
INSERT INTO autorstwo (Praca, Autor) VALUES (346, 'Rytter');
INSERT INTO autorstwo (Praca, Autor) VALUES (346, 'Walen');
INSERT INTO autorstwo (Praca, Autor) VALUES (347, 'Charalampopoulos');
INSERT INTO autorstwo (Praca, Autor) VALUES (347, 'Radoszewski');
INSERT INTO autorstwo (Praca, Autor) VALUES (348, 'Radoszewski');
INSERT INTO autorstwo (Praca, Autor) VALUES (349, 'Radoszewski');
INSERT INTO autorstwo (Praca, Autor) VALUES (349, 'Rytter');
INSERT INTO autorstwo (Praca, Autor) VALUES (349, 'Straszynski');
INSERT INTO autorstwo (Praca, Autor) VALUES (349, 'Walen');
INSERT INTO autorstwo (Praca, Autor) VALUES (350, 'Radoszewski');
INSERT INTO autorstwo (Praca, Autor) VALUES (350, 'Rytter');
INSERT INTO autorstwo (Praca, Autor) VALUES (350, 'Straszynski');
INSERT INTO autorstwo (Praca, Autor) VALUES (350, 'Walen');
INSERT INTO autorstwo (Praca, Autor) VALUES (351, 'Radoszewski');
INSERT INTO autorstwo (Praca, Autor) VALUES (352, 'Radoszewski');
INSERT INTO autorstwo (Praca, Autor) VALUES (352, 'Rytter');
INSERT INTO autorstwo (Praca, Autor) VALUES (352, 'Walen');
INSERT INTO autorstwo (Praca, Autor) VALUES (353, 'Charalampopoulos');
INSERT INTO autorstwo (Praca, Autor) VALUES (353, 'Radoszewski');
INSERT INTO autorstwo (Praca, Autor) VALUES (353, 'Rytter');
INSERT INTO autorstwo (Praca, Autor) VALUES (353, 'Walen');
INSERT INTO autorstwo (Praca, Autor) VALUES (354, 'Charalampopoulos');
INSERT INTO autorstwo (Praca, Autor) VALUES (354, 'Radoszewski');
INSERT INTO autorstwo (Praca, Autor) VALUES (354, 'Walen');
INSERT INTO autorstwo (Praca, Autor) VALUES (355, 'Radoszewski');
INSERT INTO autorstwo (Praca, Autor) VALUES (355, 'Rytter');
INSERT INTO autorstwo (Praca, Autor) VALUES (355, 'Walen');
INSERT INTO autorstwo (Praca, Autor) VALUES (356, 'Radoszewski');
INSERT INTO autorstwo (Praca, Autor) VALUES (357, 'Rzadca');
INSERT INTO autorstwo (Praca, Autor) VALUES (358, 'Rzadca');
INSERT INTO autorstwo (Praca, Autor) VALUES (359, 'Schmude');
INSERT INTO autorstwo (Praca, Autor) VALUES (360, 'Skibski');
INSERT INTO autorstwo (Praca, Autor) VALUES (361, 'Skowron');
INSERT INTO autorstwo (Praca, Autor) VALUES (362, 'Skowron');
INSERT INTO autorstwo (Praca, Autor) VALUES (363, 'Skowron');
INSERT INTO autorstwo (Praca, Autor) VALUES (364, 'Skowron');
INSERT INTO autorstwo (Praca, Autor) VALUES (365, 'Skowron');
INSERT INTO autorstwo (Praca, Autor) VALUES (366, 'Skowron');
INSERT INTO autorstwo (Praca, Autor) VALUES (367, 'Skowron');
INSERT INTO autorstwo (Praca, Autor) VALUES (368, 'Skowron');
INSERT INTO autorstwo (Praca, Autor) VALUES (369, 'Skrzypczak');
INSERT INTO autorstwo (Praca, Autor) VALUES (370, 'Michielini');
INSERT INTO autorstwo (Praca, Autor) VALUES (370, 'Skrzypczak');
INSERT INTO autorstwo (Praca, Autor) VALUES (371, 'Fabianski');
INSERT INTO autorstwo (Praca, Autor) VALUES (371, 'Skrzypczak');
INSERT INTO autorstwo (Praca, Autor) VALUES (371, 'Torunczyk');
INSERT INTO autorstwo (Praca, Autor) VALUES (372, 'Slezak');
INSERT INTO autorstwo (Praca, Autor) VALUES (373, 'Slezak');
INSERT INTO autorstwo (Praca, Autor) VALUES (374, 'Slezak');
INSERT INTO autorstwo (Praca, Autor) VALUES (375, 'Slezak');
INSERT INTO autorstwo (Praca, Autor) VALUES (376, 'Slezak');
INSERT INTO autorstwo (Praca, Autor) VALUES (377, 'Slezak');
INSERT INTO autorstwo (Praca, Autor) VALUES (378, 'Startek');
INSERT INTO autorstwo (Praca, Autor) VALUES (379, 'Startek');
INSERT INTO autorstwo (Praca, Autor) VALUES (380, 'Szalas');
INSERT INTO autorstwo (Praca, Autor) VALUES (381, 'Szalas');
INSERT INTO autorstwo (Praca, Autor) VALUES (382, 'Szalas');
INSERT INTO autorstwo (Praca, Autor) VALUES (383, 'Szczurek');
INSERT INTO autorstwo (Praca, Autor) VALUES (384, 'Szczurek');
INSERT INTO autorstwo (Praca, Autor) VALUES (385, 'Szczurek');
INSERT INTO autorstwo (Praca, Autor) VALUES (386, 'Gorecki');
INSERT INTO autorstwo (Praca, Autor) VALUES (386, 'Tiuryn');
INSERT INTO autorstwo (Praca, Autor) VALUES (387, 'Torunczyk');
INSERT INTO autorstwo (Praca, Autor) VALUES (388, 'Torunczyk');
INSERT INTO autorstwo (Praca, Autor) VALUES (389, 'Wilczynski');




--- AUTORZY
INSERT INTO autorzy (ID, Ryzyko, Sloty) VALUES ('Lasota', 1, '4');
INSERT INTO autorzy (ID, Ryzyko, Sloty) VALUES ('Lhote', 0, '1,6');
INSERT INTO autorzy (ID, Ryzyko, Sloty) VALUES ('Li', 1, '3,2');
INSERT INTO autorzy (ID, Ryzyko, Sloty) VALUES ('Machnicka', 1, '3,2');
INSERT INTO autorzy (ID, Ryzyko, Sloty) VALUES ('Majewski', 0, '1,44');
INSERT INTO autorzy (ID, Ryzyko, Sloty) VALUES ('Mari', 0, '1');
INSERT INTO autorzy (ID, Ryzyko, Sloty) VALUES ('Masařik', 0, '1,6');
INSERT INTO autorzy (ID, Ryzyko, Sloty) VALUES ('Michalak', 1, '4');
INSERT INTO autorzy (ID, Ryzyko, Sloty) VALUES ('Michalewski', 1, '1,25');
INSERT INTO autorzy (ID, Ryzyko, Sloty) VALUES ('Michielini', 1, '3,2');
INSERT INTO autorzy (ID, Ryzyko, Sloty) VALUES ('Mucha', 1, '4');
INSERT INTO autorzy (ID, Ryzyko, Sloty) VALUES ('Mukherjee', 0, '1,6');
INSERT INTO autorzy (ID, Ryzyko, Sloty) VALUES ('Murlak', 1, '4');
INSERT INTO autorzy (ID, Ryzyko, Sloty) VALUES ('Muzi', 0, '1,6');
INSERT INTO autorzy (ID, Ryzyko, Sloty) VALUES ('Nadara', 1, '2,4');
INSERT INTO autorzy (ID, Ryzyko, Sloty) VALUES ('Nguyen AL', 1, '4');
INSERT INTO autorzy (ID, Ryzyko, Sloty) VALUES ('Nguyen HS', 1, '4');
INSERT INTO autorzy (ID, Ryzyko, Sloty) VALUES ('Niemyska', 1, '4');
INSERT INTO autorzy (ID, Ryzyko, Sloty) VALUES ('Niwinski', 1, '4');
INSERT INTO autorzy (ID, Ryzyko, Sloty) VALUES ('Novotná', 0, '2,4');
INSERT INTO autorzy (ID, Ryzyko, Sloty) VALUES ('Nyporko', 0, '1');
INSERT INTO autorzy (ID, Ryzyko, Sloty) VALUES ('Odrzygozdz', 0, '1');
INSERT INTO autorzy (ID, Ryzyko, Sloty) VALUES ('Okrasa', 0, '2,4');
INSERT INTO autorzy (ID, Ryzyko, Sloty) VALUES ('Pacuk', 0, '2');
INSERT INTO autorzy (ID, Ryzyko, Sloty) VALUES ('Parys', 1, '4');
INSERT INTO autorzy (ID, Ryzyko, Sloty) VALUES ('Paszek', 0, '1,6');
INSERT INTO autorzy (ID, Ryzyko, Sloty) VALUES ('Pawlewicz', 1, '4');
INSERT INTO autorzy (ID, Ryzyko, Sloty) VALUES ('Pilipczuk Ma', 1, '4');
INSERT INTO autorzy (ID, Ryzyko, Sloty) VALUES ('Pilipczuk Mi', 1, '4');

WITH Krawedzie AS (
SELECT DISTINCT
	a.autor AS autor1,
	b.autor AS autor2
FROM
	autorstwo a
LEFT JOIN
	autorstwo b
ON a.praca = b.praca AND a.autor != b.autor
),
wspol AS (
SELECT
	ID AS autor,
	autor2 AS kolaborowal
FROM
	autorzy a
LEFT JOIN
	Krawedzie b
ON ID = autor1
),
drzewo AS (SELECT
	autor,
	MIN(LEVEL-1) AS Pilipczuk_number
FROM
	wspol
START WITH
	autor = 'Pilipczuk Mi'
CONNECT BY
	NOCYCLE autor = PRIOR kolaborowal
GROUP BY autor)
SELECT
	a.autor,
	MIN(Pilipczuk_number) AS Pilipczuk_number
FROM
	wspol a
LEFT JOIN
	drzewo b
ON a.autor = b.autor
GROUP BY a.autor;
DROP TABLE autorzy;
DROP TABLE autorstwo;
