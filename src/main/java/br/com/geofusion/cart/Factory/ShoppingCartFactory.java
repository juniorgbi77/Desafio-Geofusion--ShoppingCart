package br.com.geofusion.cart.Factory;

import br.com.geofusion.cart.Model.ShoppingCart;
import br.com.geofusion.cart.Service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Classe responsável pela criação e recuperação dos carrinhos de compras.
 */
@Service
public class ShoppingCartFactory {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * Cria e retorna um novo carrinho de compras para o cliente passado como parâmetro.
     *
     * Caso já exista um carrinho de compras para o cliente passado como parâmetro, este carrinho deverá ser retornado.
     *
     * @param clientId
     * @return ShoppingCart
     */
    public ShoppingCart create(String clientId) {
        ShoppingCart ExistingCart = shoppingCartService.FindByClientId(clientId);
        if(ExistingCart == null){
            ShoppingCart newShoppingCart = new ShoppingCart(clientId);
            return shoppingCartService.create(newShoppingCart);
        }
        return ExistingCart;
    }

    /**
     * Retorna o valor do ticket médio no momento da chamada ao método.
     * O valor do ticket médio é a soma do valor total de todos os carrinhos de compra dividido
     * pela quantidade de carrinhos de compra.
     * O valor retornado deverá ser arredondado com duas casas decimais, seguindo a regra:
     * 0-4 deve ser arredondado para baixo e 5-9 deve ser arredondado para cima.
     *
     * @return BigDecimal
     */
    public BigDecimal getAverageTicketAmount() {
        List<ShoppingCart> carts = shoppingCartService.readAll();
        if(carts == null){
            return BigDecimal.ZERO;
        }
        BigDecimal result = carts
                .stream()
                .reduce(BigDecimal.ZERO, (sum, ob) -> sum.add(ob.getAmount()), BigDecimal::add);

        return result.divide(new BigDecimal(carts.size()), RoundingMode.HALF_UP);
    }

    /**
     * Invalida um carrinho de compras quando o cliente faz um checkout ou sua sessão expirar.
     * Deve ser efetuada a remoção do carrinho do cliente passado como parâmetro da listagem de carrinhos de compras.
     *
     * @param clientId
     * @return Retorna um boolean, tendo o valor true caso o cliente passado como parämetro tenha um carrinho de compras e
     * e false caso o cliente não possua um carrinho.
     */
    public boolean invalidate(String clientId) {
        ShoppingCart ExistingCart = shoppingCartService.FindByClientId(clientId);
        if(ExistingCart == null){
            return false;
        }
        shoppingCartService.delete(ExistingCart.getId());
        return true;
    }
}
