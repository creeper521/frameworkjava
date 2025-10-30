import com.bitejiuyeke.biteportalservice.BitePortalServiceApplication;
import com.bitejiuyeke.biteportalservice.user.entity.dto.WechatLoginDTO;
import com.bitejiuyeke.biteportalservice.user.service.IUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedInputStream;
import java.io.Reader;

@SpringBootTest(classes = BitePortalServiceApplication.class)
public class UserControllerTest {

    @Autowired
    private IUserService userService;
    @Test
    void login() {
        WechatLoginDTO wechatLoginDTO = new WechatLoginDTO();
        wechatLoginDTO.setOpenId("123456789");
        Assertions.assertTrue(userService.login(wechatLoginDTO) != null);
    }
}
