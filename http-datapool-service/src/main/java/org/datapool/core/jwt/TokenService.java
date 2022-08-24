package org.datapool.core.jwt;

public interface TokenService<V> {

    TokenObject decryptToken(String token);

    String createToken(V data);
}
