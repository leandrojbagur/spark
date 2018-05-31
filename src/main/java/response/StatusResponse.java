package response;

public enum StatusResponse {

    SUCCESS(200), BAD_REQUEST(400), NOT_FOUND(404), ERROR(500);

    private final int status;

    StatusResponse(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
