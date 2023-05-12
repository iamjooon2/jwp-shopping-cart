package cart.dao;

import cart.domain.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;

    public ProductDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> findAll() {
        final String sql = "SELECT * FROM PRODUCT";
        return jdbcTemplate.query(sql, getProductRowMapper());
    }

    private RowMapper<Product> getProductRowMapper() {
        return (resultSet, rowNum) -> new Product(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getInt("price"),
                resultSet.getString("image")
        );
    }

    public Long insert(final Product product) {
        final String sql = "INSERT INTO PRODUCT (name, price, image) VALUES (?, ?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
                final PreparedStatement preparedStatement = con.prepareStatement(
                        sql, new String[]{"ID"}
                );
                preparedStatement.setString(1, product.getName());
                preparedStatement.setInt(2, product.getPrice());
                preparedStatement.setString(3, product.getImage());
                return preparedStatement;
            }
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public int update(final Long id, final Product product) {
        final String sql = "UPDATE PRODUCT SET name = ?, price = ?, image = ? WHERE id = ?";
        return jdbcTemplate.update(
                sql,
                product.getName(),
                product.getPrice(),
                product.getImage(),
                id
        );
    }

    public int delete(final Long id) {
        final String sql = "DELETE FROM product WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public Optional<Product> findById(final Long id) {
        final String sql = "SELECT * from product where id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, getProductRowMapper(), id));
        } catch (EmptyResultDataAccessException error) {
            return Optional.empty();
        }
    }
}
