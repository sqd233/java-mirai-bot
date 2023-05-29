package com.qrcode.event;

import kotlin.coroutines.Continuation;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.auth.QRCodeLoginListener;
import net.mamoe.mirai.utils.DeviceVerificationRequests;
import net.mamoe.mirai.utils.DeviceVerificationResult;
import net.mamoe.mirai.utils.LoginSolver;

public class QRLoginSolver extends LoginSolver {

    private final LoginSolver parentSolver;

    public QRLoginSolver(LoginSolver parentSolver) {
        this.parentSolver = parentSolver;
    }

    public LoginSolver getParentSolver() {
        return this.parentSolver;
    }

    @Override
    public String onSolvePicCaptcha(Bot bot, byte[] data, Continuation<? super String> continuation) {
        return (String) this.parentSolver.onSolvePicCaptcha(bot, data, continuation);
    }

    @Override
    public String onSolveSliderCaptcha(Bot bot, String url, Continuation<? super String> continuation) {
        return (String) this.parentSolver.onSolveSliderCaptcha(bot, url, continuation);
    }

    public boolean isSliderCaptchaSupported() {
        return this.parentSolver.isSliderCaptchaSupported();
    }

    public QRCodeLoginListener createQRCodeLoginListener(Bot bot) {
        System.out.println("YES！");
        return new QRLoginListener();
    }

    public DeviceVerificationResult onSolveDeviceVerification(Bot bot, DeviceVerificationRequests requests, Continuation<? super DeviceVerificationResult> continuation) {
        return (DeviceVerificationResult) this.parentSolver.onSolveDeviceVerification(bot, requests, continuation);
    }

    public String onSolveUnsafeDeviceLoginVerify(Bot bot, String url, Continuation<? super String> continuation) {
        return (String) this.parentSolver.onSolveUnsafeDeviceLoginVerify(bot, url, continuation);
    }
}
