package com.services.ServiceProvider.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.services.ServiceProvider.constant.Constants;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;

@Data
@Builder
@NoArgsConstructor
public class ApiResponse {

    private int code;

    private boolean status;

    private String message;

    @Builder.Default
    private Object data= Collections.emptyMap();

    @JsonIgnore
    private int httpStatusCode;

    public ApiResponse(int code, boolean status, String message, Object data,int httpStatusCode) {
        this.code = code;
        this.status = status;
        this.message = message!=null?message: Constants.RESPONSE.get(code);
        this.data = data;
        this.httpStatusCode=status?200:400;

    }
}