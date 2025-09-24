package io.github.zapolyarnydev.ptknow.api.v0;

public record ApiResponse <T> (ApiStatus status, String message, T data) {

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(ApiStatus.SUCCESS, message, data);
    }

    public static ApiResponse<Void> success(String message) {
        return new ApiResponse<>(ApiStatus.SUCCESS, message, null);
    }

    public static <T> ApiResponse<T> failure(String message) {
        return new ApiResponse<>(ApiStatus.FAILURE, message, null);
    }

}
