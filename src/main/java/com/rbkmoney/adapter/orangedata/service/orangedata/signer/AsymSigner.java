package com.rbkmoney.adapter.orangedata.service.orangedata.signer;


import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class AsymSigner implements com.rbkmoney.adapter.orangedata.service.orangedata.signer.Signer {

    private KeyFactory keyFactory;
    private Signature sig;

    public AsymSigner() {
        try {
            this.keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            this.sig = Signature.getInstance(HASH_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new UnknownCryptoException(e);
        }
    }

    @Override
    public String sign(String data, String privateKey) {
        try {
            byte[] encodedPrivateKey = java.util.Base64.getDecoder().decode(privateKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
            PrivateKey privKey;
            synchronized (keyFactory) {
                privKey = keyFactory.generatePrivate(keySpec);
            }
            byte[] signatureBytes;
            synchronized (sig) {
                sig.initSign(privKey);
                sig.update(data.getBytes(StandardCharsets.UTF_8));
                signatureBytes = sig.sign();
            }
            return java.util.Base64.getEncoder().encodeToString(signatureBytes);
        } catch (InvalidKeySpecException | InvalidKeyException | SignatureException e) {
            throw new UnknownCryptoException(e);
        }
    }

}
