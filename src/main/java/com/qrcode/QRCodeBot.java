package com.qrcode;

import com.qrcode.event.QRLoginSolver;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.auth.BotAuthorization;
import net.mamoe.mirai.utils.BotConfiguration;
import net.mamoe.mirai.utils.LoginSolver;

public class QRCodeBot {

    /**
     * 获取扫码登录机器人
     * springboot启动请将SpringApplication.run(BotApplication.class);改成
     * SpringApplicationBuilder builder = new SpringApplicationBuilder(BotApplication.class);
     * builder.headless(false).web(WebApplicationType.NONE).run(args);
     * @param account qq号
     * @return
     */
    public static Bot getQRCodeBot (Long account) {
        // 创建机器人
        BotConfiguration configuration = new BotConfiguration();
        configuration.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_WATCH);
        configuration.setLoginSolver(new QRLoginSolver(LoginSolver.Default));
        // device.json
        configuration.fileBasedDeviceInfo();
        // 下线重新登陆
        configuration.autoReconnectOnForceOffline();
        // 组装扫码登录
        BotAuthorization botAuthorization = BotAuthorization.byQRCode();
        Bot bot = BotFactory.INSTANCE.newBot(account, botAuthorization, configuration);
        configuration.getLoginSolver().createQRCodeLoginListener(bot);
        return bot;
    }

}
