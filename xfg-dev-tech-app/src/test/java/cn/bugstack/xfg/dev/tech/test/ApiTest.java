package cn.bugstack.xfg.dev.tech.test;

import cn.bugstack.xfg.dev.tech.infrastructure.gateway.IWeixinApiService;
import cn.bugstack.xfg.dev.tech.infrastructure.gateway.dto.WeixinQrCodeRequestDTO;
import cn.bugstack.xfg.dev.tech.infrastructure.gateway.dto.WeixinQrCodeResponseDTO;
import cn.bugstack.xfg.dev.tech.infrastructure.gateway.dto.WeixinTokenResponseDTO;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

@Slf4j
public class ApiTest {

    private static final String BASE_URL = "https://api.weixin.qq.com/";

    private IWeixinApiService weixinApiService;

    @Before
    public void init() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        weixinApiService = retrofit.create(IWeixinApiService.class);
    }

    @Test
    public void test_getToken() throws IOException {
        Call<WeixinTokenResponseDTO> call = weixinApiService.getToken("client_credential", "wx5a228ff69e28a91f", "0bea03aa1310bac050aae79dd8703928");
        WeixinTokenResponseDTO weixinTokenResponseDTO = call.execute().body();
        log.info("测试结果：{}", JSON.toJSONString(weixinTokenResponseDTO));
        // access_token 77_GVdT47NxnFXYnEFZBWGY31JS-U1oC-Xud0SIaWH9MY1E8IB_kSMc9HxZUIvKa9XQtrujJcj2tG6D6YFArT6k6xv4WF9LCXTayLX1zgPsxf-JpahmmISIUyNh3lgNKIcAGACZX
    }

    @Test
    public void test_createQrCode() throws IOException {
        String accessToken = "77_GVdT47NxnFXYnEFZBWGY31JS-U1oC-Xud0SIaWH9MY1E8IB_kSMc9HxZUIvKa9XQtrujJcj2tG6D6YFArT6k6xv4WF9LCXTayLX1zgPsxf-JpahmmISIUyNh3lgNKIcAGACZX";
        WeixinQrCodeRequestDTO request = WeixinQrCodeRequestDTO.builder()
                .expire_seconds(2592000)
                .action_name(WeixinQrCodeRequestDTO.ActionNameTypeVO.QR_SCENE.getCode())
                .action_info(WeixinQrCodeRequestDTO.ActionInfo.builder()
                        .scene(WeixinQrCodeRequestDTO.ActionInfo.Scene.builder()
                                .scene_id(100601)
                                // .scene_str("test") 配合 ActionNameTypeVO.QR_STR_SCENE
                                .build())
                        .build())
                .build();

        Call<WeixinQrCodeResponseDTO> qrCodeCall = weixinApiService.createQrCode(accessToken, request);
        WeixinQrCodeResponseDTO weixinQrCodeResponseDTO = qrCodeCall.execute().body();
        log.info("测试结果：{}", JSON.toJSONString(weixinQrCodeResponseDTO));

        // 测试结果；{"expire_seconds":604800,"ticket":"gQHp8TwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAySnRLOG9FTDBjckcxMnRHMnhDMUoAAgSdndplAwQAjScA","url":"http://weixin.qq.com/q/020o80o7L0crG1_WlzxB1O"}
    }

    @Test
    public void test_showqrcode() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQHp8TwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAySnRLOG9FTDBjckcxMnRHMnhDMUoAAgSdndplAwQAjScA")
                .get()
                .build();

        Response response = client.newCall(request).execute();
        log.info("测试结果：{}", response.body());
    }

}
