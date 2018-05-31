package service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import domain.Item;
import exception.ItemException;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import response.StatusResponse;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ItemService implements IItemService {

    public void save(String json) throws ItemException {
        try {
            Item item = new Gson().fromJson(json, Item.class);
            if (!item.validate()) throw new ItemException("Revise los campos obligatorios del item", StatusResponse.BAD_REQUEST);

            TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));

            client.prepareIndex("store", "item", item.getItemId())
                    .setSource(new Gson().toJson(item), XContentType.JSON)
                    .execute()
                    .actionGet();

            client.close();
        } catch (JsonSyntaxException jsonEx) {
            throw new ItemException("El item no es válido, corrobore los campos", jsonEx, StatusResponse.BAD_REQUEST);
        } catch (UnknownHostException e) {
            throw new ItemException("No se ha podido guardar correctamente el item", e, StatusResponse.ERROR);
        }
    }

    public void update(String itemId, String json) throws ItemException {
        try {
            Item item = new Gson().fromJson(json, Item.class);

            if (!item.validate()) throw new ItemException("Revise los campos obligatorios del item", StatusResponse.BAD_REQUEST);

            TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));

            GetResponse getResponse = client.prepareGet("store", "item", itemId).get();

            if (!getResponse.isExists()) throw new ItemException("El item que quiere actualizar no existe", StatusResponse.NOT_FOUND);

            client.prepareIndex("store", "item", itemId)
                    .setSource(new Gson().toJson(item), XContentType.JSON)
                    .execute()
                    .actionGet();
            client.close();
        } catch (JsonSyntaxException jsonEx) {
           throw new ItemException("El item no es válido, corrobore los campos", jsonEx, StatusResponse.BAD_REQUEST);
        } catch (UnknownHostException e) {
            throw new ItemException("No se ha podido actualizar correctamente el item", e, StatusResponse.ERROR);
        }
    }

    public Item get(String id) throws ItemException {
        try {
            TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));
            GetResponse response = client.prepareGet("store", "item", id).get();

            if (!response.isExists()) throw new ItemException("El item " + id + " no existe", StatusResponse.NOT_FOUND);

            return new Gson().fromJson(response.getSourceAsString(), Item.class);
        } catch (UnknownHostException e) {
            throw new ItemException("El item " + id + " no se encontró", e, StatusResponse.ERROR);
        }
    }

    public void delete(String itemId) throws ItemException {
        try {
            TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));
            DeleteResponse response = client.prepareDelete("store", "item", itemId).get();
            if (response.status().equals(RestStatus.NOT_FOUND)) throw new ItemException("El item " + itemId + " no existe", StatusResponse.NOT_FOUND);
        } catch (UnknownHostException e) {
            throw new ItemException("No fue posible eliminar el item " + itemId, e, StatusResponse.ERROR);
        }
    }

    public List<Item> getAll() throws ItemException {
        TransportClient client;
        try {
             client = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));
        } catch (UnknownHostException e) {
            throw new ItemException("Error para obtener todos los items", e, StatusResponse.ERROR);
        }

        SearchResponse response = client.prepareSearch("store")
                .setTypes("item")
                .setSize(100)
                .execute()
                .actionGet();

        List<Item> items = new ArrayList<>();
        Gson gson = new Gson();
        response.getHits().forEach(hit -> items.add(gson.fromJson(hit.getSourceAsString(), Item.class)));
        return items;
    }

}
