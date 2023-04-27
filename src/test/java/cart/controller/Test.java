//package cart.controller;
//
//import static io.restassured.RestAssured.given;
//
//import cart.dto.RequestUpdateProductDto;
//import io.restassured.http.ContentType;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.test.context.jdbc.Sql;
//
//@SuppressWarnings("NonAsciiCharacters")
//@JdbcTest
//@Sql({"data.sql"})
//public class Test {
//
//    @Mock
//    private JdbcTemplate jdbcTemplate;
//
//    @ParameterizedTest
//    @ValueSource(ints = {Integer.MAX_VALUE, Integer.MIN_VALUE})
//    void 유효한_가격_범위를_넘긴_상품은_등록할_수_없다(final Integer price) {
//
//        Mockito.when(jdbcTemplate.update("INSERT INTO PRODUCT (name, price, image) VALUES (\"치킨\", 1_000, \"치킨 사진\")", Object.class))
//                        .thenReturn(1);
//
//        Long insertedId = Long.valueOf(jdbcTemplate.update("INSERT INTO PRODUCT (name, price, image) VALUES (\"치킨\", 1_000, \"치킨 사진\")",
//                Object.class));
//
//        given()
//                .log().all().contentType(ContentType.JSON)
//                .body(new RequestUpdateProductDto(insertedId,"피자", 10_000, "피자 사진"))
//                .when()
//                .put("/admin/product")
//                .then()
//                .log().all()
//                .statusCode(HttpStatus.OK.value());
//    }
//
//
//}
