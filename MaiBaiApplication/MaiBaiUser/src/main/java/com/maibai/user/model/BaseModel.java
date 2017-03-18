package com.maibai.user.model;

import java.io.Serializable;

/**
 * Created by cuiyk on 16-6-16.
 */
public class BaseModel<T> implements Serializable {

    public int ret;
    public String message;
    public T  response;


    @Override
    public String toString() {
        return "BaseModel{" +
                "ret=" + ret +
                ", message='" + message + '\'' +
                ", response=" + response +
                '}';
    }
}
