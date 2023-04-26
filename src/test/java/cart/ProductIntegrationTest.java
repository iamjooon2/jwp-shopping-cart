package cart;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import cart.dto.RequestCreateProductDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void getProducts() {
        final var result = given()
                .log().all()
                .when()
                .get("/admin")
                .then()
                .extract();

        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 상품을_등록하면_상품목록에_상품이_추가된다() {
        // given
        given()
                .log().all().contentType(ContentType.JSON)
                .body(new RequestCreateProductDto("치킨", 10000, "치킨 사진"))
                .when()
                .post("/admin/product")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());

        // when
        final Response response = given().log().all()
                .accept(MediaType.TEXT_HTML_VALUE)
                .when()
                .get("/")
                .then()
                .log().all()
                .extract().response();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().asString()).contains("치킨");
    }

    /*To do
     * 첫 화면에서 관리자 버튼을 누르면, 관리자 목록이 잘 보이는지
     *
     * 상품 등록 후, 상품 목록 & 관리자 목록에서 등록한 상품이 보이는지
    * 게시글 삭제 후, 상품 목록 & 관리자 목록에서 사라지는지
    * 게시글 수정 후, 상품 목록 & 관리자 목록에서 게시글이 수정됐는지
    * */

    @Test
    void 메인화면에서_관리자를_클릭하면_Admin페이지가_반환된다() {
        final Response response = given().log().all()
                .accept(MediaType.TEXT_HTML_VALUE)
                .when()
                .get("/admin")
                .then()
                .log().all()
                .extract().response();

        assertThat(response.getBody().asString()).contains("ID", "이름", "가격", "이미지", "Actions", "상품 추가");
    }
}
