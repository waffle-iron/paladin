package com.paladin.http.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static com.paladin.http.common.Contants.CRLF;

/**
 * @author cluttered.code@gmail.com
 */
public class HttpRequestBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(HttpRequestBuilder.class);

    public static HttpRequest fromString(final String message) {
        final Scanner requestScanner = new Scanner(message).useDelimiter(CRLF);
        String requestLine = requestScanner.nextLine();

        // Request Line
        Scanner lineScanner = new Scanner(requestLine);
        final String method = lineScanner.next();
        final String target = lineScanner.next();
        final String version = lineScanner.next();

        // Headers
        final Map<String, String> headers = new HashMap<>();
        while(requestScanner.hasNextLine()) {
            requestLine = requestScanner.nextLine();
            if(requestLine.isEmpty()) {
                break;
            }
            lineScanner = new Scanner(requestLine).useDelimiter("\\s*:\\s*");
            headers.put(lineScanner.next(), lineScanner.nextLine());
        }

        // Body
        final StringBuilder bodyBuilder = new StringBuilder();
        while(requestScanner.hasNextLine()) {
            if (bodyBuilder.length() > 0) {
                bodyBuilder.append(CRLF);
            }
            bodyBuilder.append(requestScanner.nextLine());
        }

        final HttpRequest request = new HttpRequest(HttpMethod.valueOf(method), target, version, headers, bodyBuilder.toString());
        LOG.debug("Received HTTP Request: {}", request);
        return request;
    }
}