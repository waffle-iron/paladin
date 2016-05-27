package com.paladin.http.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.paladin.http.common.Contants.CRLF;

/**
 * @author cluttered.code@gmail.com
 */
public class HttpRequest extends HttpMessage {

    private final HttpMethod method;
    private final String target;
    private final String version;

    HttpRequest(final HttpMethod method, final String target, final String version,
                        final Map<String, String> headers, final String body) {
        super(headers, body);
        this.method = method;
        this.target = target;
        this.version = version;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getTarget() {
        return target;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return method + " " + target + " " + version + CRLF + super.toString();
    }
}