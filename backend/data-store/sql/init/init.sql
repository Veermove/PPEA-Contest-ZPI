truncate table person.base cascade;
truncate table person.awards_representative cascade;
truncate table person.jury_member cascade;
truncate table person.ipma_expert cascade;
truncate table person.applicant cascade;
truncate table person.ipma_expert cascade;
truncate table person.assessor cascade;

alter sequence person.applicant_applicant_id_seq                         restart with 1;
alter sequence person.assessor_assessor_id_seq                           restart with 1;
alter sequence person.awards_representative_awards_representative_id_seq restart with 1;
alter sequence person.base_person_id_seq                                 restart with 1;
alter sequence person.ipma_expert_ipma_expert_id_seq                     restart with 1;
alter sequence person.jury_member_jury_member_id_seq                     restart with 1;

truncate table edition.contest cascade;
truncate table edition.jury_member_contest cascade;
truncate table edition.awards_representative_contest cascade;
truncate table edition.pem_criterion cascade;

alter sequence edition.awards_representative_contest_awards_representative_contest_seq restart with 1;
alter sequence edition.contest_contest_id_seq                                          restart with 1;
alter sequence edition.jury_member_contest_jury_member_contest_id_seq                  restart with 1;
alter sequence edition.pem_criterion_pem_criterion_id_seq                              restart with 1;

truncate table project.applicant_submission cascade;
truncate table project.application_report cascade;
truncate table project.assessor_submission cascade;
truncate table project.assessors_answer cascade;
truncate table project.ipma_expert_submission cascade;
truncate table project.jury_question cascade;
truncate table project.partial_rating cascade;
truncate table project.rating cascade;
truncate table project.submission cascade;

alter sequence project.applicant_submission_applicant_submission_id_seq     restart with 1;
alter sequence project.application_report_application_report_id_seq         restart with 1;
alter sequence project.assessor_submission_assessor_submission_id_seq       restart with 1;
alter sequence project.assessors_answer_assessors_answer_id_seq             restart with 1;
alter sequence project.ipma_expert_submission_ipma_expert_submission_id_seq restart with 1;
alter sequence project.jury_question_jury_question_id_seq                   restart with 1;
alter sequence project.partial_rating_partial_rating_id_seq                 restart with 1;
alter sequence project.rating_rating_id_seq                                 restart with 1;
alter sequence project.submission_submission_id_seq                         restart with 1;

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

insert into edition.contest ("year", "master_jury_id", "est_time_individual_assessment", "est_time_initial_assessment", "est_time_final_assessment", "est_time_jury_questions", "min_project_duration_days", "min_participant_team_size", "min_subcontractors", "max_project_completion_months", "url_code_of_conduct", "url_schedule", "url_flyer", "url_finalists", "url_results")
values
    (2023, 1, '2023-12-03', '2023-11-15', '2023-11-30', '2023-12-15', 30, 2, 3, 6, 'http://example.com/code_of_conduct', 'http://example.com/schedule', 'http://example.com/flyer', 'http://example.com/finalists', 'http://example.com/results'),
    (2022, 2, '2022-10-01', '2022-10-15', '2022-10-30', '2022-11-15', 45, 3, 4, 7, 'http://example.com/code_of_conduct_2022', 'http://example.com/schedule_2022', 'http://example.com/flyer_2022', 'http://example.com/finalists_2022', 'http://example.com/results_2022'),
    (2021, 3, '2021-09-01', '2021-09-15', '2021-09-30', '2021-10-15', 60, 4, 5, 8, 'http://example.com/code_of_conduct_2021', 'http://example.com/schedule_2021', 'http://example.com/flyer_2021', 'http://example.com/finalists_2021', 'http://example.com/results_2021');

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
    (1, 'Role models for excellence', 'Leaders communicate and live up to their values (i.e. they `walk the talk`), follow ethical standards and act as role models. They ensure that structures and norms are in place that enable project team members to work effectively and efficiently. Leaders build and strengthen a culture of excellence and continuous improvement both within and beyond the project. They observe and carry out the project excellence concepts in a credible way and stimulate others to do the same.', 'A', '1', 'a'),
    (2, 'Role models for excellence', 'Leaders communicate and live up to their values (i.e. they `walk the talk`), follow ethical standards and act as role models. They ensure that structures and norms are in place that enable project team members to work effectively and efficiently. Leaders build and strengthen a culture of excellence and continuous improvement both within and beyond the project. They observe and carry out the project excellence concepts in a credible way and stimulate others to do the same.', 'A', '1', 'a'),
    (3, 'Role models for excellence', 'Leaders communicate and live up to their values (i.e. they `walk the talk`), follow ethical standards and act as role models. They ensure that structures and norms are in place that enable project team members to work effectively and efficiently. Leaders build and strengthen a culture of excellence and continuous improvement both within and beyond the project. They observe and carry out the project excellence concepts in a credible way and stimulate others to do the same.', 'A', '1', 'a'),
    (1, 'Care for project stakeholders', 'Leaders care for internal and external stakeholders. They are proactively engaged in balancing the needs and interests of different parties, supporting their development and striving to provide a good working environment. They ensure that the impact of the project on its environment is recognised and actively managed to ensure sustainability.', 'A', '1', 'b'),
    (2, 'Care for project stakeholders', 'Leaders care for internal and external stakeholders. They are proactively engaged in balancing the needs and interests of different parties, supporting their development and striving to provide a good working environment. They ensure that the impact of the project on its environment is recognised and actively managed to ensure sustainability.', 'A', '1', 'b'),
    (3, 'Care for project stakeholders', 'Leaders care for internal and external stakeholders. They are proactively engaged in balancing the needs and interests of different parties, supporting their development and striving to provide a good working environment. They ensure that the impact of the project on its environment is recognised and actively managed to ensure sustainability.', 'A', '1', 'b'),
    (1, 'Orientation towards project objectives and adaptability to change', 'Project leaders take responsibility for short- and long-term project results (i.e. benefits), ensuring successful project delivery. They ensure sustainability of project results including, if applicable, the consequences for future generations. They listen to stakeholders, observe the project environment and create space for dialogue about different approaches and innovation.', 'A', '1', 'c'),
    (2, 'Orientation towards project objectives and adaptability to change', 'Project leaders take responsibility for short- and long-term project results (i.e. benefits), ensuring successful project delivery. They ensure sustainability of project results including, if applicable, the consequences for future generations. They listen to stakeholders, observe the project environment and create space for dialogue about different approaches and innovation.', 'A', '1', 'c'),
    (3, 'Orientation towards project objectives and adaptability to change', 'Project leaders take responsibility for short- and long-term project results (i.e. benefits), ensuring successful project delivery. They ensure sustainability of project results including, if applicable, the consequences for future generations. They listen to stakeholders, observe the project environment and create space for dialogue about different approaches and innovation.', 'A', '1', 'c'),
    (1, 'Orientation towards project objective', 'Stakeholders, their needs, expectations and requirements are clearly identified and actively managed.', 'A', '2', 'a'),
    (2, 'Orientation towards project objective', 'Stakeholders, their needs, expectations and requirements are clearly identified and actively managed.', 'A', '2', 'a'),
    (3, 'Orientation towards project objective', 'Stakeholders, their needs, expectations and requirements are clearly identified and actively managed.', 'A', '2', 'a'),
    (1, 'Development and realisation of project objectives', 'Project objectives are developed from a comprehensive analysis of relevant information. Competing interests are dealt with. Stakeholders are familiar with the relevant objectives and understand their role in achieving them. Objectives are regularly reviewed, and adapted to changes in stakeholders` expectations and requirements when necessary', 'A', '2', 'b'),
    (2, 'Development and realisation of project objectives', 'Project objectives are developed from a comprehensive analysis of relevant information. Competing interests are dealt with. Stakeholders are familiar with the relevant objectives and understand their role in achieving them. Objectives are regularly reviewed, and adapted to changes in stakeholders` expectations and requirements when necessary', 'A', '2', 'b'),
    (3, 'Development and realisation of project objectives', 'Project objectives are developed from a comprehensive analysis of relevant information. Competing interests are dealt with. Stakeholders are familiar with the relevant objectives and understand their role in achieving them. Objectives are regularly reviewed, and adapted to changes in stakeholders` expectations and requirements when necessary', 'A', '2', 'b'),
    (1, 'Development and realisation of project strategy', 'In excellent projects, project teams develop and implement an overall project strategy based on an evaluation of the project objectives, context and enviro- nment (including its position in the programme and/or project portfolio). The project strategy, together with the project objectives, enables the project team to focus on what is most important for the project to be successful', 'A', '2', 'c'),
    (2, 'Development and realisation of project strategy', 'In excellent projects, project teams develop and implement an overall project strategy based on an evaluation of the project objectives, context and enviro- nment (including its position in the programme and/or project portfolio). The project strategy, together with the project objectives, enables the project team to focus on what is most important for the project to be successful', 'A', '2', 'c'),
    (3, 'Development and realisation of project strategy', 'In excellent projects, project teams develop and implement an overall project strategy based on an evaluation of the project objectives, context and enviro- nment (including its position in the programme and/or project portfolio). The project strategy, together with the project objectives, enables the project team to focus on what is most important for the project to be successful', 'A', '2', 'c'),
    (1, 'Identification and development of competences', 'Leaders recognise the competences required for project success in all three domains: people, practice and perspective. They are aware of the capabilities, limitations and potential of their own organisation and those of their partners and suppliers. The organisations` own staff and third parties are taken into account when developing the project setup. Their competences are developed as required and when needed.', 'A', '3', 'a'),
    (2, 'Identification and development of competences', 'Leaders recognise the competences required for project success in all three domains: people, practice and perspective. They are aware of the capabilities, limitations and potential of their own organisation and those of their partners and suppliers. The organisations` own staff and third parties are taken into account when developing the project setup. Their competences are developed as required and when needed.', 'A', '3', 'a'),
    (3, 'Identification and development of competences', 'Leaders recognise the competences required for project success in all three domains: people, practice and perspective. They are aware of the capabilities, limitations and potential of their own organisation and those of their partners and suppliers. The organisations` own staff and third parties are taken into account when developing the project setup. Their competences are developed as required and when needed.', 'A', '3', 'a'),
    (1, 'Recognition of achievements and empowerment', 'People are rewarded, recognised and taken care of by leaders. Project team members, partners and suppliers are enabled to realise their full potential when realising project objectives. They are involved in the ongoing processes and are empowered to take appropriate actions.', 'A', '3', 'b'),
    (2, 'Recognition of achievements and empowerment', 'People are rewarded, recognised and taken care of by leaders. Project team members, partners and suppliers are enabled to realise their full potential when realising project objectives. They are involved in the ongoing processes and are empowered to take appropriate actions.', 'A', '3', 'b'),
    (3, 'Recognition of achievements and empowerment', 'People are rewarded, recognised and taken care of by leaders. Project team members, partners and suppliers are enabled to realise their full potential when realising project objectives. They are involved in the ongoing processes and are empowered to take appropriate actions.', 'A', '3', 'b'),
    (1, 'Collaboration and communication', 'The project organisation and processes are designed in ways that enable project team members, partners and suppliers to communicate and cooperate efficiently both within the project and beyond', 'A', '3', 'c'),
    (2, 'Collaboration and communication', 'The project organisation and processes are designed in ways that enable project team members, partners and suppliers to communicate and cooperate efficiently both within the project and beyond', 'A', '3', 'c'),
    (3, 'Collaboration and communication', 'The project organisation and processes are designed in ways that enable project team members, partners and suppliers to communicate and cooperate efficiently both within the project and beyond', 'A', '3', 'c'),
    (1, 'Project Management Processes & Resources', 'Teams on excellent projects identify the key project management processes and related resources necessary for project success in cooperation with stakeholders. Key methods, tools and project management processes are selected, developed and optimised to achieve the project objectives in the most effective and efficient way. This is done based on a good understanding of the project needs and organisational capabilities.', 'B', '1', NULL),
    (2, 'Project Management Processes & Resources', 'Teams on excellent projects identify the key project management processes and related resources necessary for project success in cooperation with stakeholders. Key methods, tools and project management processes are selected, developed and optimised to achieve the project objectives in the most effective and efficient way. This is done based on a good understanding of the project needs and organisational capabilities.', 'B', '1', NULL),
    (3, 'Project Management Processes & Resources', 'Teams on excellent projects identify the key project management processes and related resources necessary for project success in cooperation with stakeholders. Key methods, tools and project management processes are selected, developed and optimised to achieve the project objectives in the most effective and efficient way. This is done based on a good understanding of the project needs and organisational capabilities.', 'B', '1', NULL),
    (1, 'Management of Other Key Processes & Resources', 'Teams on excellent projects identify other key project delivery and support processes and related resources necessary for project success (e.g. product design, engineering, maintenance, handover and acceptance, logistics, safety and security) in cooperation with stakeholders. These methods, tools and processes are selected, developed and optimised to achieve the project objectives in the most effective and efficient way. This is achieved based on a good understanding of organisational capabilities.', 'B', '2', NULL),
    (2, 'Management of Other Key Processes & Resources', 'Teams on excellent projects identify other key project delivery and support processes and related resources necessary for project success (e.g. product design, engineering, maintenance, handover and acceptance, logistics, safety and security) in cooperation with stakeholders. These methods, tools and processes are selected, developed and optimised to achieve the project objectives in the most effective and efficient way. This is achieved based on a good understanding of organisational capabilities.', 'B', '2', NULL),
    (3, 'Management of Other Key Processes & Resources', 'Teams on excellent projects identify other key project delivery and support processes and related resources necessary for project success (e.g. product design, engineering, maintenance, handover and acceptance, logistics, safety and security) in cooperation with stakeholders. These methods, tools and processes are selected, developed and optimised to achieve the project objectives in the most effective and efficient way. This is achieved based on a good understanding of organisational capabilities.', 'B', '2', NULL),
    (1, 'Customer perception', 'Customer representatives consistently express their satisfaction throughout the entire project lifecycle.', 'C', '1', 'a'),
    (2, 'Customer perception', 'Customer representatives consistently express their satisfaction throughout the entire project lifecycle.', 'C', '1', 'a'),
    (3, 'Customer perception', 'Customer representatives consistently express their satisfaction throughout the entire project lifecycle.', 'C', '1', 'a'),
    (1, 'Indicators of customer satisfaction', 'In excellent projects, the perception expressed by customer representatives (C.1a.) and relevant observable indicators lead to the same conclusions about the customer satisfaction level.', 'C', '1', 'b'),
    (2, 'Indicators of customer satisfaction', 'In excellent projects, the perception expressed by customer representatives (C.1a.) and relevant observable indicators lead to the same conclusions about the customer satisfaction level.', 'C', '1', 'b'),
    (3, 'Indicators of customer satisfaction', 'In excellent projects, the perception expressed by customer representatives (C.1a.) and relevant observable indicators lead to the same conclusions about the customer satisfaction level.', 'C', '1', 'b'),
    (1, 'Perception of the project team', 'Project team members consistently express their satisfaction throughout the entire project lifecycle.', 'C', '2', 'a'),
    (2, 'Perception of the project team', 'Project team members consistently express their satisfaction throughout the entire project lifecycle.', 'C', '2', 'a'),
    (3, 'Perception of the project team', 'Project team members consistently express their satisfaction throughout the entire project lifecycle.', 'C', '2', 'a'),
    (1, 'Indicators of project team satisfaction', 'In excellent projects the perception expressed by team members(C.2a.) and relevant observable indicators lead to the sameconclusions about the level of project team member satisfaction.', 'C', '2', 'b'),
    (2, 'Indicators of project team satisfaction', 'In excellent projects the perception expressed by team members(C.2a.) and relevant observable indicators lead to the sameconclusions about the level of project team member satisfaction.', 'C', '2', 'b'),
    (3, 'Indicators of project team satisfaction', 'In excellent projects the perception expressed by team members(C.2a.) and relevant observable indicators lead to the sameconclusions about the level of project team member satisfaction.', 'C', '2', 'b'),
    (1, 'Perception of the other stakeholders', 'Stakeholders consistently express their satisfaction throughout the entire project lifecycle. Positive impacts on the environment are measurable. Whenever the project has a considerable impact on the natural environment, the satisfaction of respective stakeholders (e.g. NGOs, local communities and/or authorities) is considered.', 'C', '3', 'a'),
    (2, 'Perception of the other stakeholders', 'Stakeholders consistently express their satisfaction throughout the entire project lifecycle. Positive impacts on the environment are measurable. Whenever the project has a considerable impact on the natural environment, the satisfaction of respective stakeholders (e.g. NGOs, local communities and/or authorities) is considered.', 'C', '3', 'a'),
    (3, 'Perception of the other stakeholders', 'Stakeholders consistently express their satisfaction throughout the entire project lifecycle. Positive impacts on the environment are measurable. Whenever the project has a considerable impact on the natural environment, the satisfaction of respective stakeholders (e.g. NGOs, local communities and/or authorities) is considered.', 'C', '3', 'a'),
    (1, 'Indicators of other stakeholders` satisfaction', 'In excellent projects the perception expressed by stakeholders (C.3a.) and relevant observable indicators lead to the same conclusions about the level of stakeholder satisfaction.', 'C', '3', 'b'),
    (2, 'Indicators of other stakeholders` satisfaction', 'In excellent projects the perception expressed by stakeholders (C.3a.) and relevant observable indicators lead to the same conclusions about the level of stakeholder satisfaction.', 'C', '3', 'b'),
    (3, 'Indicators of other stakeholders` satisfaction', 'In excellent projects the perception expressed by stakeholders (C.3a.) and relevant observable indicators lead to the same conclusions about the level of stakeholder satisfaction.', 'C', '3', 'b'),
    (1, 'Realisation of results as defined in project objectives', 'Excellent projects realise the results as defined in the project objectives (A.2b.)', 'C', '4', 'a'),
    (2, 'Realisation of results as defined in project objectives', 'Excellent projects realise the results as defined in the project objectives (A.2b.)', 'C', '4', 'a'),
    (3, 'Realisation of results as defined in project objectives', 'Excellent projects realise the results as defined in the project objectives (A.2b.)', 'C', '4', 'a'),
    (1, 'Realisation of results beyond project objectives, including impact on environment', 'Excellent project management and leadership often lead to additional results beyond planned project objectives. These include positive impact on their environment (e.g. natural, organisational, business etc.).', 'C', '4', 'b'),
    (2, 'Realisation of results beyond project objectives, including impact on environment', 'Excellent project management and leadership often lead to additional results beyond planned project objectives. These include positive impact on their environment (e.g. natural, organisational, business etc.).', 'C', '4', 'b'),
    (3, 'Realisation of results beyond project objectives, including impact on environment', 'Excellent project management and leadership often lead to additional results beyond planned project objectives. These include positive impact on their environment (e.g. natural, organisational, business etc.).', 'C', '4', 'b'),
    (1, 'Project performance', 'Excellent projects realise their results in an effective and efficient way and minimise their negative impact on the project environment.', 'C', '4', 'c'),
    (2, 'Project performance', 'Excellent projects realise their results in an effective and efficient way and minimise their negative impact on the project environment.', 'C', '4', 'c'),
    (3, 'Project performance', 'Excellent projects realise their results in an effective and efficient way and minimise their negative impact on the project environment.', 'C', '4', 'c');

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
    (1, 'EcoGlobe', 45, 5, '2023-12-31', 'accepted', 50000.00, 'Rewolucyjny projekt mający na celu stworzenie zrównoważonego świata poprzez innowacyjne technologie i praktyki.'),
    (1, 'AquaBotics', 60, 3, '2023-11-15', 'accepted', 35000.00, 'Podwodna przygoda odkrywająca tajemnice głębokich mórz za pomocą nowoczesnych technologii robotycznych.'),
    (1, 'SolarScape', 30, 4, '2022-02-28', 'accepted', 75000.00, 'Podróż ku jaśniejszej przyszłości dzięki rozwiązaniom związanym z energią słoneczną.'),
    (2, 'UrbanHarmony', 55, 2, '2023-12-31', 'accepted', 40000.00, 'Tworzenie harmonijnych przestrzeni miejskich dla lepszej jakości życia.'),
    (2, 'BioWonders', 40, 6, '2023-11-15', 'accepted', 60000.00, 'Odkrywanie cudów natury poprzez biotechnologię i ochronę środowiska.'),
    (2, 'SpaceVoyage', 25, 8, '2022-02-28', 'accepted', 90000.00, 'Wyruszenie w podróż, aby odkryć tajemnice kosmosu zewnętrznego.'),
    (3, 'FutureFarms', 35, 4, '2023-12-31', 'accepted', 55000.00, 'Pionierskie praktyki zrównoważonej uprawy dla bardziej zielonej przyszłości.'),
    (3, 'TechNest', 50, 3, '2023-11-15', 'accepted', 45000.00, 'Budowa wysokotechnologicznego gniazda dla innowacji i kreatywności.'),
    (3, 'ArtisanCraft', 65, 7, '2022-02-28', 'accepted', 80000.00, 'Odrodzenie sztuki rzemiosła dla współczesnego świata.');

insert into project.application_report (
    "submission_id",
    "is_draft",
    "report_submission_date",
    "project_goals",
    "organisation_structure",
    "division_of_work",
    "project_schedule",
    "attatchments"
) values
    (1, false, '2023-10-15', 'ProjectGoals.pdf', 'OrganisationStructure.pdf', 'DivisionOfWork.pdf', 'ProjectSchedule.pdf', 'Attatchments.pdf'),
    (2, false, '2023-10-20', 'ProjectGoals.pdf', 'OrganisationStructure.pdf', 'DivisionOfWork.pdf', 'ProjectSchedule.pdf', 'Attatchments.pdf'),
    (3, false, '2023-10-25', 'ProjectGoals.pdf', 'OrganisationStructure.pdf', 'DivisionOfWork.pdf', 'ProjectSchedule.pdf', 'Attatchments.pdf'),
    (4, false, '2023-10-15', 'ProjectGoals.pdf', 'OrganisationStructure.pdf', 'DivisionOfWork.pdf', 'ProjectSchedule.pdf', 'Attatchments.pdf'),
    (5, false, '2023-10-20', 'ProjectGoals.pdf', 'OrganisationStructure.pdf', 'DivisionOfWork.pdf', 'ProjectSchedule.pdf', 'Attatchments.pdf'),
    (6, false, '2023-10-25', 'ProjectGoals.pdf', 'OrganisationStructure.pdf', 'DivisionOfWork.pdf', 'ProjectSchedule.pdf', 'Attatchments.pdf'),
    (7, false, '2023-10-15', 'ProjectGoals.pdf', 'OrganisationStructure.pdf', 'DivisionOfWork.pdf', 'ProjectSchedule.pdf', 'Attatchments.pdf'),
    (8, false, '2023-10-20', 'ProjectGoals.pdf', 'OrganisationStructure.pdf', 'DivisionOfWork.pdf', 'ProjectSchedule.pdf', 'Attatchments.pdf'),
    (9, false, '2023-10-25', 'ProjectGoals.pdf', 'OrganisationStructure.pdf', 'DivisionOfWork.pdf', 'ProjectSchedule.pdf', 'Attatchments.pdf');

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

-- submissions 1-3 are from active contest
insert into project.rating( "submission_id", "assessor_id", "is_draft", "type") values (2, 3, true, 'individual');
insert into project.rating( "submission_id", "assessor_id", "is_draft", "type") values (2, 4, false, 'individual');
insert into project.partial_rating ( "rating_id", "criterion_id", "points", "justification", "modified_by_id" ) values (1, 2,  5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 2);
insert into project.partial_rating ( "rating_id", "criterion_id", "points", "justification", "modified_by_id" ) values (2, 2,  5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 2);

update project.submission set status = 'accepted_individual' where submission_id = 2;
-- submission 4
insert into project.rating ( "submission_id", "assessor_id", "is_draft", "type") values (4, 7, false,'individual');
insert into project.rating ( "submission_id", "assessor_id", "is_draft", "type") values (4, 8, false,'individual');
insert into project.partial_rating ( "rating_id", "criterion_id", "points", "justification", "modified_by_id" ) values (3,  2,  5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 2);
insert into project.partial_rating ( "rating_id", "criterion_id", "points", "justification", "modified_by_id" ) values (4,  2,  5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 2);

update project.submission set status = 'accepted_individual' where submission_id = 4;
insert into project.rating ( "submission_id", "assessor_id", "is_draft", "type") values (4, 8, false,'initial');
insert into project.partial_rating ( "rating_id", "criterion_id", "points", "justification", "modified_by_id" ) values (5,  2,  5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 2);

update project.submission set status = 'accepted_initial' where submission_id = 4;
insert into project.rating ( "submission_id", "assessor_id", "is_draft", "type") values (4, 8, false,'final');
insert into project.partial_rating ( "rating_id", "criterion_id", "points", "justification", "modified_by_id" ) values (6,  2,  5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 2);

-- submission 5
insert into project.rating ( "submission_id", "assessor_id", "is_draft", "type") values (5, 10, false,'individual');
insert into project.rating ( "submission_id", "assessor_id", "is_draft", "type") values (5, 9, false,'individual');
insert into project.partial_rating ( "rating_id", "criterion_id", "points", "justification", "modified_by_id" ) values (7,  2,  5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 2);
insert into project.partial_rating ( "rating_id", "criterion_id", "points", "justification", "modified_by_id" ) values (8,  2,  5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 2);

update project.submission set status = 'accepted_individual' where submission_id = 5;
insert into project.rating ( "submission_id", "assessor_id", "is_draft", "type") values (5, 9, false,'initial');
insert into project.partial_rating ( "rating_id", "criterion_id", "points", "justification", "modified_by_id" ) values (9,  2,  5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 2);

update project.submission set status = 'accepted_initial' where submission_id = 5;
insert into project.rating ( "submission_id", "assessor_id", "is_draft", "type") values (5, 10, false,'final');
insert into project.partial_rating ( "rating_id", "criterion_id", "points", "justification", "modified_by_id" ) values (10,  2,  5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 2);

-- submission 6
insert into project.rating ( "submission_id", "assessor_id", "is_draft", "type") values (6, 1, false,'individual');
insert into project.rating ( "submission_id", "assessor_id", "is_draft", "type") values (6, 2, false,'individual');
insert into project.partial_rating ( "rating_id", "criterion_id", "points", "justification", "modified_by_id" ) values (11,  2,  5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 2);
insert into project.partial_rating ( "rating_id", "criterion_id", "points", "justification", "modified_by_id" ) values (12,  2,  5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 2);

update project.submission set status = 'accepted_individual' where submission_id = 6;
insert into project.rating ( "submission_id", "assessor_id", "is_draft", "type") values (6, 2, false,'initial');
insert into project.partial_rating ( "rating_id", "criterion_id", "points", "justification", "modified_by_id" ) values (13,  2,  5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 2);

update project.submission set status = 'accepted_initial' where submission_id = 6;
insert into project.rating ( "submission_id", "assessor_id", "is_draft", "type") values (6, 1, false,'final');
insert into project.partial_rating ( "rating_id", "criterion_id", "points", "justification", "modified_by_id" ) values (14,  2,  5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 2);

-- submission 7
insert into project.rating ( "submission_id", "assessor_id", "is_draft", "type") values (7, 3, false,'individual');
insert into project.rating ( "submission_id", "assessor_id", "is_draft", "type") values (7, 4, false,'individual');
insert into project.partial_rating ( "rating_id", "criterion_id", "points", "justification", "modified_by_id" ) values (15,  2,  5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 2);
insert into project.partial_rating ( "rating_id", "criterion_id", "points", "justification", "modified_by_id" ) values (16,  2,  5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 2);

update project.submission set status = 'accepted_individual' where submission_id = 7;
insert into project.rating ( "submission_id", "assessor_id", "is_draft", "type") values (7, 4, false,'initial');
insert into project.partial_rating ( "rating_id", "criterion_id", "points", "justification", "modified_by_id" ) values (17,  2,  5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 2);

update project.submission set status = 'accepted_initial' where submission_id = 7;
insert into project.rating ( "submission_id", "assessor_id", "is_draft", "type") values (7, 3, false,'final');
insert into project.partial_rating ( "rating_id", "criterion_id", "points", "justification", "modified_by_id" ) values (18,  2,  5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 2);

-- submission 8
insert into project.rating ( "submission_id", "assessor_id", "is_draft", "type") values (8, 5, false,'individual');
insert into project.rating ( "submission_id", "assessor_id", "is_draft", "type") values (8, 6, false,'individual');
insert into project.partial_rating ( "rating_id", "criterion_id", "points", "justification", "modified_by_id" ) values (19,  2,  5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 2);
insert into project.partial_rating ( "rating_id", "criterion_id", "points", "justification", "modified_by_id" ) values (20,  2,  5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 2);

update project.submission set status = 'accepted_individual' where submission_id = 8;
insert into project.rating ( "submission_id", "assessor_id", "is_draft", "type") values (8, 6, false,'initial');
insert into project.partial_rating ( "rating_id", "criterion_id", "points", "justification", "modified_by_id" ) values (21,  2,  5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 2);

update project.submission set status = 'accepted_initial' where submission_id = 8;
insert into project.rating ( "submission_id", "assessor_id", "is_draft", "type") values (8, 5, false,'final');
insert into project.partial_rating ( "rating_id", "criterion_id", "points", "justification", "modified_by_id" ) values (22,  2,  5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 2);

-- submission 9
insert into project.rating ( "submission_id", "assessor_id", "is_draft", "type") values (9, 5, false,'individual');
insert into project.rating ( "submission_id", "assessor_id", "is_draft", "type") values (9, 6, false,'individual');
insert into project.partial_rating ( "rating_id", "criterion_id", "points", "justification", "modified_by_id" ) values (23,  2,  5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 2);
insert into project.partial_rating ( "rating_id", "criterion_id", "points", "justification", "modified_by_id" ) values (24,  2,  5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 2);

update project.submission set status = 'accepted_individual' where submission_id = 9;
insert into project.rating ( "submission_id", "assessor_id", "is_draft", "type") values (9, 6, false,'initial');
insert into project.partial_rating ( "rating_id", "criterion_id", "points", "justification", "modified_by_id" ) values (25,  2,  5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 2);

update project.submission set status = 'accepted_initial' where submission_id = 9;
insert into project.rating ( "submission_id", "assessor_id", "is_draft", "type") values (9, 5, false,'final');
insert into project.partial_rating ( "rating_id", "criterion_id", "points", "justification", "modified_by_id" ) values (26,  2,  5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl. Sed vitae nisl euismod, aliquam nunc vitae, aliquam nisl.', 2);

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



insert into project.jury_question (
    "submission_id",
    "criterion_id",
    "question",
    "is_draft"
) values
    (1, 1, 'Czy projekt jest zgodny z celami konkursu?', false),
    (1, 4, 'Czy budżet został przekroczony?', false),
    (2, 1, 'Czy projekt jest zgodny z celami konkursu?', false),
    (2, 4, 'Czy budżet został przekroczony?', false),
    (3, 1, 'Czy projekt jest zgodny z celami konkursu?', false),
    (3, 4, 'Czy budżet został przekroczony?', false),
    (4, 2, 'Czy projekt jest zgodny z celami konkursu?', false),
    (4, 5, 'Czy budżet został przekroczony?', false),
    (5, 2, 'Czy projekt jest zgodny z celami konkursu?', false),
    (5, 5, 'Czy budżet został przekroczony?', false),
    (6, 2, 'Czy projekt jest zgodny z celami konkursu?', false),
    (6, 5, 'Czy budżet został przekroczony?', false),
    (7, 3, 'Czy projekt jest zgodny z celami konkursu?', false),
    (7, 6, 'Czy budżet został przekroczony?', false),
    (8, 3, 'Czy projekt jest zgodny z celami konkursu?', false),
    (8, 6, 'Czy budżet został przekroczony?', false),
    (9, 3, 'Czy projekt jest zgodny z celami konkursu?', false),
    (9, 6, 'Czy budżet został przekroczony?', false);

insert into project.assessors_answer (
    "jury_question_id",
    "assessor_id",
    "answer",
    "files"
) values
    (1, 1, 'Tak, brak niezgodności.', 'Attachment1.pdf,Attachment2.pdf'),
    (2, 2, 'Nie, nie został przekroczony.', ''),
    (3, 3, 'Tak, brak niezgodności.', 'Attachment1.pdf,Attachment2.pdf'),
    (4, 4, 'Nie, nie został przekroczony.', 'Attachment3.pdf'),
    (5, 5, 'Tak, brak niezgodności.', 'Attachment1.pdf,Attachment2.pdf'),
    (6, 6, 'Nie, nie został przekroczony.', 'Attachment3.pdf'),
    (7, 7, 'Tak, brak niezgodności.', 'Attachment1.pdf,Attachment2.pdf'),
    (8, 8, 'Nie, nie został przekroczony.', 'Attachment3.pdf'),
    (9, 9, 'Tak, brak niezgodności.', 'Attachment1.pdf,Attachment2.pdf'),
    (10, 10, 'Nie, nie został przekroczony.', 'Attachment3.pdf'),
    (11, 1, 'Tak, brak niezgodności.', 'Attachment1.pdf,Attachment2.pdf'),
    (12, 2, 'Nie, nie został przekroczony.', 'Attachment3.pdf'),
    (13, 3, 'Tak, brak niezgodności.', 'Attachment1.pdf,Attachment2.pdf'),
    (14, 4, 'Nie, nie został przekroczony.', 'Attachment3.pdf'),
    (15, 5, 'Tak, brak niezgodności.', 'Attachment1.pdf,Attachment2.pdf'),
    (16, 6, 'Nie, nie został przekroczony.', 'Attachment3.pdf'),
    (17, 5, 'Tak, brak niezgodności.', 'Attachment1.pdf,Attachment2.pdf'),
    (18, 7, 'Nie, nie został przekroczony.', 'Attachment3.pdf');
