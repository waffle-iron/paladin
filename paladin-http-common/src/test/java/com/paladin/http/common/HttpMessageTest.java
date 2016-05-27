package com.paladin.http.common;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.paladin.http.common.Contants.CRLF;
import static mockit.Deencapsulation.getField;
import static mockit.Deencapsulation.setField;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author cluttered.code@gmail.com
 */
@RunWith(JMockit.class)
@SuppressWarnings("unused")
public class HttpMessageTest {

    private static final Logger LOG = LoggerFactory.getLogger(HttpMessageTest.class);

    @Tested
    @Mocked
    private HttpMessage httpMessage;

    @Injectable
    private Map<String, String> headers;

    @Injectable
    private final String body = "BODY";

    @Test
    public void testConstructor() {
        assertThat(getField(httpMessage, "headers"), is(headers));
        assertThat(httpMessage.getBody(), is(body));
    }

    @Test
    public void testListHeaders(@Mocked final Set<String> headerSet) {
        new Expectations() {{
            headers.keySet(); times = 1; result = headerSet;
        }};

        final Set<String> actual = httpMessage.listHeaders();

        assertThat(actual, is(headerSet));
    }

    @Test
    public void testGetHeader() {
        final String headerName = "HEADER_NAME";
        final String headerValue = "HEADER_VALUE";

        new Expectations() {{
            headers.get(headerName); times = 1; result = headerValue;
        }};

        final String actual = httpMessage.getHeader(headerName);

        assertThat(actual, is(headerValue));
    }

    @Test
    public void testToString() {
        final String headerName1 = "HEADER_NAME_1";
        final String headerValue1 = "HEADER_VALUE_1";
        final String headerName2 = "HEADER_NAME_2";
        final String headerValue2 = "HEADER_VALUE:2";
        final Map<String, String> newHeaders = new HashMap<>();
        newHeaders.put(headerName1, headerValue1);
        newHeaders.put(headerName2, headerValue2);

        setField(httpMessage, "headers", newHeaders);

        String expected = "";
        for(Map.Entry<String, String> entry : newHeaders.entrySet()) {
            expected += entry.getKey() + ":" + entry.getValue() + CRLF;
        }
        expected += CRLF + body;

        final String actual = httpMessage.toString();

        LOG.info(actual);
        assertThat(actual, is(expected));
    }
}