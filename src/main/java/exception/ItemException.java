package exception;

import response.StatusResponse;

public class ItemException extends Exception {

    private StatusResponse status;

    public ItemException(String message, Throwable cause, StatusResponse status) {
        super(message, cause);
        this.status = status;
    }

    public ItemException(String message, StatusResponse status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status.getStatus();
    }
}
