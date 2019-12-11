package com.lcs.common;

import lombok.Data;

/**
 * Created by lowdad on 18-1-23.
 */
@Data
public class Result {
    private Boolean success;
    private Integer errCode;
    private String errMsg;
    private Object Data;
}
