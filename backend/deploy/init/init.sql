insert into person.base (first_name, last_name, email) values
    ('Rusticanella', 'Gombardino', 'rusticanella.gombardino@email.com'),
    ('Veridiana', 'Benincasa', 'veridiana.benincasa@email.com'),
    ('Agostino', 'Botticelli', 'agostino.botticelli@email.com'),
    ('Alessandrina', 'Capone', 'alessandrina.capone@email.com'),
    ('Bernardino', 'Carrozza', 'bernardino.carrozza@email.com'),
    ('Carlo', 'Cesario', 'carlo.cesario@email.com'),
    ('Cesidia', 'Colonna', 'cesidia.colonna@email.com'),
    ('Delfina', 'Donatello', 'delfina.donatello@email.com'),
    ('Edmondo', 'Esposito', 'edmondo.esposito@email.com'),
    ('Fabrizia', 'Ferragamo', 'fabrizia.ferragamo@email.com'),
    ('Graziana', 'Ghirlanda', 'graziana.ghirlanda@email.com'),
    ('Isabella', 'Iannucci', 'isabella.iannucci@email.com'),
    ('Jacopo', 'Lorenzetti', 'jacopo.lorenzetti@email.com'),
    ('Leandro', 'Mancini', 'leandro.mancini@email.com'),
    ('Lisandro', 'Marcellino', 'lisandro.marcellino@email.com'),
    ('Luciano', 'Marini', 'luciano.marini@email.com'),
    ('Maddalena', 'Martellini', 'maddalena.martellini@email.com'),
    ('Marcella', 'Milanesi', 'marcella.milanesi@email.com'),
    ('Nerio', 'Nocentini', 'nerio.nocentini@email.com'),
    ('Nicoletta', 'Orsini', 'nicoletta.orsini@email.com'),
    ('Orlando', 'Palladino', 'orlando.palladino@email.com'),
    ('Palmina', 'Perugino', 'palmina.perugino@email.com'),
    ('Pompeo', 'Pieroni', 'pompeo.pieroni@email.com'),
    ('Roberto', 'Romani', 'roberto.romani@email.com'),
    ('Romualdo', 'Rossellini', 'romualdo.rossellini@email.com'),
    ('Rufino', 'Santoro', 'rufino.santoro@email.com'),
    ('Silvano', 'Santucci', 'silvano.santucci@email.com'),
    ('Teodora', 'Sartore', 'teodora.sartore@email.com'),
    ('Tommaso', 'Savonarola', 'tommaso.savonarola@email.com'),
    ('Umberto', 'Sforza', 'umberto.sforza@email.com'),
    ('Vincenza', 'Soriano', 'vincenza.soriano@email.com'),
    ('Vincent', 'Strozzi', 'vincent.strozzi@email.com'),
    ('Vittorina', 'Toscanini', 'vittorina.toscanini@email.com'),
    ('Zenobio', 'Valentini', 'zenobio.valentini@email.com'),
    ('Brunella', 'Vivaldi', 'brunella.vivaldi@email.com'),
    ('Claudiano', 'Vittorio', 'claudiano.vittorio@email.com'),
    ('Diamante', 'Zanetti', 'diamante.zanetti@email.com'),
    ('Fabiano', 'Biancardi', 'fabiano.biancardi@email.com'),
    ('Ferdinanda', 'Calabrese', 'ferdinanda.calabrese@email.com'),
    ('Fioralba', 'Damico', 'fioralba.damico@email.com'),
    ('Fortunato', 'De Luca', 'fortunato.deluca@email.com'),
    ('Gabriella', 'Ferrero', 'gabriella.ferrero@email.com'),
    ('Gelsomino', 'Giovanni', 'gelsomino.giovanni@email.com'),
    ('Geronimo', 'Grimaldi', 'geronimo.grimaldi@email.com'),
    ('Giovannella', 'Marino', 'giovannella.marino@email.com'),
    ('Leopoldo', 'Napoli', 'leopoldo.napoli@email.com'),
    ('Loreto', 'Palermo', 'loreto.palermo@email.com'),
    ('Luigia', 'Pelligrino', 'luigia.pelligrino@email.com'),
    ('Riccardo', 'Rinaldi', 'riccardo.rinaldi@email.com'),
    ('Violetta', 'Santagata', 'violetta.santagata@email.com');

insert into person.awards_representative (person_id) values (1), (2), (3), (4), (5), (6), (7), (8), (9), (10);
insert into person.jury_member (person_id) values (11), (12), (13), (14), (15), (16), (17), (18), (19), (20);
insert into person.ipma_expert (person_id) values (21), (22), (23), (24), (25), (26), (27), (28), (29), (30);
insert into person.applicant (person_id) values (31), (32), (33), (34), (35), (36), (37), (38), (39), (40);
insert into person.ipma_expert (person_id) values (41), (42), (43), (44), (45), (46), (47), (48), (49), (50);
insert into person.assessor (ipma_expert_id) values (1), (2), (3), (4), (5), (6), (7), (8), (9), (10);

insert into edition.contest ("year", "master_jury_id", "est_time_individual_assessment", "est_time_preliminary_assessment", "est_time_final_assessment", "est_time_jury_questions", "min_project_duration_days", "min_participant_team_size", "min_subcontractors", "max_project_completion_months", "url_code_of_conduct", "url_schedule", "url_flyer", "url_finalists", "url_results")
values
    (2023, 1, '2023-11-01', '2023-11-15', '2023-11-30', '2023-12-15', 30, 2, 3, 6, 'http://example.com/code_of_conduct', 'http://example.com/schedule', 'http://example.com/flyer', 'http://example.com/finalists', 'http://example.com/results'),
    (2024, 2, '2024-10-01', '2024-10-15', '2024-10-30', '2024-11-15', 45, 3, 4, 7, 'http://example.com/code_of_conduct_2024', 'http://example.com/schedule_2024', 'http://example.com/flyer_2024', 'http://example.com/finalists_2024', 'http://example.com/results_2024'),
    (2025, 3, '2025-09-01', '2025-09-15', '2025-09-30', '2025-10-15', 60, 4, 5, 8, 'http://example.com/code_of_conduct_2025', 'http://example.com/schedule_2025', 'http://example.com/flyer_2025', 'http://example.com/finalists_2025', 'http://example.com/results_2025');

insert into edition.jury_member_contest ("contest_id", "jury_member_id")
values
    (1, 1),
    (1, 2),
    (2, 1),
    (2, 3),
    (3, 2),
    (3, 4);

insert into edition.awards_representative_contest ("contest_id", "awards_representative_id")
values
    (1, 1),
    (1, 2),
    (2, 1),
    (2, 3),
    (3, 2),
    (3, 4);

insert into edition.pem_criterion ("contest_id", "name", "description", "area", "criteria", "subcriteria")
values
    (1, 'Ludzie i cele', 'obszar ten uznaje się za fundament doskonałości w zarządzaniu projektami. Właściwi ludzie, kierowani i wspierani przez doskonałych liderów oraz dzielący z nimi wspólną wizję sukcesu, mają kluczowe znaczenie dla ciągłego doskonalenia w projekcie i pozwalają mu wykroczyć poza granice znanych standardów.', 'Fundamenty doskonałości', 'Fundamenty doskonałości', 'Ludzie i cele'),
    (1, 'Procesy i zasoby', 'obszar ten obejmuje praktyki niezbędne do wzmacniania doskonałości przez czytelne i skuteczne procesy oraz adekwatne zasoby wykorzystywane w sprawny i zrównoważony sposób. Stanowi on także podstawę do zabezpieczenia rezultatów wynikających z innowacyjności, czyniąc je solidnym punktem wyjścia dla kolejnych udoskonaleń.', 'Wzmacnianie doskonałości', 'Wzmacnianie doskonałości', 'Procesy i zasoby'),
    (1, 'Rezultaty projektu', 'podejście do zarządzania projektem może zostać uznane za doskonałe, wyłącznie jeżeli prowadzi do wyróżniających się, zrównoważonych rezultatów dla wszystkich kluczowych interesariuszy. Obszar ten stanowi dopełnienie pierwszych dwóch, zapewniając niezbędny dowód osiągnięcia doskonałych rezultatów zgodnych z oczekiwaniami interesariuszy projektu.', 'Dowód doskonałości', 'Dowód doskonałości', 'Rezultaty projektu'),
    (2, 'Ludzie i cele', 'obszar ten uznaje się za fundament doskonałości w zarządzaniu projektami. Właściwi ludzie, kierowani i wspierani przez doskonałych liderów oraz dzielący z nimi wspólną wizję sukcesu, mają kluczowe znaczenie dla ciągłego doskonalenia w projekcie i pozwalają mu wykroczyć poza granice znanych standardów.', 'Fundamenty doskonałości', 'Fundamenty doskonałości', 'Ludzie i cele'),
    (2, 'Procesy i zasoby', 'obszar ten obejmuje praktyki niezbędne do wzmacniania doskonałości przez czytelne i skuteczne procesy oraz adekwatne zasoby wykorzystywane w sprawny i zrównoważony sposób. Stanowi on także podstawę do zabezpieczenia rezultatów wynikających z innowacyjności, czyniąc je solidnym punktem wyjścia dla kolejnych udoskonaleń.', 'Wzmacnianie doskonałości', 'Wzmacnianie doskonałości', 'Procesy i zasoby'),
    (2, 'Rezultaty projektu', 'podejście do zarządzania projektem może zostać uznane za doskonałe, wyłącznie jeżeli prowadzi do wyróżniających się, zrównoważonych rezultatów dla wszystkich kluczowych interesariuszy. Obszar ten stanowi dopełnienie pierwszych dwóch, zapewniając niezbędny dowód osiągnięcia doskonałych rezultatów zgodnych z oczekiwaniami interesariuszy projektu.', 'Dowód doskonałości', 'Dowód doskonałości', 'Rezultaty projektu'),
    (3, 'Ludzie i cele', 'obszar ten uznaje się za fundament doskonałości w zarządzaniu projektami. Właściwi ludzie, kierowani i wspierani przez doskonałych liderów oraz dzielący z nimi wspólną wizję sukcesu, mają kluczowe znaczenie dla ciągłego doskonalenia w projekcie i pozwalają mu wykroczyć poza granice znanych standardów.', 'Fundamenty doskonałości', 'Fundamenty doskonałości', 'Ludzie i cele'),
    (3, 'Procesy i zasoby', 'obszar ten obejmuje praktyki niezbędne do wzmacniania doskonałości przez czytelne i skuteczne procesy oraz adekwatne zasoby wykorzystywane w sprawny i zrównoważony sposób. Stanowi on także podstawę do zabezpieczenia rezultatów wynikających z innowacyjności, czyniąc je solidnym punktem wyjścia dla kolejnych udoskonaleń.', 'Wzmacnianie doskonałości', 'Wzmacnianie doskonałości', 'Procesy i zasoby'),
    (3, 'Rezultaty projektu', 'podejście do zarządzania projektem może zostać uznane za doskonałe, wyłącznie jeżeli prowadzi do wyróżniających się, zrównoważonych rezultatów dla wszystkich kluczowych interesariuszy. Obszar ten stanowi dopełnienie pierwszych dwóch, zapewniając niezbędny dowód osiągnięcia doskonałych rezultatów zgodnych z oczekiwaniami interesariuszy projektu.', 'Dowód doskonałości', 'Dowód doskonałości', 'Rezultaty projektu');

insert into project.submission (
    "contest_id",
    "name",
    "duration_days",
    "team_size",
    "finish_date",
    "status",
    "budget",
    "description"
) values
    (1, 'EcoGlobe', 45, 5, '2023-12-31', 'submitted', 50000.00, 'Rewolucyjny projekt mający na celu stworzenie zrównoważonego świata poprzez innowacyjne technologie i praktyki.'),
    (1, 'AquaBotics', 60, 3, '2023-11-15', 'submitted', 35000.00, 'Podwodna przygoda odkrywająca tajemnice głębokich mórz za pomocą nowoczesnych technologii robotycznych.'),
    (1, 'SolarScape', 30, 4, '2024-02-28', 'submitted', 75000.00, 'Podróż ku jaśniejszej przyszłości dzięki rozwiązaniom związanym z energią słoneczną.'),
    (2, 'UrbanHarmony', 55, 2, '2023-12-31', 'submitted', 40000.00, 'Tworzenie harmonijnych przestrzeni miejskich dla lepszej jakości życia.'),
    (2, 'BioWonders', 40, 6, '2023-11-15', 'submitted', 60000.00, 'Odkrywanie cudów natury poprzez biotechnologię i ochronę środowiska.'),
    (2, 'SpaceVoyage', 25, 8, '2024-02-28', 'submitted', 90000.00, 'Wyruszenie w podróż, aby odkryć tajemnice kosmosu zewnętrznego.'),
    (3, 'FutureFarms', 35, 4, '2023-12-31', 'submitted', 55000.00, 'Pionierskie praktyki zrównoważonej uprawy dla bardziej zielonej przyszłości.'),
    (3, 'TechNest', 50, 3, '2023-11-15', 'submitted', 45000.00, 'Budowa wysokotechnologicznego gniazda dla innowacji i kreatywności.'),
    (3, 'ArtisanCraft', 65, 7, '2024-02-28', 'submitted', 80000.00, 'Odrodzenie sztuki rzemiosła dla współczesnego świata.');

insert into project.application_report (
    "submission_id",
    "is_draft",
    "report_submission_date"
) values
    (1, false, '2023-10-15'),
    (2, false, '2023-10-20'),
    (3, false, '2023-10-25'),
    (4, false, '2023-10-15'),
    (5, false, '2023-10-20'),
    (6, false, '2023-10-25'),
    (7, false, '2023-10-15'),
    (8, false, '2023-10-20'),
    (9, false, '2023-10-25');

insert into project.assessor_submission (
    "assessor_id",
    "submission_id",
    "is_leading"
) values
    (1, 1, true),
    (2, 1, false),
    (3, 2, true),
    (4, 2, false),
    (5, 3, true),
    (6, 3, false),
    (7, 4, true),
    (8, 4, false),
    (9, 5, true),
    (10, 5, false),
    (1, 6, true),
    (2, 6, false),
    (3, 7, true),
    (4, 7, false),
    (5, 8, true),
    (6, 8, false),
    (7, 9, true),
    (8, 9, false),
    (9, 9, false);

insert into project.applicant_submission (
    "applicant_id",
    "submission_id"
) values
    (1, 1),
    (2, 2),
    (3, 3),
    (4, 4),
    (5, 5),
    (6, 6),
    (7, 7),
    (8, 8),
    (9, 9);

insert into project.rating (
    "submission_id",
    "assessor_id",
    "is_draft",
    "type"
) values
    (1, 1, true, 'final'),
    (1, 2, false,'initial'),
    (1, 2, false,'individual'),
    (1, 1, false,'individual'),
    (2, 3, true, 'final'),
    (2, 4, false,'initial'),
    (2, 4, false,'individual'),
    (2, 3, false,'individual'),
    (3, 5, true, 'final'),
    (3, 6, false,'initial'),
    (3, 6, false,'individual'),
    (3, 5, false,'individual'),
    (4, 7, true, 'final'),
    (4, 8, false,'initial'),
    (4, 8, false,'individual'),
    (4, 7, false,'individual'),
    (5, 9, true, 'final'),
    (5, 10, false,'initial'),
    (5, 10, false,'individual'),
    (5, 9, false,'individual'),
    (6, 1, true, 'final'),
    (6, 2, false,'initial'),
    (6, 2, false,'individual'),
    (6, 1, false,'individual'),
    (7, 3, true, 'final'),
    (7, 4, false,'initial'),
    (7, 4, false,'individual'),
    (7, 3, false,'individual'),
    (8, 5, true, 'final'),
    (8, 6, false,'initial'),
    (8, 6, false,'individual'),
    (8, 5, false,'individual'),
    (9, 7, true, 'final'),
    (9, 8, false,'initial'),
    (9, 8, false,'individual'),
    (9, 7, false,'individual');

insert into project.partial_rating (
    "rating_id",
    "criterion_id",
    "points",
    "justification",
    "modified_by_id"
) values
    (1, 1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 1),
    (2, 1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 1),
    (3, 1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 1),
    (4, 1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 1),
    (5, 1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 1),
    (6, 1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 1),
    (7, 1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 1),
    (8, 1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 1),
    (9, 1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 2),
    (10,1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 2),
    (11,1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 2),
    (12,1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 2),
    (13,1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 2),
    (14,1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 1),
    (15,1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 1),
    (16,1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 1),
    (17,1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 1),
    (18,1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 1),
    (19,1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 1),
    (20,1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 1),
    (21,1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 1),
    (22,1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 2),
    (23,1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 1),
    (24,1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 1),
    (25,1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 1),
    (26,1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 1),
    (27,1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 1),
    (28,1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 1),
    (29,1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 1),
    (30,1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 1),
    (31,1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 1),
    (32,1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 1),
    (33,1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 1),
    (34,1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 1),
    (35,1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 1),
    (36,1, 5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 1);

insert into project.ipma_expert_submission (
    "ipma_expert_id",
    "submission_id"
) values
    (1, 1),
    (2, 2),
    (3, 3),
    (4, 4),
    (5, 5),
    (6, 6),
    (7, 7),
    (8, 8),
    (9, 9);
