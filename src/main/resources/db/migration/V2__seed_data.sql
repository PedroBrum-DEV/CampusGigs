-- senha do admin: admin123
-- senha dos alunos: aluno123
INSERT INTO users (name, username, password, role) VALUES
                                                       ('Admin CampusGigs', 'admin', '$2b$10$POVhdtyBIsRpsuddPBykZeqQZ/V3XITCm1.Dv7ApmktgQJ3RVKF2G', 'ADMIN'),
                                                       ('Ana Souza',         'ana.souza',   '$2b$10$R.zsFSqucOqSwE.zI/syq.G9KP.hrgkoXOGTkq7RVSjswYVTFcS2m', 'STUDENT'),
                                                       ('Bruno Lima',        'bruno.lima',  '$2b$10$R.zsFSqucOqSwE.zI/syq.G9KP.hrgkoXOGTkq7RVSjswYVTFcS2m', 'STUDENT'),
                                                       ('Carla Mendes',      'carla.mendes','$2b$10$R.zsFSqucOqSwE.zI/syq.G9KP.hrgkoXOGTkq7RVSjswYVTFcS2m', 'STUDENT');

INSERT INTO gigs (title, description, category, price, status, owner_id) VALUES
                                                                             ('Criar logo para projeto de faculdade', 'Preciso de um logo simples para o TCC do grupo de sistemas.', 'Design', 80.00, 'OPEN', 2),
                                                                             ('Aula particular de Java', 'Reforço de POO e Spring Boot para prova da próxima semana.', 'Aulas', 50.00, 'OPEN', 3),
                                                                             ('Digitação de monografia', 'Digitar e formatar 40 páginas de monografia em ABNT.', 'Redação', 120.00, 'OPEN', 2),
                                                                             ('Revisão de código de TCC', 'Revisar um projeto Spring Boot antes da apresentação final.', 'Programação', 100.00, 'CLOSED', 3);

INSERT INTO applications (gig_id, applicant_id, message, status) VALUES
    (2, 4, 'Tenho experiência com Java e Spring, posso ajudar amanhã à tarde.', 'PENDING');