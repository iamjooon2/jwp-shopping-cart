package cart.service;

import cart.dao.CartDao;
import cart.dao.entity.CartEntity;
import cart.dao.entity.MemberEntity;
import cart.dao.entity.ProductEntity;
import cart.dto.AuthDto;
import cart.dto.response.CartResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {

    private final MemberService memberService;
    private final ProductService productService;
    private final CartDao cartDao;

    @Autowired
    public CartService(final MemberService memberService, final ProductService productService, final CartDao cartDao) {
        this.memberService = memberService;
        this.productService = productService;
        this.cartDao = cartDao;
    }

    @Transactional
    public int insert(final Long productId, final AuthDto authDto) {
        final MemberEntity memberEntity = memberService.findMember(authDto);
        final Optional<CartEntity> cartEntity = cartDao.findCart(productId, memberEntity.getId());
        if (cartEntity.isPresent()) {
            throw new IllegalArgumentException("이미 카트에 담겨진 제품입니다.");
        }
        return cartDao.insert(productId, memberEntity.getId());
    }

    @Transactional
    public int delete(final Long productId, final AuthDto authDto) {
        final MemberEntity memberEntity = memberService.findMember(authDto);
        final Optional<CartEntity> cartEntity = cartDao.findCart(productId, memberEntity.getId());
        if (cartEntity.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 데이터입니다.");
        }
        return cartDao.delete(cartEntity.get());
    }

    @Transactional(readOnly = true)
    public List<CartResponse> selectCart(final AuthDto authDto) {
        final MemberEntity memberEntity = memberService.findMember(authDto);
        final Long memberId = memberEntity.getId();
        final Optional<List<CartEntity>> cartEntitiesOptional = cartDao.findAllByMemberId(memberId);
        if (cartEntitiesOptional.isEmpty()) {
            return new ArrayList<>();
        }
        final List<ProductEntity> productEntities = cartEntitiesOptional.get().stream()
                .map(cartEntity -> productService.findById(cartEntity.getProductId()))
                .collect(Collectors.toUnmodifiableList());
        return productEntities.stream()
                .map(CartResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }
}
