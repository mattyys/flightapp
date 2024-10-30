package org.tokioschool.flightapp.config;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;

import java.util.UUID;

public class UUIDDocumentListener extends AbstractMongoEventListener<UUIDDocument> {
    @Override
    public void onBeforeConvert(BeforeConvertEvent<UUIDDocument> event) {
        UUIDDocument uuidDocument = event.getSource();
        if(uuidDocument.getId() == null){
            uuidDocument.setId(UUID.randomUUID());
        }
    }
}
