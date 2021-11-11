package com.lizumin.easyimage.filter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.lizumin.easyimage.model.entity.User;
import com.lizumin.easyimage.model.entity.UserProfile;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.boot.jackson.JsonObjectSerializer;

import java.io.IOException;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/4 1:52 am
 * 4
 */
@JsonComponent
public class AccountJsonComponent {

    /**
     *
     * How to serialize the User object
     *
     */
    public static class Serializer extends JsonObjectSerializer<User>{
        @Override
        protected void serializeObject(User value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeStringField("username",value.getUsername());
        }
    }
}
