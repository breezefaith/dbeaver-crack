package com.dbeaver.agent;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

public class Cipher {
    public static String RSA_PRIVATE_KEY = "MIIEuwIBADANBgkqhkiG9w0BAQEFAASCBKUwggShAgEAAoIBAQCMKf4/q7OUy+BHIN33SWf/E/Jfdusa6rmXDlNSOJv7TLLRxrP81OGef3vw4PB1gTBfgDUkGuUTmPAyXHAJ6kUjH6YDb2o5PgurSIqPNJAkeAPs3WqW7PsvzWp1shWaR1v1MiJhVaXxX4fxaIHG1k3URbsii8S5E7BvjtPGwHgfjf2ZG1OoVNEsY5aneprh/saCPdvb5EP/VBhPczfTNBKs70Wxf/oCMTVw4s4MyqVrhy2aFoc4zjzigkwRW0headVaJwWKUqkFwsK4Xe2sGfwD6V1W6NmK44f8j1U+sXVcDNRqZ8jAwo74DGcXjOMsAVmgokp5QpvmxsdCKQweMZxDAgMBAAECgf8dLqENoJh2SMkcbG6XJ2SZNw3ynxyHMVelztW9Yj8Xky1Bf9CTICXZWhfz9Ue0yOQvys54z4cVc9vB07BMxpNrPKugpRHFctjH6jju/k9q4fhVpV1yQhgKKKDoE3ifJm9+6j4LBeOgdym8/7hrxzKgTErDmbx1MoqjFczOrkPt5zm1vB5ZTye5cyh2kqPcvOUxtWBX15aLhs+gkuAo35iZ5Ehz6bHjFf/18XNWcvWeT2MWFuNgCMi4a471eSe05/TqoWYR0IWd+a1SzJe0mM4OmEPjtt2BH5NxrcLmYY6z7WBNgt4OQZa4NleGI9kDcUyUl6RZTx1wx+iY7t/zbRkCgYEAupvM8dIMukRJIKDM9C6FuxLXbWMvc2y0OHyMj1qQOul/hEFDnLGK6W2b0XVW8SXeCt2kokpfCPwqgluY3XA7jnaRoQ13+IChodVR+zNtpeK2aEjtkdakJgiZmZcPO6Cp+DG8pyVXf1FIEzvzJVTPsod97B0E8nMmlmHReVxLFfkCgYEAwEjndhaCL7iHksgXTTJgwNWvlCrPFNZGv92KbTvi+YGxogFOYxQ5VOoYV/DKLAXtT+bTCZrBDLuTcmVlpxB38FOOHWctV4M4sPun97t8QbK7QX9uSgQIPhZHW7J6FcljcPhebSxa/3AB/bW+YV25hGfzwCTC4O2kRxMNvZS/YxsCgYA5Q3w1rN6I/lFueMnMGMai7Jo9EBXMlswLFXVQvFrMKUEfRaPI3tag4SjSsu5WG+eyb+eIgcf+CtvEmvUi5sDv68SupmJtWJPBBKQesN5x7EtLXy2/9hzzKupvrmJXSus9pY2dwSDDih7TAC+SAh+mSlvC6/jLqbhqp2PPmRnViQKBgC9E/Nmz2NdAZtu3orrQiqZ9IYesKnI15vidzrzhvv+VWYS5C3sh6qIsVkWwJRNDdb5xwSmYSKMHrcyujpnOW53R/JrP1X6w/IvNodIt3fMMrCoC2KsLaLdSpMIvEwIuGusXs6blA43ODD9BoYawEr4xC+QNZXcEFKGAVp1nZtFHAoGBAKSZ60QVJZCKqA7rgE8zBTQBUf1Kz5IghjRsAZ4pfzaMLZwpgSUSgxeARmVa0WKXedSNDji25Secp1ZQAU5xVHNmiP2fP3X7nW1v1QjPvlnV7bHes5YjtHBhjxyvNvtDxK0w7SJOaBFlo55GEHpWK+oGVG9n6VPcrifLFbxNSJNG";
    public byte[] localKeyBytes = Base64.getDecoder().decode(RSA_PRIVATE_KEY);

    public Key getPublicKey() {
        try {
            PrivateKey myPrivateKey = (PrivateKey) getPrivateKey();
            RSAPrivateCrtKey privk = (RSAPrivateCrtKey) myPrivateKey;
            RSAPublicKeySpec publicKeySpec = null;
            if (privk == null) {
                throw new InvalidKeySpecException();
            }
            publicKeySpec = new RSAPublicKeySpec(privk.getModulus(), privk.getPublicExponent());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(publicKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Key getPrivateKey() {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(localKeyBytes);
            return keyFactory.generatePrivate(privateKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

}
