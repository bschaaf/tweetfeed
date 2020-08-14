package org.interview.model;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ModelUtil {
    private static final String DATE_FORMAT = "EEE MMM dd HH:mm:ss ZZZ yyyy";
    private static final String CREATEDAT_FIELD = "created_at";


    static Long setCreatedAtDateEpoch(String createdAtDateString){
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT, Locale.ENGLISH);
    Long createdAt= null;
    try{
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(createdAtDateString,formatter);
        createdAt = zonedDateTime.toInstant().toEpochMilli()/1000;
    } catch(DateTimeParseException e){
        log.info("Date can't be parsed:" +  e.getMessage());
    } 
    return createdAt;
}

    static String getCreatedAtDateString(Long createdAt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT, Locale.ENGLISH);
        Instant i = Instant.ofEpochSecond(createdAt);
        ZonedDateTime createdAtDateTime = ZonedDateTime.ofInstant(i, ZoneOffset.UTC);
        return createdAtDateTime.format(formatter);
    }

    static class CustomValueSerializer extends JsonSerializer<Long> {
        CustomValueSerializer() {
            super();
        }

        @Override
        public void serialize(Long l, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                throws IOException {

            if (jsonGenerator.getOutputContext().getCurrentName().equals(CREATEDAT_FIELD)) {
                jsonGenerator.writeString(getCreatedAtDateString(l));
            } else {
                jsonGenerator.writeNumber(l);
            }
        }

        @Override
        public Class<Long> handledType() {
            return Long.class;
        }
    }

}