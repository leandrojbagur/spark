import com.google.gson.Gson;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import domain.Item;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.math.BigDecimal;

@RunWith(DataProviderRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest {

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @DataProvider
    public static Object[][] validatePostItemDataProvider() {
        return new Object[][] {
                { new Item.ItemBuilder().itemId("ITEM0011")
                        .description("Test item")
                        .title("Nuevo test item")
                        .categoryId("Categoria")
                        .price(BigDecimal.TEN)
                        .build(), 200 },
                { new Item.ItemBuilder().itemId("ITEM0012")
                        .title("Titulo del item")
                        .description("Test item")
                        .build(), 400 },
                { new Item.ItemBuilder().itemId("ITEM0013")
                        .description("Test item")
                        .categoryId("Categoria")
                        .build(), 400 }
        };
    }

    @Test
    @UseDataProvider("validatePostItemDataProvider")
    public void testAValidatePostItem(Item itemToCreate, int expected) {
        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.body(new Gson().toJson(itemToCreate, Item.class));
        Response response = httpRequest.post("/create");
        Assert.assertEquals(expected, response.getStatusCode());
    }

    @DataProvider
    public static Object[][] validateGetItemDataProvider() {
        return new Object[][] {
                { "0", 404 },
                { "1235123", 404 },
                { "ITEM0011", 200 }
        };
    }

    @Test
    @UseDataProvider("validateGetItemDataProvider")
    public void testBValidateGetItem(String itemId, int expected) {
        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest.get("/item/" + itemId);
        Assert.assertEquals(expected, response.getStatusCode());
    }

    @DataProvider
    public static Object[][] validateUpdateItemDataProvider() {
        return new Object[][] {
                { "ITEM001", new Item.ItemBuilder().itemId("ITEM0001")
                        .description("Test item")
                        .title("Nuevo test item")
                        .categoryId("Categoria")
                        .price(BigDecimal.TEN)
                        .build(), 404 },
                { "ITEM0011", new Item.ItemBuilder().itemId("ITEM0013")
                        .description("Test item")
                        .categoryId("Categoria")
                        .build(), 400 },
                { "ITEM0011", new Item.ItemBuilder().itemId("ITEM0011")
                        .description("Test item")
                        .title("Nuevo test item")
                        .categoryId("Categoria")
                        .price(BigDecimal.TEN)
                        .build(), 200 }
        };
    }

    @Test
    @UseDataProvider("validateUpdateItemDataProvider")
    public void testCValidateUpdateItem(String itemId, Item itemToUpdate, int expected) {
        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.body(new Gson().toJson(itemToUpdate, Item.class));
        Response response = httpRequest.put("/update/" + itemId);
        Assert.assertEquals(expected, response.getStatusCode());
    }

    @DataProvider
    public static Object[][] validateDeleteItemDataProvider() {
        return new Object[][] {
                { "0", 404 },
                { "ITEM0011",  200 }
        };
    }

    @Test
    @UseDataProvider("validateDeleteItemDataProvider")
    public void testDValidateDeleteItem(String itemId, int expected) {
        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest.delete("/delete/" + itemId);
        Assert.assertEquals(expected, response.getStatusCode());
    }
}
