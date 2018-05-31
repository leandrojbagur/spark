package controller;

import com.google.gson.Gson;
import exception.ItemException;
import service.IItemService;
import service.ItemService;
import spark.ModelAndView;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import response.StandardResponse;
import response.StatusResponse;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.staticFileLocation;

public class ItemController {

    public static void main(String[] args) {
        port(8080);
        final IItemService itemService = new ItemService();
        ThymeleafTemplateEngine engine = new ThymeleafTemplateEngine();
        staticFileLocation("/public");

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
            try {
                itemService.update(request.params("id"), request.body());
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
            try {
                itemService.delete(request.params(":id"));
                response.status(StatusResponse.SUCCESS.getStatus());
                return new Gson().toJson(new StandardResponse("Item eliminado correctamente"));
            } catch (ItemException e) {
                response.status(e.getStatus());
                return new Gson().toJson(new StandardResponse(e.getMessage()));
            }
        });
    }
}
