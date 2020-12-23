package com.example.demo;

import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.converter.json.MappingJacksonValue;

@Getter
@Setter
public class CoreResponseBody<T> {

    private T result;
    private String message;
    private Boolean success;
    private Exception error;
    private String errorCode;

    public CoreResponseBody() {

    }

    /**
     * Create an responseBody used in ResponseEntity.
     *
     * @param result  coreResponse result.
     * @param message coreResponse message.
     * @param error   Exception.
     */
    private CoreResponseBody(@Nullable T result, String message, Exception error, String errorCode) {
        this.result = result;
        this.message = message;
        this.error = error;
        this.errorCode = errorCode;
    }

    public static <T> MappingJacksonValue result2(@Nullable T result) {
        MappingJacksonValue mapper = new MappingJacksonValue(new CoreResponseBody(result, null, null, null));
        SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider().setFailOnUnknownId(false);
        // simpleFilterProvider.addFilter("oneFilter", SimpleBeanPropertyFilter.filterOutAllExcept("id","address"));
        mapper.setFilters(simpleFilterProvider);
        return mapper;
    }


    public static <T> CoreResponseBody<T> result(@Nullable T result) {
        return new CoreResponseBody<T>(result, null, null, null);
    }

    public static <T> CoreResponseBody<T> result(@Nullable T result, String message) {
        return new CoreResponseBody<T>(result, message, null, null);
    }

    public static <T> CoreResponseBody<T> error(Exception error) {
        return new CoreResponseBody<T>(null, null, error, null);
    }

    public static <T> CoreResponseBody<T> error(Exception error, String message) {
        return new CoreResponseBody<T>(null, message, error, null);
    }

    public static <T> CoreResponseBody<T> error(Exception error, String message, String errorCode) {
        return new CoreResponseBody<T>(null, message, error, errorCode);
    }

    public String getMessage() {
        return message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public Exception getError() {
        return error;
    }

    public T getResult() {
        return result;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public CoreResponseBody<T> setResult(T result) {
        this.result = result;
        return this;
    }


    public CoreResponseBody<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public CoreResponseBody<T> setSuccess(Boolean success) {
        this.success = success;
        return this;
    }

    public CoreResponseBody<T> setError(Exception error) {
        this.error = error;
        return this;
    }

}