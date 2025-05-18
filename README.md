# seed-desafio-mercado-livre
Atividade prática do [treinamento Deveficiente](https://deveficiente.com/o).

```bash
mvn clean verify
```

> Para consultar a cobertura de testes acesso o arquivo `target/site/jacoco/index.html` no seu navegador.

## Cadastro de Novo Usuário

### Necessidades
Precisamos saber o instante do cadastro, login e senha.

### Restrições
* O instante não pode ser nulo e não pode ser no futuro.
* O login não pode ser em branco ou nulo.
* O login precisa ter o formato de um email.
* A senha não pode ser em branco ou nula.
* A senha precisa ter no mínimo 6 caracteres.
* A senha deve ser guardada usando algum algoritmo de hash da sua escolha.

### Resultado Esperado
* O usuário precisa estar criado no sistema.
* O cliente que fez a requisição precisa saber que o usuário foi criado. Apenas um retorno com status 200 é suficiente.

### Sobre a Utilização do Material de Suporte
Se você já está no projeto do nosso Mercado Livre, isso indica que você praticou bastante no projeto da Casa do Código. A ideia é que você utilize menos o material de suporte a partir de agora. Tente sempre basear-se nas suas próprias ideias e realmente invista energia para desenvolver a funcionalidade.

Neste momento, a sugestão de divisão de responsabilidade que estamos trabalhando deve estar um pouco mais clara. Você possui uma métrica muito poderosa para focar seu código em fazê-lo funcionar e, posteriormente, se necessário, dividir as responsabilidades de uma forma que facilite o entendimento por outras pessoas.

Caso sinta que precisa de suporte, utilize o material de suporte de maneira progressiva. Lembre-se que também temos nosso canal no Discord, onde você pode pedir ajuda.

### Informações de Suporte Geral
* **Será que você fez um código parecido com esse exemplo [aqui](link-para-exemplo)?**
    * Se a resposta para o ponto 1 for sim, recomendo novamente este material [aqui](link-para-arquitetura-design) sobre arquitetura x design. Também acho que será interessante você olhar a minha implementação logo de cara, apenas para ter uma ideia do design que estou propondo.
* **Controllers 100% coesos** para lembrar nossa ideia de ter controllers que utilizam todos os atributos.
* **Como você fez para receber os dados da requisição?** Você aproveitou a facilidade do framework e recebeu sua entidade (objeto que faz parte do domínio) diretamente no método mapeado para um endereço? Dê uma olhada neste pilar [aqui](link-para-pilar).
* **Dado que você separou os dados que chegam da request do objeto de domínio, como você fará para converter dessa entrada para o domínio?** Sugiro olhar um pouco sobre nossa ideia de Form Value Objects.
* Muitos dos problemas de uma aplicação vêm do fato de ela trabalhar com objetos em estado inválido. O ponto mais crítico em relação a isso é justamente quando os dados vêm de outra fonte, por exemplo, um cliente externo. É por isso que temos o seguinte pilar: **quanto mais externa é a borda, mais proteção nós temos**. Confira uma explicação sobre ele [aqui](link-para-explicacao-1) e depois [aqui](link-para-explicacao-2).
* **Email e senha são informações obrigatórias.** Como você lidou com isso? Informação natural e obrigatória entra pelo construtor.
* Deixamos pistas que facilitam o uso do código onde não conseguimos resolver com compilação. Muitas vezes recebemos `String`, `ints` que possuem significados (ex: um email). Se você não pode garantir a validação do formato em compilação, que tal deixar uma dica para a outra pessoa?
* Ainda sobre as dicas, você recebeu a senha do usuário como `String` no construtor? Se sim, como o ponto do código que chama aquele construtor sabe que aquela `String` precisa ser codificada ou não codificada? Você codificou a senha dentro do construtor? E se o outro ponto de código também tiver codificado a senha, haverá uma dupla codificação? Como você pode deixar uma dica ou aumentar a restrição em tempo de compilação?
* Utilize um Insomnia ou qualquer outra forma para verificar o endpoint.
* Pegue cada uma das classes que você criou e realize a contagem da carga intrínseca. Esse é o viés de design que estamos trabalhando. Precisamos nos habituar a fazer isso para que se torne algo automático em nossa vida.
* Faça a análise de quais pontos do seu código necessitam de testes, dada a régua que estamos utilizando. Lembre-se, estamos buscando por pontos do código onde existam condicionais e/ou branches.
* **Como Alberto faria esse código?** Parte 2 com o código resolvendo a questão da senha codificada [aqui](link-para-codigo-alberto-parte2).
* **Quais foram os testes escritos por Alberto?** [Link para testes de Alberto]

### Informações de Suporte para a Combinação Java/Kotlin + Spring
* Para receber os dados da request como JSON, temos a annotation `@RequestBody`.
* Usamos a annotation `@Valid` para pedir que os dados da request sejam validados.
* Para realizar as validações padrões, existe a Bean Validation.
* Como criar um `@RestControllerAdvice` para customizar o JSON de saída com erros de validação [link-para-restcontrolleradvice].
* Como externalizar as mensagens de erro no arquivo de configuração [link-para-externalizar-mensagens].

## Cadastro de categorias
### necessidades
No mercado livre você pode criar hierarquias de categorias livres.  
Ex: Tecnologia → Celulares → Smartphones → Android, Ios etc.  
Perceba que o sistema precisa ser flexível o suficiente para que essas sequências sejam criadas.

- Toda categoria tem um nome
- A categoria pode ter uma categoria mãe

### restrições
- O nome da categoria é obrigatório
- O nome da categoria precisa ser único

### resultado esperado
- Categoria criada e status 200 retornado pelo endpoint.
- Caso existam erros de validação, o endpoint deve retornar 400 e o JSON dos erros.

### informações de suporte geral
1. Controllers 100% coesos para lembrar você a nossa ideia de ter controllers que utilizam todos os atributos.
2. Como foi que você fez para receber os dados da requisição? Será que aproveitou a facilidade do framework e recebeu a sua entidade (objeto que faz parte do domínio) direto no método mapeado para um endereço?  
   Dá uma olhada nesse pilar aqui.
3. Dado que você separou os dados que chegam da request do objeto de domínio, como vai fazer para converter dessa entrada para o domínio?  
   Sugiro olhar um pouco sobre nossa ideia de Form Value Objects
4. Muitos dos problemas de uma aplicação vêm do fato de ela trabalhar com objetos em estado inválido.  
   O ponto mais crítico é justamente quando os dados vêm de outra fonte.  
   Confira uma explicação sobre isso aqui e depois aqui
5. Apenas o nome da categoria é obrigatório. Como você lidou com isso?  
   Informação natural e obrigatória entra pelo construtor, mas informação natural e não obrigatória não entra.
6. Utilize um Insomnia ou qualquer outra forma para verificar o endpoint.
7. Pegue cada uma das classes que você criou e realize a contagem da carga intrínseca.
8. Como Alberto faria esse código? Aqui ainda não tem a validação do nome único.
9. Como Alberto faria esse código generalizando a validação de valores únicos?

### informações de suporte para a combinação Java/Kotlin + Spring
1. Para receber os dados da request como JSON, usamos a annotation `@RequestBody`.
2. Usamos a annotation `@Valid` para pedir que os dados da request sejam validados.
3. Para realizar as validações padrões, existe a **Bean Validation**.
4. Como criar um `@RestControllerAdvice` para customizar o JSON de saída com erros de validação
5. Como externalizar as mensagens de erro no arquivo de configuração

## TRABALHANDO COM O USUÁRIO LOGADO (VAI IMPACTAR NO RESTO DO CÓDIGO)

### problema

Você precisa configurar um mecanismo de autenticação via token, provavelmente com o Spring Security, para permitir o login.  
Caso queira, é só olhar nesse link [aqui](./).  
Aí tem todo código de segurança necessário para autenticar no Spring Security via token JWT.  
Fique à vontade para entendê-lo e aplicar no projeto.

Caso você esteja utilizando NestJS, ASP.NET Core MVC ou outro framework, é só decidir por qual tecnologia de segurança você vai querer.

### solução que eu faria para focar mais nas features

Em todo trecho de código que precisar do usuário logado, na primeira linha do método do controller que precisa de um usuário logado,  
eu buscaria pelo usuário com um email específico e usaria ele como "referência logada" na aplicação.

Depois, se você quiser, é só habilitar o projeto de segurança, receber o usuário como argumento do método e apagar essa linha...  
Tudo deveria funcionar normalmente.

## Usuário logado cadastra novo produto
### explicação
Aqui a gente vai permitir o cadastro de um produto por usuário logado.

### necessidades
* Tem um nome
* Tem um valor
* Tem quantidade disponível
* Características(cada produto pode ter um conjunto de características diferente)
    * Da uma olhada nos detalhes de produtos diferentes do mercado livre.
* Tem uma descrição que vai ser feita usando Markdown
* Pertence a uma categoria
* Instante de cadastro

### restrições
* Nome é obrigatório
* Valor é obrigatório e maior que zero
* Quantidade é obrigatório e >= 0
* O produto possui pelo menos três características
* Descrição é obrigatória e tem máximo de 1000 caracteres.
* A categoria é obrigatória

### resultado esperado
* Um novo produto criado e status 200 retornado
* Caso dê erro de validação retorne 400 e o json dos erros

### informações de suporte geral
1. Controllers 100% coesos para lembrar você a nossa ideia de ter controllers que utilizam todos os atributos.
2. Como foi que você fez para receber os dados da requisição? Será que aproveitou a facilidade do framework e recebeu a sua entidade(objeto que faz parte do domínio) direto no método mapeado para um endereço? Dá uma olhada nesse pilar aqui.
3. Dado que você separou os dados que chegam da request do objeto de domínio, como vai fazer para converter dessa entrada para o domínio? Sugiro olhar um pouco sobre nossa ideia de Form Value Objects.
4. Muitos dos problemas de uma aplicação vem do fato dela trabalhar com objetos em estado inválido. O ponto mais crítico em relação a isso é justamente quando os dados vêm de outra fonte, por exemplo um cliente externo. É por isso que temos o seguinte pilar: quanto mais externa é a borda mais proteção nós temos. Confira uma explicação sobre ele aqui e depois aqui
5. São muitas informações obrigatórias. Como você lidou com isso? Informação natural e obrigatória entra pelo construtor
6. Ainda na parte das informações obrigatórias tem aquela boa armadilha. Produto tem características, mas cada característica é de um produto. Como que você vai criar um sem o outro? Esse é um desafio muito bom. Resolvemos algo parecido no desafio da Casa do código. Só que tome cuidado, aqui precisa de menos engenharia :).
7. Deixamos pistas que facilitem o uso do código onde não conseguimos resolver com compilação. Muitas vezes recebemos String, ints que possuem significados. Um email por exemplo. Se você não pode garantir a validação do formato em compilação, que tal deixar uma dica para a outra pessoa?
8. Utilize um insomnia ou qualquer outra forma para verificar o endpoint
9. Pegue cada uma das classes que você criou e realize a contagem da carga intrínseca. Esse é o viés de design que estamos trabalhando. Precisamos nos habituar a fazer isso para que se torne algo automático na nossa vida.
10. Caso você já esteja utilizando um projeto de segurança. O framework da sua escolha, integrado com algum outro projeto de segurança, deve permitir que você tenha acesso ao objeto que representa o usuário logado dentro do método do seu controller.
11. Faça a análise de quais pontos do seu código necessitam de testes, dado a régua que estamos utilizando. Lembre estamos buscando por pontos do código onde existam condicionais e/ou branches.
12. Ainda sobre os testes, lembre que muitas vezes você pode substituir código repetitivo de testes por parametrização!
13. Como Alberto faria esse código parte 1 e parte 2 e parte 3
14. Como Alberto testaria o código dele? Confira em três partes: Parte 1, parte 2 e parte 3 :).

### informações de suporte para a combinação Java/Kotlin + Spring
1. Para receber os dados da request como json, temos a annotation `@RequestBody`
2. Usamos a annotation `@Valid` para pedir que os dados da request sejam validados
3. Para realizar as validações padrões existe a Bean Validation
4. Como criar um `@RestControllerAdvice` para customizar o json de saída com erros de validação
5. Como externalizar as mensagens de erro no arquivo de configuração.
6. Use e abuse das annotations da bean validation para indicar as restrições dos parâmetros.
7. Brinque um pouco com a classe `Assert` do Spring para fazer checagens de parâmetro também. As ideias de Design By Contract ajudam demais a aumentar a confiabilidade da aplicação.
8. Para configurar o Spring Security olhe aqui
9. Lembrando que, para receber a referência para o usuário logado no método do controller, você pode usar a annotation `@AuthenticationPrincipal`.

## Adicione uma opinião sobre um produto

### **explicação**
Um usuário logado pode opinar sobre um produto. Claro que o melhor era que isso só pudesse ser feito depois da compra, mas podemos lidar com isso depois.

### **necessidades**
- Tem uma nota que vai de 1 a 5  
- Tem um título. Ex: espetacular, horrível...  
- Tem uma descrição  
- O usuário logado que fez a pergunta  
- O produto para o qual a pergunta foi direcionada  

### **restrições**
- A nota é no mínimo 1 e no máximo 5  
- Título é obrigatório  
- Descrição é obrigatória e tem no máximo 500 caracteres  
- Usuário é obrigatório  
- Produto relacionado é obrigatório  

### **resultado esperado**
- Uma nova opinião é criada e status 200 é retornado  
- Em caso de erro de validação, retorne 400 e o JSON com erros  

### **informações de suporte geral**
1. Controllers 100% coesos para lembrar da nossa ideia de utilizar todos os atributos no controller.  
2. Como você recebe os dados da requisição? Avalie se aproveitou a facilidade do framework.  
3. Separou os dados da request do objeto de domínio? Veja sobre Form Value Objects.  
4. Quanto mais externa é a borda, mais proteção temos.  
5. Toda informação obrigatória entra pelo construtor. Uma opinião é de um usuário para um produto.  
6. Teste o endpoint com Insomnia ou outro método.  
7. Conte a carga intrínseca de cada classe.  
8. Se já utiliza segurança no projeto, o framework deve fornecer acesso ao usuário logado no controller.  
9. Como Alberto faria esse código.  

### **informações de suporte para a combinação Java/Kotlin + Spring**
1. Use `@RequestBody` para receber os dados da request como JSON.  
2. Use `@Valid` para validar a entrada.  
3. Utilize Bean Validation para validações padrão.  
4. Criar um `@RestControllerAdvice` para customizar erros de validação.  
5. Externalize as mensagens de erro no arquivo de configuração.  
6. Use as anotações da Bean Validation para indicar restrições.  
7. Explore a classe **Assert** do Spring (Design by Contract).  
8. Configure o Spring Security conforme necessário.  
9. Use `@AuthenticationPrincipal` para acessar o usuário logado.  
10. Consulte os materiais para retornar status diferentes conforme o caso.  

## Faça uma pergunta ao vendedor(a)
**explicação**

Um usuário logado pode fazer uma pergunta sobre o produto

**necessidades**

- A pergunta tem um título
- Tem instante de criação
- O usuário que fez a pergunta
- O produto relacionado a pergunta
- O vendedor recebe um email com a pergunta nova e o link para a página de visualização do produto(ainda vai existir)
  - o email não precisa ser de verdade. Pode ser apenas um print no console do servidor com o corpo.

**restrições**

- O título é obrigatório
- O produto é obrigatório
- O usuário é obrigatório

**resultado esperado**

- Uma nova pergunta é criada e a lista de perguntas, com a nova pergunta adicionada, é retornada. Status 200
- Em caso de erro de validação, retorne 400 e o json com erros.

**informações de suporte geral**

- Controllers 100% coesos para lembrar você a nossa ideia de ter controllers que utilizam todos os atributos. Lembre que isso não quer dizer que o controller tem um método só, apenas que idealmente todos os métodos devem usar todos os atributos ou pelo menos a grande maioria deles.
- Como foi que você fez para receber os dados da requisição? Será que aproveitou a facilidade do framework e recebeu a sua entidade(objeto que faz parte do domínio) direto no método mapeado para um endereço? Dá uma olhada nesse pilar aqui.
- Dado que você separou os dados que chegam da request do objeto de domínio, como vai fazer para converter dessa entrada para o domínio? Sugiro olhar um pouco sobre nossa ideia de Form Value Objects.
- Muitos dos problemas de uma aplicação vem do fato dela trabalhar com objetos em estado inválido. O ponto mais crítico em relação a isso é justamente quando os dados vêm de outra fonte, por exemplo um cliente externo. É por isso que temos o seguinte pilar: quanto mais externa é a borda mais proteção nós temos. Confira uma explicação sobre ele aqui e depois aqui
- Lembre que toda informação natural e obrigatória entra pelo construtor. Um opinião é de um usuário para um produto. Além das outras informações obrigatórias. 
- Na hora de enviar o email, você precisa construir as informações a partir da pergunta. Pode ser natural querer colocar esse código dentro da classe Pergunta ​​e faz até muito sentido. Deixei uma opinião aqui sobre isso.
- Será que você pensou em listeners para resolver o envio do email? Eu sugiro que regras de negócio devem ser declaradas de maneira explícita na nossa aplicação. 
- Utilize um insomnia ou qualquer outra forma para verificar o endpoint
- Pegue cada uma das classes que você criou e realize a contagem da carga intrínseca. Esse é o viés de design que estamos trabalhando. Precisamos nos habituar a fazer isso para que se torne algo automático na nossa vida.
- Caso você já esteja utilizando um projeto de segurança. O framework da sua escolha, integrado com algum outro projeto de segurança, deve permitir que você tenha acesso ao objeto que representa o usuário logado dentro do método do seu controller. 
- Como Alberto faria esse código e como Alberto faria para enviar o email. 

**informações de suporte para a combinação Java/Kotlin + Spring​**

- Para receber os dados da request como json, temos a annotation @RequestBody
- Usamos a annotation @Valid para pedir que os dados da request sejam validados
- Para realizar as validações padrões existe a Bean Validation
- Como criar um @RestControllerAdvice para customizar o json de saída com erros de validação
- Como externalizar as mensagens de erro no arquivo de configuração.
- Use e abuse das annotations da bean validation para indicar as restrições dos parâmetros. 
- Brinque um pouco com a classe Assert​ ​do Spring para fazer checagens de parâmetro também. As ideias de Design By Contract ajudam demais a aumentar a confiabilidade da aplicação.
- Para configurar o Spring Security olhe aqui
- Lembrando que, para receber a referência para o usuário logado no método do controller, você pode usar a annotation @AuthenticationPrincipal​.
- Para retornar status diferentes, consulte este material aqui
- Uma solução muito utilizada no mercado para este tipo de situação é a criação de listeners. Eu até já escrevi sobre isso muito atrás . Eu sempre recomendo que regras que fazem parte do fluxo de negócio fiquem explícitas, para que sejam mais facilmente encontradas. 

## Escreva o código necessário para montar a página de detalhe

**Explicação:**

O front precisa montar essa página https://www.mercadolivre.com.br/bomba-galo-de-agua-recarregavel-dispenser-bivolt-usb-de-agua-bebedouro-20l-eletro-portateis-de-agua-guerra-luxo-padro-5-estrelas-cor-preto-com-prata/p/MLB38133829#polycard_client=search_best-seller-categories&wid=MLB5249922918&sid=search

Não temos todas as informações, mas já temos bastante coisa. Faça, do jeito que achar melhor o código necessário para que o endpoint retorne as informações para que o front monte a página.

**Informações que já temos como retornar:**

* Links para imagens
* Nome do produto
* Preço do produto
* Características do produto
* Descrição do produto
* Média de notas do produto
* Número total de notas do produto
* Opiniões sobre o produto
* Perguntas para aquele produto

**Informações de suporte geral:**

1.  A prioridade é funcionar. Execute o seu código o mais rápido possível. Perceba que aqui você tem várias informações que devem ser retornadas. Se você escolher fazer tudo antes de executar a primeira requisição, vai demorar bastante ver seu código executando. O que pode dificultar achar os erros na primeira execução.
2.  Como Alberto faria [parte 1], [parte 2] (calculando média), [parte 3] (refatora contagem opiniões).
