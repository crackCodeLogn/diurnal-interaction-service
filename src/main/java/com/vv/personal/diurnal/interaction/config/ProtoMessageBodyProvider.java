package com.vv.personal.diurnal.interaction.config;

import com.vv.personal.diurnal.artifactory.generated.OtpMailProto;

import javax.ws.rs.BadRequestException;
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
public class ProtoMessageBodyProvider implements MessageBodyReader {

    @Override
    public boolean isReadable(Class type, Type type1,
                              Annotation[] antns, MediaType mt) {
        return OtpMailProto.OtpMail.class.isAssignableFrom(type);
    }

    @Override
    public Object readFrom(Class type, Type type1, Annotation[] antns,
                           MediaType mt, MultivaluedMap mm, InputStream in)
            throws IOException, WebApplicationException {
        if (OtpMailProto.OtpMail.class.isAssignableFrom(type)) {
            return OtpMailProto.OtpMail.parseFrom(in);
        } else {
            throw new BadRequestException("Can't Deserailize");
        }
    }
}