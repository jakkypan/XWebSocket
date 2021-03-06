package com.xwebservice.self;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;


class WebSocketHandshake {
    private static final String WEBSOCKET_VERSION = "13";

    private URI url = null;
    private String protocol = null;
    private String nonce = null;
    private Map<String, String> extraHeaders = null;


    public WebSocketHandshake(URI url, String protocol, Map<String, String> extraHeaders) {
        this.url = url;
        this.protocol = protocol;
        this.extraHeaders = extraHeaders;
        this.nonce = this.createNonce();
    }

    public byte[] getHandshake() {
        String path = url.getPath();
        String query = url.getQuery();
        path += query == null ? "" : "?" + query;
        String host = url.getHost();

        if (url.getPort() != -1) {
            host += ":" + url.getPort();
        }

        LinkedHashMap<String, String> header = new LinkedHashMap<String, String>();
        header.put("Host", host);
        header.put("Upgrade", "websocket");
        header.put("Connection", "Upgrade");
        header.put("Sec-WebSocket-Version", WEBSOCKET_VERSION);
        header.put("Sec-WebSocket-Key", this.nonce);

        if (this.protocol != null) {
            header.put("Sec-WebSocket-Protocol", this.protocol);
        }

        if (this.extraHeaders != null) {
            for (String fieldName : this.extraHeaders.keySet()) {
                // Only checks for Field names with the exact same text,
                // but according to RFC 2616 (HTTP) field names are case-insensitive.
                if (!header.containsKey(fieldName)) {
                    header.put(fieldName, this.extraHeaders.get(fieldName));
                }
            }
        }

        String handshake = "GET " + path + " HTTP/1.1\r\n";
        handshake += this.generateHeader(header);
        handshake += "\r\n";

        byte[] handshakeBytes = new byte[handshake.getBytes().length];
        System.arraycopy(handshake.getBytes(), 0, handshakeBytes, 0, handshake.getBytes().length);

        return handshakeBytes;
    }

    private String generateHeader(LinkedHashMap<String, String> headers) {
        String header = new String();
        for (String fieldName : headers.keySet()) {
            header += fieldName + ": " + headers.get(fieldName) + "\r\n";
        }
        return header;
    }

    private String createNonce() {
        byte[] nonce = new byte[16];
        for (int i = 0; i < 16; i++) {
            nonce[i] = (byte) rand(0, 255);
        }
        return Base64.encodeToString(nonce, false);
    }

    public void verifyServerStatusLine(String statusLine) {
        int statusCode = Integer.valueOf(statusLine.substring(9, 12));

        if (statusCode == 407) {
            throw new WebSocketException("connection failed: proxy authentication not supported");
        } else if (statusCode == 404) {
            throw new WebSocketException("connection failed: 404 not found");
        } else if (statusCode != 101) {
            throw new WebSocketException("connection failed: unknown status code " + statusCode);
        }
    }

    public void verifyServerHandshakeHeaders(HashMap<String, String> headers) {
        if (!headers.get("upgrade").toLowerCase(Locale.US).equals("websocket")) {
            throw new WebSocketException("connection failed: missing header field in server handshake: Upgrade");
        } else if (!headers.get("connection").toLowerCase(Locale.US).equals("upgrade")) {
            throw new WebSocketException("connection failed: missing header field in server handshake: Connection");
        }
    }

    private int rand(int min, int max) {
        int rand = (int) (Math.random() * max + min);
        return rand;
    }
}
