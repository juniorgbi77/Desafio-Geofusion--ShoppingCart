package br.com.geofusion.cart.Model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * Classe que representa o carrinho de compras de um cliente.
 */

@Data
@Entity
@NoArgsConstructor
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String clientId;


    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Item> items;

    public ShoppingCart(String clientId) {
        super();
        this.clientId = clientId;
    }

    /**
     * Permite a adição de um novo item no carrinho de compras.
     * <p>
     * Caso o item já exista no carrinho para este mesmo produto, as seguintes regras deverão ser seguidas:
     * - A quantidade do item deverá ser a soma da quantidade atual com a quantidade passada como parâmetro.
     * - Se o valor unitário informado for diferente do valor unitário atual do item, o novo valor unitário do item deverá ser
     * o passado como parâmetro.
     * <p>
     * Devem ser lançadas subclasses de RuntimeException caso não seja possível adicionar o item ao carrinho de compras.
     *
     * @param product
     * @param unitPrice
     * @param quantity
     */
    public void addItem(Product product, BigDecimal unitPrice, int quantity) {
        try {
            Item ExistingItem = this.getItemByProduct(product);

            if (ExistingItem == null) {
                Item newItem = new Item(product, unitPrice, quantity);
                items.add(newItem);
                return;
            }

            ExistingItem.setQuantity(ExistingItem.getQuantity() + quantity);
            ExistingItem.setUnitPrice(unitPrice);
            int index = items.indexOf(ExistingItem);
            items.set(index, ExistingItem);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Permite a remoção do item que representa este produto do carrinho de compras.
     *
     * @param product
     * @return Retorna um boolean, tendo o valor true caso o produto exista no carrinho de compras e false
     * caso o produto não exista no carrinho.
     */
    public boolean removeItem(Product product) {
        Item ExistingItem = this.getItemByProduct(product);
        int index = items.indexOf(ExistingItem);
        return this.removeItem(index);
    }

    /**
     * Permite a remoção do item de acordo com a posição.
     * Essa posição deve ser determinada pela ordem de inclusão do produto na
     * coleção, em que zero representa o primeiro item.
     *
     * @param itemIndex
     * @return Retorna um boolean, tendo o valor true caso o produto exista no carrinho de compras e false
     * caso o produto não exista no carrinho.
     */
    public boolean removeItem(int itemIndex) {
        if (itemIndex == -1) {
            return false;
        }
        items.remove(itemIndex);
        return true;
    }

    /**
     * Retorna o valor total do carrinho de compras, que deve ser a soma dos valores totais
     * de todos os itens que compõem o carrinho.
     *
     * @return BigDecimal
     */
    public BigDecimal getAmount() {
        if (this.items == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal result = this.items
                .stream()
                .reduce(BigDecimal.ZERO, (sum, ob) -> sum.add(ob.getAmount()), BigDecimal::add);
        return result;
    }

    /**
     * Retorna a lista de itens do carrinho de compras.
     *
     * @return items
     */
    public Collection<Item> getItems() {
        return this.items;
    }

    /**
     * @param product
     * @return Item
     */
    private Item getItemByProduct(Product product) {
        Item item = items.stream()
                .filter(obj -> product.equals(obj.getProduct()))
                .findAny()
                .orElse(null);
        return item;
    }

    /**
     * @param itemId
     * @return Item
     */
    private Item getItemByID(Long itemId) {
        Item item = items.stream()
                .filter(obj -> itemId.equals(obj.getId()))
                .findAny()
                .orElse(null);
        return item;
    }

    /**
     *
     * @param itemId
     * @param unitPrice
     * @return
     */
    public boolean changeValue(Long itemId, BigDecimal unitPrice) {
            Item ExistingItem = this.getItemByID(itemId);

            if (ExistingItem == null) {
                return false;
            }
            ExistingItem.setUnitPrice(unitPrice);
            int index = items.indexOf(ExistingItem);
            items.set(index, ExistingItem);
            return true;
    }


}