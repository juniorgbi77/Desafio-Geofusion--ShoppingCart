
# Geofusion Backend Teste


## Como instalar

- rodar comando `mvn clean install` no root do projeto
- rodar comando `mvn spring-boot:run` para iniciar o Tomcat


##Rotas
-No diretorio root do projeto tem uma Collection do Insomnia com todas as rotas.


####1.Exibição de produtos
* 1.1 - GET = **BASE_URL**/product -> Exibe todos os produtos.
* 1.2 - GET = **BASE_URL**/product/**product_code** -> Exibe um produto especifico.
* 1.3 - POST = **BASE_URL**/product *body*{"description":"teste"} -> Insere um produto.
* 1.4 - PUT = **BASE_URL**/product/**product_code** *body*{"description":"teste"} -> Altera um produto.
* 1.5 - DELETE = **BASE_URL**/product/**product_code** -> Apaga um produto.

Inferi que as proximas rotas traram o **Item** com **produto**, pois a regra de negocio demonstra que um produto deve ser adicionado ao carrinho como item.

####2. Alteração de preços do produto.
* 2.1 - POST = **BASE_URL**/cart/**client_id**/**item_id** *body*{"unitPrice":"1.5"} -> Altera o valor do item no carrinho


####3. Criação de carrinhos.
* 3.1 - POST = **BASE_URL**/cart/**client_id** -> Insere um Carrinho.

####4. Adição de produto no carrinho
* 4.1 - POST = **BASE_URL**/cart/**client_id**/**product_code** *body*{"unitPrice":"1.1","quantity":1} -> Insere um Item no carrinho.

####5. Remoção de produto do carrinho
* 5.1 - DELETE = **BASE_URL**/cart/**client_id**/**product_code**  -> Apaga um Item do carrinho.

####6. Exibição de carrinhos (Com total de itens e média de valores)
* 6.1 - GET = **BASE_URL**/cart -> Exibe todos os carrinhos.
