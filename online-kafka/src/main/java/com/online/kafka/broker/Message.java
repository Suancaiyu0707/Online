package com.online.kafka.broker;

import java.io.Serializable;
import java.util.zip.CRC32;

public class Message implements Serializable {

    private CRC32 crc32;

    private short magic;

    private boolean codeEnabaled;

    private short codeClassOrdinal;

    private String key;

    private String value;
}
