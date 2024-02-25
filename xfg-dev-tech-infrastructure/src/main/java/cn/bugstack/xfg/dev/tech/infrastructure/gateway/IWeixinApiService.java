package cn.bugstack.xfg.dev.tech.infrastructure.gateway;

import cn.bugstack.xfg.dev.tech.infrastructure.gateway.dto.WeixinQrCodeRequestDTO;
import cn.bugstack.xfg.dev.tech.infrastructure.gateway.dto.WeixinQrCodeResponseDTO;
import cn.bugstack.xfg.dev.tech.infrastructure.gateway.dto.WeixinTokenResponseDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 微信API服务
 * @create 2024-02-25 08:58
 */
public interface IWeixinApiService {

    /**
     * 获取 Access token
     * 文档：<a href="https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Get_access_token.html">Get_access_token</a>
     *
     * @param grantType 获取access_token填写client_credential
     * @param appId     第三方用户唯一凭证
     * @param appSecret 第三方用户唯一凭证密钥，即appsecret
     * @return 响应结果
     */
    @GET("cgi-bin/token")
    Call<WeixinTokenResponseDTO> getToken(
            @Query("grant_type") String grantType,
            @Query("appid") String appId,
            @Query("secret") String appSecret
    );

    /**
     * 获取凭据 ticket
     * 文档：<a href="https://developers.weixin.qq.com/doc/offiaccount/Account_Management/Generating_a_Parametric_QR_Code.html">Generating_a_Parametric_QR_Code</a>
     * <a href="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET">前端根据凭证展示二维码</a>
     *
     * @param accessToken            getToken 获取的 token 信息
     * @param weixinQrCodeRequestDTO 入参对象
     * @return 应答结果
     */
    @POST("cgi-bin/qrcode/create")
    Call<WeixinQrCodeResponseDTO> createQrCode(@Query("access_token") String accessToken, @Body WeixinQrCodeRequestDTO weixinQrCodeRequestDTO);

}
