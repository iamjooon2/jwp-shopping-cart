package cart.service;

import cart.dao.CartDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.dao.entity.CartEntity;
import cart.dao.entity.MemberEntity;
import cart.dao.entity.ProductEntity;
import cart.dto.AuthDto;
import cart.dto.response.CartResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {

    private final MemberService memberService;
    private final CartDao cartDao;
    private final MemberDao memberDao;
    private final ProductDao productDao;

    @Autowired
    public CartService(final MemberService memberService, final CartDao cartDao, final MemberDao memberDao,
                       final ProductDao productDao) {
        this.memberService = memberService;
        this.cartDao = cartDao;
        this.memberDao = memberDao;
        this.productDao = productDao;
    }

    @Transactional
    public void insert(final Long productId, final AuthDto authDto) {
        final MemberEntity memberEntity = memberService.findMember(authDto);
        final Optional<CartEntity> cartEntity = cartDao.findCart(productId, memberEntity.getId());
        if (!cartEntity.isEmpty()) {
            throw new IllegalArgumentException("이미 카트에 담겨진 제품입니다.");
        }
        cartDao.insert(productId, memberEntity.getId());
    }
}
