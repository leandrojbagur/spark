package controller;

import com.google.gson.Gson;
import exception.ItemException;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.IItemService;
import service.ItemService;
import spark.ModelAndView;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import response.StandardResponse;
import response.StatusResponse;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.staticFileLocation;

public class ItemController {

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    public static void main(String[] args) throws UnknownHostException {
        logger.info("Inicializando aplicacion");
        port(8080);
        final IItemService itemService = new ItemService();
        ThymeleafTemplateEngine engine = new ThymeleafTemplateEngine();
        staticFileLocation("/public");
        createIndex();

        // Home path
        get("/store", (request, response) -> new ModelAndView(new HashMap<>(), "list") , engine);

        // Show edit view
        get("/create", (request, response) -> new ModelAndView(new HashMap<>(), "edit"), engine);

        // Show edit view with item data
        get("/edit/:id", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("item", itemService.get(request.params(":id")));
            response.status(StatusResponse.SUCCESS.getStatus());
            return new ModelAndView(model, "edit");
        }, engine);

        // Create a new item
        post("/create", (request, response) -> {
            logger.info("Creando nuevo item");
            try {
                itemService.save(request.body());
                response.status(StatusResponse.SUCCESS.getStatus());
                return new Gson().toJson(new StandardResponse("Item creado correctamente"));
            } catch (ItemException e) {
                response.status(e.getStatus());
                return new Gson().toJson(new StandardResponse(e.getMessage()));
            }
        });

        // Update a specific item
        put("/update/:id", (request, response) -> {
            String itemId = request.params("id");
            logger.info("Actualizando item " + itemId);
            try {
                itemService.update(itemId, request.body());
                response.status(StatusResponse.SUCCESS.getStatus());
                return new Gson().toJson(new StandardResponse("Item actualizado correctamente"));
            } catch (ItemException e) {
                response.status(e.getStatus());
                return new Gson().toJson(new StandardResponse(e.getMessage()));
            }
        });

        // Get all items
        get("/items", (request, response) -> {
            try {
                response.type("application/json");
                return new Gson().toJson(itemService.getAll());
            } catch (ItemException e) {
                response.status(e.getStatus());
                return new Gson().toJson(new StandardResponse(e.getMessage()));
            }
        });

        // Get a specific item
        get("/item/:id", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            try {
                model.put("item", itemService.get(request.params(":id")));
                response.status(StatusResponse.SUCCESS.getStatus());
                return new ModelAndView(model, "item");
            } catch (ItemException e) {
                response.status(e.getStatus());
                model.put("message", e.getMessage());
                return new ModelAndView(model, "item");
            }
        }, engine);

        // Delete a specific item
        delete("/delete/:id", (request, response) -> {
            String itemId = request.params(":id");
            logger.info("Eliminando item " + itemId);
            try {
                itemService.delete(itemId);
                response.status(StatusResponse.SUCCESS.getStatus());
                return new Gson().toJson(new StandardResponse("Item eliminado correctamente"));
            } catch (ItemException e) {
                response.status(e.getStatus());
                return new Gson().toJson(new StandardResponse(e.getMessage()));
            }
        });
    }

    private static void createIndex() throws UnknownHostException {
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));

        String[] indices = client.admin().indices().getIndex(new GetIndexRequest()).actionGet().getIndices();

        if (!Arrays.asList(indices).contains("store")) {
            logger.info("Inicializando indice de ElasticSearch 'store'");
            client.admin().indices().create(new CreateIndexRequest("store")).actionGet();
        }
    }
}
