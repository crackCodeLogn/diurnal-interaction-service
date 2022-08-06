package com.vv.personal.diurnal.interaction.transmission.reader;

import com.vv.personal.diurnal.artifactory.generated.TokenProto;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * @author Vivek
 * @since 06/08/21
 */
@Provider
@Consumes("application/x-protobuf")
public class TokenRequestMessageBodyReader implements MessageBodyReader<TokenProto.TokenShell> {

    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return TokenProto.TokenShell.class.isAssignableFrom(aClass);
    }

    @Override
    public TokenProto.TokenShell readFrom(Class<TokenProto.TokenShell> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> multivaluedMap, InputStream inputStream) throws IOException, WebApplicationException {
        return TokenProto.TokenShell.parseFrom(inputStream.readAllBytes());
    }
}