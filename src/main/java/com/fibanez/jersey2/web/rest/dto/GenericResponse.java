package com.fibanez.jersey2.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponse implements Serializable {

    @JsonProperty("type")
    String type;

    @JsonProperty("status")
    String status;

    @JsonProperty("code")
    String code;

    @JsonProperty("message")
    String message;

    @JsonProperty("description")
    String description;

    public static class Builder {
        String type;
        String status;
        String code;
        String message;
        String description;

        public Builder type(String type) {
            this.type = type;
            return this;
        }
        public Builder status(String status) {
            this.status = status;
            return this;
        }
        public Builder code(String code) {
            this.code = code;
            return this;
        }
        public Builder message(String message) {
            this.message = message;
            return this;
        }
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        public GenericResponse build() {
            return new GenericResponse(this);
        }

    }

    public GenericResponse(Builder builder) {
        this.type = builder.type;
        this.status= builder.status;
        this.code= builder.code;
        this.message= builder.message;
        this.description= builder.description;
    }

}
