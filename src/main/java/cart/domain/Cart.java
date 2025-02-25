package cart.domain;

public class Cart {

    private final Long id;
    private final Long productId;
    private final Long memberId;

    public Cart(final Long productId, final Long memberId) {
        this(null, productId, memberId);
    }

    public Cart(final Long id, final Long productId, final Long memberId) {
        this.id = id;
        this.productId = productId;
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
