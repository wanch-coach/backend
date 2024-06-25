package com.wanchcoach.global.error;

import com.wanchcoach.global.util.MessageUtils;
import org.apache.commons.lang3.StringUtils;

public class InvalidJWTException extends ServiceRuntimeException{

    static final String MESSAGE_KEY = "error.InvalidJWTToken";

    static final String MESSAGE_DETAILS = "error.InvalidJWTToken.datails";

    public InvalidJWTException(Class cls, Object... values) {
        this(cls.getSimpleName(), values);
    }

    public InvalidJWTException(String targetName, Object... values) {
        super(MESSAGE_KEY, MESSAGE_DETAILS, new String[]{targetName,
                (values != null && values.length > 0) ? StringUtils.join(values, ",") : ""});
    }


    @Override
    public String getMessage() {
        return MessageUtils.getMessage(getDetailKey(), getParams());
    }

    @Override
    public String toString() {
        return MessageUtils.getMessage(getMessageKey());
    }
}
