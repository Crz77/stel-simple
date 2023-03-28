package dao.implementations;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


// class for getting the Type of a generic class

public abstract class TypeToken<T> {
    private final Type type;

    protected TypeToken() {
        Type superClass = getClass().getGenericSuperclass();
        type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    public Type getType() {
        return type;
    }
}
