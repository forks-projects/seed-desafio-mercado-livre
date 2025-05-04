insert into categorias (categoria_mae_id, nome, id) values (null, 'tecnologia', default);
insert into categorias (categoria_mae_id, nome, id) values (1, 'celular', default);
insert into usuarios (data_hora_registro, login, senha, id) values ('2025-04-10 23:24:49.113066', 'adriano@email.com', '$2a$10$oQF7VT5lkB40zMnqyo8PIuyFA7cGxK.noPF.KM.I/brcanhRkVDkS', default);
insert into produtos ( id, nome, descricao, quantidade, valor, categoria_id, usuario_id, data_hora_registro ) values ( default, 'celular', 'uma descricao', 1, 100.00, 2, 1, '2025-05-03 16:54:18.603218' );
insert into caracteristicas (id, produto_id, descricao, nome) VALUES (1, 1, 'Motorola', 'marca'), (2, 1, 'preto', 'cor'), (3, 1, '300g', 'peso');

insert into usuarios (data_hora_registro, login, senha, id) values ( '2025-05-03 17:15:15.628818', 'maria@email.com', '$2a$10$rM/55sib0.ricZeWor9og.0Thd1ZaCAqxP72kYDlw..dHrtrS3bfq', default);
insert into produtos ( id, nome, descricao, quantidade, valor, categoria_id, usuario_id, data_hora_registro ) values ( default, 'smartwatch', 'Um smartwatch moderno com diversas funcionalidades.', 5, 250.00, 2, 2, '2025-05-03 17:20:00.000000' );
insert into caracteristicas (id, produto_id, descricao, nome) values (4, 2, 'Samsung', 'marca'), (5, 2, 'prata', 'cor'), (6, 2, '150g', 'peso');