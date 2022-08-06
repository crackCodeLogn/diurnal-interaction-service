package com.vv.personal.diurnal.interaction.transmission.reader;

import com.vv.personal.diurnal.artifactory.generated.OtpMailProto;

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
 * @since 31/10/21
 */
@Provider
@Consumes("application/x-protobuf")
public class OtpMailRequestMessageBodyReader implements MessageBodyReader<OtpMailProto.OtpMail> {

    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return OtpMailProto.OtpMail.class.isAssignableFrom(aClass);
    }

    @Override
    public OtpMailProto.OtpMail readFrom(Class<OtpMailProto.OtpMail> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> multivaluedMap, InputStream inputStream) throws IOException, WebApplicationException {
        return OtpMailProto.OtpMail.parseFrom(inputStream.readAllBytes());
    }
}