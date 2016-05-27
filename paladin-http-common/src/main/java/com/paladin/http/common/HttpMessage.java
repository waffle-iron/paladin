package com.paladin.http.common;

import java.util.Map;
import java.util.Set;

import static com.paladin.http.common.Contants.COLON;
import static com.paladin.http.common.Contants.CRLF;

/**
 * @author cluttered.code@gmail.com
 */
public abstract class HttpMessage {

    private final Map<String, String> headers;
    private final String body;

    protected HttpMessage(final Map<String, String> headers, final String body) {
        this.headers = headers;
        this.body = body;
    }

    public Set<String> listHeaders() {
        return headers.keySet();
    }

    public String getHeader(final String name) {
        return headers.get(name);
    }

    public String getBody() {
        return body;
    }

    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for(Map.Entry<String, String> entry : headers.entrySet()) {
            builder.append(entry.getKey()).append(COLON).append(entry.getValue()).append(CRLF);
        }
        builder.append(CRLF).append(body);
        return builder.toString();
    }
}