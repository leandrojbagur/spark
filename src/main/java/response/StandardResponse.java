package response;

import com.google.gson.JsonElement;

public class StandardResponse {

    private String message;
    private JsonElement data;

    public StandardResponse(String message) {
        this.message = message;
    }

    public StandardResponse(String message, JsonElement data) {
        this.message = message;
        this.data = data;
    }

}
