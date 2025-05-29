insert into categorias (categoria_mae_id, nome, id) values (null, 'tecnologia', default);
insert into categorias (categoria_mae_id, nome, id) values (1, 'celular', default);
insert into usuarios (data_hora_registro, login, senha, id) values ('2025-04-10 23:24:49.113066', 'adriano@email.com', '$2a$10$oQF7VT5lkB40zMnqyo8PIuyFA7cGxK.noPF.KM.I/brcanhRkVDkS', default);
insert into produtos ( id, nome, descricao, quantidade, valor, categoria_id, usuario_id, data_hora_registro ) values ( default, 'celular', 'uma descricao', 1, 100.00, 2, 1, '2025-05-03 16:54:18.603218' );
insert into caracteristicas (id, produto_id, descricao, nome) VALUES (default, 1, 'Motorola', 'marca'), (default, 1, 'preto', 'cor'), (default, 1, '300g', 'peso');

insert into usuarios (data_hora_registro, login, senha, id) values ( '2025-05-03 17:15:15.628818', 'maria@email.com', '$2a$10$rM/55sib0.ricZeWor9og.0Thd1ZaCAqxP72kYDlw..dHrtrS3bfq', default);
insert into produtos ( id, nome, descricao, quantidade, valor, categoria_id, usuario_id, data_hora_registro ) values ( default, 'smartwatch', 'Um smartwatch moderno com diversas funcionalidades.', 5, 250.00, 2, 2, '2025-05-03 17:20:00.000000' );
insert into caracteristicas (id, produto_id, descricao, nome) values (default, 2, 'Samsung', 'marca'), (default, 2, 'prata', 'cor'), (default, 2, '150g', 'peso');

-- Inserção do produto 3 e seus relacionamentos
INSERT INTO produtos ( id, nome, descricao, quantidade, valor, categoria_id, usuario_id, data_hora_registro ) VALUES ( default, 'celular 2', 'uma descricao', 1, 100.00, 2, 1, '2025-05-18 14:37:58.071730' );
INSERT INTO caracteristicas (id, produto_id, descricao, nome) VALUES (default, 3, '300g', 'peso'), (default, 3, 'preto', 'cor'), (default, 3, 'Motorola', 'marca');
INSERT INTO imagens_produto (id, produto_id, nome, url_imagem) VALUES (default, 3, '_sunflower.jpg', '//bucketname/_sunflower.jpg'), (default, 3, '_download.png', '//bucketname/_download.png');
INSERT INTO opinioes_produto (nota, id, produto_id, usuario_id, descricao, titulo) VALUES (5, default, 3, 1, 'Voluntarius agnitio claustrum adnuo.', 'opniao 3141 Crona Glens'), (3, default, 3, 2, 'Sit truculenter turba incidunt.', 'opniao 9956 Oceane Camp');
INSERT INTO perguntas_produto (data_hora_registro, id, produto_id, usuario_id, titulo) VALUES ('2025-05-18 14:38:22.529804', default, 3, 2, 'pergunta appono turbo curriculum');

INSERT INTO produtos ( id, nome, descricao, quantidade, valor, categoria_id, usuario_id, data_hora_registro ) VALUES ( default, 'Smartwatch Xtreme', 'Um smartwatch completo com GPS e monitor de batimentos cardíacos.', 99999999, 299.99, 2, 1, '2025-05-27 10:39:00.000000' );
INSERT INTO caracteristicas (id, produto_id, descricao, nome) VALUES (default, 4, '50g', 'peso'), (default, 4, 'azul marinho', 'cor'), (default, 4, 'FitTech', 'marca'), (default, 4, 'Tela AMOLED 1.4 polegadas', 'tela');
INSERT INTO imagens_produto (id, produto_id, nome, url_imagem) VALUES (default, 4, 'smartwatch_azul.jpg', '//bucketname/smartwatch_azul.jpg'), (default, 4, 'smartwatch_tela.png', '//bucketname/smartwatch_tela.png');
INSERT INTO opinioes_produto (nota, id, produto_id, usuario_id, descricao, titulo) VALUES (4, default, 4, 2, 'Excelente para atividades físicas, bateria dura bastante.', 'Ótimo custo-benefício'), (5, default, 4, 1, 'Muito bonito e funcional, recomendo!', 'Perfeito para o dia a dia');
INSERT INTO perguntas_produto (data_hora_registro, id, produto_id, usuario_id, titulo) VALUES ('2025-05-27 10:39:00.000000', default, 4, 2, 'É resistente à água?');
