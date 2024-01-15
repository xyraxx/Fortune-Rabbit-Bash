package dev.fs.mad.game11.controller

import java.nio.charset.StandardCharsets
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class Crypt {
    companion object {
        private val METHOD = "AES/CBC/PKCS5Padding"
        private val IV = "fedcba9876543210"

        @Throws(Exception::class)
        fun decrypt(message: String, key: String): String {
            val secretKeySpec = SecretKeySpec(key.toByteArray(), "AES")
            val cipher = Cipher.getInstance(METHOD)
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, IvParameterSpec(IV.toByteArray()))
            val decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(message))

            val trimmedBytes = ByteArray(decryptedBytes.size - 16)
            System.arraycopy(decryptedBytes, 16, trimmedBytes, 0, trimmedBytes.size)

            return String(trimmedBytes, StandardCharsets.UTF_8)
        }
    }
}