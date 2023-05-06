package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import cart.domain.Cart;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class CartDaoTest {
    private static final SqlParameterSource PRODUCT_FIXTURE = new MapSqlParameterSource()
            .addValue("name", "치킨")
            .addValue("price", 1_000)
            .addValue("image", "치킨 사진");
    private static final SqlParameterSource MEMBER_FIXTURE = new MapSqlParameterSource()
            .addValue("email", "gavi@woowahan.com")
            .addValue("password", "1234");

    private CartDao cartDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert simpleProductInsert;
    private SimpleJdbcInsert simpleMemberInsert;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        cartDao = new CartDao(jdbcTemplate);
        simpleProductInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("PRODUCT")
                .usingGeneratedKeyColumns("id");
        simpleMemberInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("MEMBER")
                .usingGeneratedKeyColumns("id");
    }

    @Test
    void 카트를_추가할_수_있다() {
        // given
        final Long productId = simpleProductInsert.executeAndReturnKey(PRODUCT_FIXTURE).longValue();
        final Long memberId = simpleMemberInsert.executeAndReturnKey(MEMBER_FIXTURE).longValue();

        // when
        final int insertedId = cartDao.insert(productId, memberId);

        // then
        assertThat(insertedId).isOne();
    }

    @Test
    void 카트를_삭제할_수_있다() {
        // given
        final Long productId = simpleProductInsert.executeAndReturnKey(PRODUCT_FIXTURE).longValue();
        final Long memberId = simpleMemberInsert.executeAndReturnKey(MEMBER_FIXTURE).longValue();

        // when
        cartDao.delete(productId, memberId);

        // then
        assertThat(cartDao.findAllByMemberId(memberId)).isEqualTo(Optional.ofNullable(List.of()));
    }

    @Test
    void id_들을_가진_데이터를_찾는다() {
        // given
        final Long productId = simpleProductInsert.executeAndReturnKey(PRODUCT_FIXTURE).longValue();
        final Long memberId = simpleMemberInsert.executeAndReturnKey(MEMBER_FIXTURE).longValue();
        cartDao.insert(productId, memberId);

        // when
        final Optional<Cart> cartOptional = cartDao.find(productId, memberId);

        // then
        assertSoftly(softly -> {
            Cart cart = cartOptional.get();
            softly.assertThat(cart.getMemberId()).isEqualTo(memberId);
            softly.assertThat(cart.getProductId()).isEqualTo(productId);
        });
    }

    @Test
    void memberId를_가진_데이터를_조회한다() {
        // given
        final Long memberId = simpleMemberInsert.executeAndReturnKey(MEMBER_FIXTURE).longValue();
        final Long productId = simpleProductInsert.executeAndReturnKey(PRODUCT_FIXTURE).longValue();

        // when
        cartDao.insert(productId, memberId);
        cartDao.insert(productId, memberId);
        cartDao.insert(productId, memberId);

        // then
        assertSoftly(softly -> {
            final Optional<List<Cart>> cartsOptional = cartDao.findAllByMemberId(memberId);
            List<Cart> carts = cartsOptional.get();
            assertThat(carts).hasSize(3);
        });
    }
}
