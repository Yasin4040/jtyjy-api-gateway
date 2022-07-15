package com.jtyjy.gateway.common.exception;

import java.text.MessageFormat;

/**
 * Description:
 * Created by ZiYao Lee on 2022/07/15.
 * Time: 15:43
 */
public interface BizExceptionAssert extends ErrorCode {
    default BizException newException() {
        return new BizException(this);
    }

    default BizException newException(Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);
        return new BizException(this.getCode(), msg);
    }

    default BizException newException(String message) {
        return new BizException(this.getCode(), message);
    }

    default BizException newException(String message, Object... args) {
        String msg = MessageFormat.format(message, args);
        return new BizException(this.getCode(), msg);
    }

    default BizException newException(Throwable t, Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);
        return new BizException(this.getCode(), msg, t);
    }
}
