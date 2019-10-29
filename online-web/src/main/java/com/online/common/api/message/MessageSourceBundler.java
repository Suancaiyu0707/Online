package com.online.common.api.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;


@Slf4j
public class MessageSourceBundler implements MessageSourceAware, InitializingBean {

    private final static Locale DefaultLocale = Locale.CHINA;

    private static MessageSource staticMessageSource;

    @Override
    public void setMessageSource(MessageSource messageSource) {
        if (staticMessageSource == null) {
            staticMessageSource = messageSource;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (staticMessageSource == null) {
            throw new NullPointerException("MessageSource不能为null");
        }
    }

    public static String getMessage(String code) {
        return getMessage(code, new Object[0]);
    }

    public static String getMessage(String code, Object[] args) throws NoSuchMessageException {
        return getMessage(code, args, null);
    }

    public static String getMessage(String code, Object[] args, String defaultMessage) {
        return staticMessageSource.getMessage(code, args, defaultMessage, getLocale());
    }

    public static String getMessage(MessageSourceResolvable resolvable) throws NoSuchMessageException {
        return staticMessageSource.getMessage(resolvable, getLocale());
    }

    private static Locale getLocale() {
        return DefaultLocale;
    }
}
