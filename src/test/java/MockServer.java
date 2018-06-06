import org.mockserver.client.server.MockServerClient;
import org.mockserver.matchers.Times;
import org.mockserver.model.Delay;
import org.mockserver.model.Header;


import static java.util.concurrent.TimeUnit.SECONDS;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.StringBody.exact;

public class MockServer {

    static MockServerClient mockServer = startClientAndServer(1200);

    public static void main(String[] args) {
        // ------------------ DELETE REQUEST

        mockServer.when(request()
                .withMethod("DELETE")
                .withPath("/delete/0")
        )
                .respond(response().withStatusCode(404)
                        .withHeaders(new Header("Content-Type", "application/json; charset=utf-8"))
                        .withBody("{\"message\":\"El item 0 no existe\"}")
                        .withDelay(new Delay(SECONDS, 1)));

        mockServer.when(request()
                .withMethod("DELETE")
                .withPath("/delete/ITEMTEST")
        )
                .respond(response().withStatusCode(200)
                        .withHeaders(new Header("Content-Type", "application/json; charset=utf-8"))
                        .withBody("{\"message\":\"Item eliminado correctamente\"}")
                        .withDelay(new Delay(SECONDS, 1)));

        // ------------------ UPDATE REQUEST

        mockServer.when(request()
                .withMethod("PUT")
                .withPath("/update/ITEMTEST")
                .withBody(exact("{\"itemId\":\"ITEM0001\",\"title\":\"Nuevo test item\",\"categoryId\":\"Categoria\",\"price\":10,\"availableQuantity\":0,\"description\":\"Test item\"}"))
        )
                .respond(response().withStatusCode(400)
                        .withHeaders(new Header("Content-Type", "application/json; charset=utf-8"))
                        .withBody("{\"message\":\"El id del item no puede ser modificado\"}")
                        .withDelay(new Delay(SECONDS, 1)));

        mockServer.when(request()
                .withMethod("PUT")
                .withPath("/update/ITEMTEST")
                .withBody("{\"itemId\":\"ITEMTEST\",\"categoryId\":\"Categoria\",\"availableQuantity\":0,\"description\":\"Test item\"}")
        )
                .respond(response().withStatusCode(400)
                        .withHeaders(new Header("Content-Type", "application/json; charset=utf-8"))
                        .withBody("{\"message\":\"Revise los campos obligatorios del item\"}")
                        .withDelay(new Delay(SECONDS, 1)));

        mockServer.when(request()
                .withMethod("PUT")
                .withPath("/update/ITEMINEXISTENTE")
                .withBody("{\"itemId\":\"ITEMINEXISTENTE\",\"title\":\"Nuevo test item\",\"categoryId\":\"Categoria\",\"price\":10,\"availableQuantity\":0,\"description\":\"Test item\"}")
        )
                .respond(response().withStatusCode(404)
                        .withHeaders(new Header("Content-Type", "application/json; charset=utf-8"))
                        .withBody("{\"message\":\"El item que quiere actualizar no existe\"}")
                        .withDelay(new Delay(SECONDS, 1)));

        mockServer.when(request()
                .withMethod("PUT")
                .withPath("/update/ITEMTEST")
                .withBody(exact("{\"itemId\":\"ITEMTEST\",\"title\":\"Nuevo test item\",\"categoryId\":\"Categoria\",\"price\":10,\"availableQuantity\":0,\"description\":\"Test item\"}"))
        )
                .respond(response().withStatusCode(200)
                        .withHeaders(new Header("Content-Type", "application/json; charset=utf-8"))
                        .withBody("{\"message\":\"Item actualizado correctamente\"}")
                        .withDelay(new Delay(SECONDS, 1)));

        // ------------------ POST REQUEST

        mockServer.when(request()
                        .withMethod("POST")
                        .withPath("/create")
                        .withBody("{\"itemId\":\"ITEMTEST\",\"title\":\"Nuevo test item\",\"categoryId\":\"Categoria\",\"price\":10,\"availableQuantity\":0,\"description\":\"Test item\"}"),
                Times.exactly(1)
        )
                .respond(response().withStatusCode(200)
                        .withHeaders(new Header("Content-Type", "application/json; charset=utf-8"))
                        .withBody("{ \"message\": \"Item creado correctamente\"}")
                        .withDelay(new Delay(SECONDS, 1)));

        mockServer.when(request()
                .withMethod("POST")
                .withPath("/create")
                .withBody("{\"itemId\":\"ITEM0012\",\"title\":\"Titulo del item\",\"availableQuantity\":0,\"description\":\"Test item\"}")
        )
                .respond(response().withStatusCode(400)
                        .withHeaders(
                                new Header("Content-Type", "application/json; charset=utf-8"))
                        .withBody("{ \"message\": \"Revise los campos obligatorios del item\"}")
                        .withDelay(new Delay(SECONDS, 1)));

        mockServer.when(request()
                .withMethod("POST")
                .withPath("/create")
                .withBody("{\"itemId\":\"ITEM0013\",\"categoryId\":\"Categoria\",\"availableQuantity\":0,\"description\":\"Test item\"}")
        )
                .respond(response().withStatusCode(400)
                        .withHeaders(
                                new Header("Content-Type", "application/json; charset=utf-8"))
                        .withBody("{ \"message\": \"Revise los campos obligatorios del item\"}")
                        .withDelay(new Delay(SECONDS, 1)));

        mockServer.when(request()
                .withMethod("POST")
                .withPath("/create")
                .withBody("{\"itemId\":\"ITEMTEST\",\"title\":\"Nuevo test item\",\"categoryId\":\"Categoria\",\"price\":10,\"availableQuantity\":0,\"description\":\"Test item\"}")
        )
                .respond(response().withStatusCode(400)
                        .withHeaders(
                                new Header("Content-Type", "application/json; charset=utf-8"))
                        .withBody("{ \"message\": \"El item que desea crear ya existe\"}")
                        .withDelay(new Delay(SECONDS, 1)));

        // ------------------ GET REQUEST

        mockServer
                .when(request()
                        .withMethod("GET")
                        .withPath("/item/0")
                )
                .respond(response()
                        .withStatusCode(404)
                        .withDelay(new Delay(SECONDS, 1)));

        mockServer
                .when(request()
                        .withMethod("GET")
                        .withPath("/item/12351230")
                )
                .respond(response()
                        .withStatusCode(404)
                        .withDelay(new Delay(SECONDS, 1)));

        mockServer
                .when(request()
                        .withMethod("GET")
                        .withPath("/item/ITEMTEST")
                )
                .respond(response()
                        .withStatusCode(200)
                        .withDelay(new Delay(SECONDS, 1)));

    }
}
