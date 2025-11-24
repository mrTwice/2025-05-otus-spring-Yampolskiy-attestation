package ru.otus.java.springframework.yampolskiy.tttaskservice.storage.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.otus.java.springframework.yampolskiy.tttaskservice.common.securiry.AbstractAccessPolicy;

@Slf4j
@Component("storageAccessPolicy")
public class StorageAccessPolicy extends AbstractAccessPolicy {

}
