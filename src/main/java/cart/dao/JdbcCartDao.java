package cart.dao;

import cart.domain.cart.Cart;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcCartDao implements CartDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;
    private final RowMapper<Cart> cartRowMapper = (resultSet, rowNum) ->
            new Cart(
                    resultSet.getLong("id"),
                    resultSet.getLong("member_id"),
                    resultSet.getLong("product_id")
            );

    public JdbcCartDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long insert(final Cart cart) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(cart);
        return insertActor.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public List<Cart> findAllByMemberId(final long memberId) {
        final String sql = "SELECT * FROM cart WHERE member_id = ?";
        return jdbcTemplate.query(sql, cartRowMapper, memberId);
    }
}
