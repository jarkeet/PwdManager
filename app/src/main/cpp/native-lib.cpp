#include <string>
#include <android/log.h>
#include "rsautil.h"
// log标签
#define  LOG_TAG    "jarkeet_JNITAG"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)


extern "C"
JNIEXPORT jstring JNICALL
Java_com_cmcc_jeff_pwdmanager_utils_RSAUtil_getPrivateKeyFromJni(
        JNIEnv *env,
        jobject /* this */) {
    std::string privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCzyB1kkVhRZeTZb3O5VPYWgzUr\n"
            "96tR1ZvTqlR02spxPtzBeX/aj7NfF3ufaijmhoOVW411XNcCVAUqWHWYxo7TOiJFPPdWVWoeAgfX\n"
            "Jrll+5vL5xvnTmB540QsibHNyubUlpYKGQWqL4pzQK1yhTVXiD7WHw/sRnPc1Y/i05zl031NvdkD\n"
            "y3GkAAtbYkdFTz6XPJNv9rJMAEwJmTKTW2EuCaeBg3ExGYARTjU6UtLpH5N57+WA+bDNSU7Y+kMg\n"
            "FH7UlhLY02RFNJOj2wClwuPfUwIXKazIR3j8dKAxpZu2mngSECTqHXmQYL9cHjYNiRVGIVI90WPg\n"
            "RQOx0Z0Qx1wlAgMBAAECggEBAIbP8PqQ4yFmvw0tviGjzEVlbmHh/t4GyCsY9uwJhPUWpoOOkIlc\n"
            "aK1N9q279Z+oDtPZK2Juk+xzBMyUONqvSjqqxTgWV4Kt2j1WWHrF1xDadCGa1BnHdCDygxTCzEn9\n"
            "dMrFgQ1hZhYQQEncnjPaH/3bdQMrl80dp5RZsOPuuyC7C9Zyq4LpD3tfvjuynB76p7uDDq/yOuyI\n"
            "NzqdLfU2eWNPDyyoS0povHqESP1XBaayBYLD4ONw9bjyketBVIoiy9bA9mBBCLTwaQd0IuOubaAp\n"
            "AfKtne0TDixhE1ZNHuSsci0KZheMtD3TGcNugZADXMFr64ae+XSQnV2AlXF4jAECgYEA6J3c8qYu\n"
            "n8jPP654Zdp6+J+xPYf3LvY/Re5QaPhCPjGIj5EG5M76UAstvzD7QE1YZgP1QCToBUKeE5cMyn8S\n"
            "R30q2WSGNKJbH9ooUl+MxiXucahpu8ZwgU0AfJWRF6X52k6Ei+wuaUlcg7p2btIN0MMlI7AdhCql\n"
            "6r4r0WfjI6ECgYEAxdqaAMybWUWWQxf34j4UCleA8oqFD71BJm4xB9qAiSkNBSAUkslzdQSqDG1x\n"
            "D66zEa5werszK2LJLgRGkfLGgjh43dmHhh59HVDxbcPEQedy+OOFYxhWmKw/rHfyZmJU5s9F/JTR\n"
            "n1RFqZLcIsFE8Tki3ub+7WbOYwPpNTU3agUCgYB1rE42TczuNcZpv5fWZpSoqxgdOWfY0OcnOCxj\n"
            "PZs9HQ+pGMN1AiNsVIta2atwVvAuAziQr1rUNmQDIvlsgqW7ll0Txh+CBtO0JEcbjIyJPG0IABsk\n"
            "y3jkYBCr9XmFjgf74JZ6tgqwZlUvqib5XrdwcRebmx8DWhO+0V8XOseToQKBgC1pSSOZ0qYgxKZN\n"
            "oAV7fhoJj30bsgK0y3lInVRSIGJa27Qrcs1mE2vuUCFeFS8mR89tHD/vK4VvvQEk1hW1hT5qO8Of\n"
            "o+1yngDWjTNGVYtS1h6uCfV+ppGQJ2UseqRpOVdoh4W7molZTEvpe0E23t4G4SwMFMLkb8ZmJWai\n"
            "+j8JAoGBAIAWEwqbu4ezHqkbs25L3zhDumWb8JFUwdtimIe3/Y24E3s+XTwLoOHYYZnSm4DDNfND\n"
            "FDONA296eubG+lefj+IjjdzFaG3VkPobsZl32GjnVBXgCp5oraROzGxLbKR8TjPM5t6AE5AX4TP6\n"
            "JpGF/fMIIVSB6SzCgs18FPe3Wlxx";

    LOGD("get private key form native: %s ", privateKey.c_str());
    return env->NewStringUTF(privateKey.c_str());
}
