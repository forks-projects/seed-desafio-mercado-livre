# seed-desafio-mercado-livre

## TODO
- encriptar senha do usuário (A senha deve ser guardada usando algum algoritmo de hash da sua escolha.)

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
