package kreadcn.homework.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 描述
 *
 * @author liuweis
 * @date 2020/8/31
 */
@Data
public class ResponseData<T> {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer errorCode;

    private T data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorMessage;

    private Boolean success;
}
