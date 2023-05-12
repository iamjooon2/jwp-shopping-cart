package cart.dto.request;

import cart.domain.Product;
import cart.dto.ProductDto;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CreateProductRequest {

    @NotEmpty(message = "상품 이름이 입력되지 않았습니다.")
    private String name;
    @NotNull(message = "가격이 입력되지 않았습니다.")
    private Integer price;
    @NotEmpty(message = "상품 이미지 주소가 입력되지 않았습니다.")
    private String image;

    public CreateProductRequest() {
    }

    public CreateProductRequest(final String name, final Integer price, final String image) {
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public ProductDto toDto() {
        return new ProductDto(name, price, image);
    }

    public Product toProduct() {
        return new Product(name, price, image);
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }
}
