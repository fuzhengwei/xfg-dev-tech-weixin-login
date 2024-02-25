package cn.bugstack.xfg.dev.tech.infrastructure.adapter;

import cn.bugstack.xfg.dev.tech.domain.adapter.ILoginAdapter;
import cn.bugstack.xfg.dev.tech.infrastructure.gateway.IWeixinApiService;
import cn.bugstack.xfg.dev.tech.infrastructure.gateway.dto.WeixinQrCodeRequestDTO;
import cn.bugstack.xfg.dev.tech.infrastructure.gateway.dto.WeixinQrCodeResponseDTO;
import cn.bugstack.xfg.dev.tech.infrastructure.gateway.dto.WeixinTokenResponseDTO;
import com.google.common.cache.Cache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description
 * @create 2024-02-25 12:03
 */
@Service
public class LoginAdapter implements ILoginAdapter {

    @Value("${weixin.config.app-id}")
    private String appid;
    @Value("${weixin.config.app-secret}")
    private String appSecret;
    @Resource
    private Cache<String, String> weixinAccessToken;
    @Resource
    private IWeixinApiService weixinApiService;

    @Override
    public String createQrCodeTicket() throws IOException {
        // 1. 获取 accessToken 【实际业务场景，按需处理下异常】
        String accessToken = weixinAccessToken.getIfPresent(appid);
        if (null == accessToken){
            Call<WeixinTokenResponseDTO> call = weixinApiService.getToken("client_credential", "wx5a228ff69e28a91f", "0bea03aa1310bac050aae79dd8703928");
            WeixinTokenResponseDTO weixinTokenResponseDTO = call.execute().body();
            accessToken = weixinTokenResponseDTO.getAccess_token();
            weixinAccessToken.put(appid, accessToken);
        }
        // 2. 生成 ticket
        WeixinQrCodeRequestDTO request = WeixinQrCodeRequestDTO.builder()
                .expire_seconds(2592000) // 过期时间单位为秒 2592000 = 30天
                .action_name(WeixinQrCodeRequestDTO.ActionNameTypeVO.QR_SCENE.getCode())
                .action_info(WeixinQrCodeRequestDTO.ActionInfo.builder()
                        .scene(WeixinQrCodeRequestDTO.ActionInfo.Scene.builder()
                                .scene_id(100601) // 场景ID
                                // .scene_str("test") 配合 ActionNameTypeVO.QR_STR_SCENE
                                .build())
                        .build())
                .build();

        Call<WeixinQrCodeResponseDTO> qrCodeCall = weixinApiService.createQrCode(accessToken, request);
        WeixinQrCodeResponseDTO weixinQrCodeResponseDTO = qrCodeCall.execute().body();
        return weixinQrCodeResponseDTO.getTicket();
    }

}
