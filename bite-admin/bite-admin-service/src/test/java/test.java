import cn.hutool.crypto.digest.DigestUtil;
import com.bitejiuyeke.bitecommoncore.utils.AESUtil;

public class test {
    public static void main(String[] args) {
//        System.out.println(AESUtil.decryptStr("e64c5f44dc95e4ca77d99136ea2c88c6"));
//        System.out.println(AESUtil.decryptStr("15e2b0d3c33891ebb0f1ef609ec419420c20e320ce94c65fbc8c3312448eb225"));
        System.out.println(DigestUtil.sha256Hex("123456"));
    }
}
