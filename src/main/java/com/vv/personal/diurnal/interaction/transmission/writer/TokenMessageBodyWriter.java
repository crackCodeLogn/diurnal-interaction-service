package com.vv.personal.diurnal.interaction.transmission.writer;

import com.vv.personal.diurnal.artifactory.generated.TokenProto;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * @author Vivek
 * @since 06/08/21
 */
@Provider
@Produces("application/x-protobuf")
public class TokenMessageBodyWriter implements MessageBodyWriter<TokenProto.TokenShell> {

    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return TokenProto.TokenShell.class.isAssignableFrom(aClass);
    }

    @Override
    public long getSize(TokenProto.TokenShell tokenShell, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return tokenShell.getSerializedSize();
    }

    @Override
    public void writeTo(TokenProto.TokenShell tokenShell, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> multivaluedMap, OutputStream outputStream) throws IOException, WebApplicationException {
        outputStream.write(tokenShell.toByteArray());
    }
}