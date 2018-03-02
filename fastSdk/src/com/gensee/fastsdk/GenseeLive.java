package com.gensee.fastsdk;

import android.content.Context;

import com.gensee.entity.InitParam;
import com.gensee.fastsdk.core.GSFastConfig;
import com.gensee.fastsdk.core.GSLive;
import com.gensee.fastsdk.util.ResManager;
import com.gensee.rtlib.ChatResource;

public class GenseeLive {

	/**
     * @param context 传入的上下文
     * @param isPublishMode
     * 是否是发布端，true=发布端，false=观看端
     * @param domain 设置域名
	 * <p>若一个url为http://test.gensee.com/site/webcast   域名是“test.gensee.com”
	 * 若一个url为http://test.gensee.com/site/training   域名是“test.gensee.com”</p>
     * @param number 直播或点播编号<p>设置对应编号，如果是点播则是点播编号，是直播便是直播编号。
	 * 请注意不要将id理解为编号。
	 * 作用等价于id，但不是id。有id可以不用编号，有编号可以不用id</p>
     * @param loginAccount 站点登录账号
     * <p> 设置站点认证账号 即登录站点的账号
	 * @param loginPwd 站点登录密码
	 * <p> 设置站点认证密码 即登录站点的密码
	 * 可选，如果后台设置直播需要登录或点播需要登录，那么登录密码要正确  且帐号同时也必须填写正确 </p>
     * @param nickName 昵称
     * <p>设置昵称  用于直播间显示或统计   一定要填写</p>
     * @param joinPwd 直播口令
     * <p>设置口令 即直播的保护密码
	 * 可选 如果后台设置了保护密码 请填写对应的口令</p>
     * @param k
     * 第三方认证K值
     * @param serviceType
     * 设置服务类型   webcast站点对应 WEBCAST   training 对应 TRAINING
     */
	
	/**
	 * 加入直播
	 * @param context context 传入的上下文
	 * @param config 发布与观看的配置
	 * @param initParam 加入直播的参数，参考后台的设置
	 */
    public static void startLive(Context context ,GSFastConfig config,InitParam initParam)
    {
    	ChatResource.initChatResource(context.getApplicationContext());
    	ResManager.getIns().init(context);
    	GSLive.getIns().startLive(context, config, initParam);
    }

    public static void initConfiguration(Context context)
    {
        ChatResource.initChatResource(context.getApplicationContext());
    }

}
