package cl.gfmn.authserver.model;

public record ErrorResponse(String timestamp, String message, String path) {
}
