package com.qrcode.event;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.auth.QRCodeLoginListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class QRLoginListener implements QRCodeLoginListener {

    private Bot tempBot;
    private File tmpFile;

    /** 二维码宽度 **/
    final Integer widthIcon = 200;
    /** 二维码高度 **/
    final Integer heightIcon = 200;

    JFrame jFrame;
    Container container;
    JLabel jl;


    private void updateQRCode(String filePath) {
        if (jFrame != null ) {
            // 为标签设置图片
            this.setIcon(filePath);
        }
    }

    /**
     * byte 转image
     * @param fileName
     * @param bytes
     * @return
     * @throws IOException
     */
    public static String byteImage (String fileName, byte [] bytes) throws IOException {
        fileName = fileName + ".png";
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        OutputStream os = new FileOutputStream(fileName);
        os.write(bytes, 0, bytes.length);
        os.flush();
        os.close();
        return fileName;
    }

    @Override
    public void onFetchQRCode(Bot bot, byte[] data) {
        tempBot = bot;
        try {
            String filePath = byteImage(bot.getId() + "-QRCODE", data);
            updateQRCode(filePath);
            tmpFile = new File(filePath);
            System.out.println("将会在弹出窗口显示二维码图片，请在相似网络环境下使用手机QQ扫码登录。若看不清图片，请查看文件 " + filePath);
            if (jFrame == null) {
                jFrame = new JFrame();
                // 窗口居中
                jFrame.setLocationRelativeTo(null);
                // 监听窗口关闭
                jFrame.addWindowListener(wa);
                // 设置面板
                container = jFrame.getContentPane();
                jl = new JLabel();
                //使标签上的文字居中
                jl.setHorizontalAlignment(SwingConstants.CENTER);
                //设置容器的背景颜色
                container.setBackground(Color.white);
                // 设置名称
                jFrame.setTitle("请扫描登录验证码");
                //设置窗体大小
                jFrame.setSize(widthIcon + 100, heightIcon + 100);
                //设置窗体关闭方式
                //1.不做任何反应。 * 2.仅仅隐藏。 * 3.关闭窗口。 * 4.关闭窗口，并结束所有线程。 * 注意：默认是2，你会发现窗口虽然不见了，但在任务管理器中还存在，这将导致springIoc容器不能自动清理关闭
                jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                // 设置图片
                this.setIcon(filePath);
                // 添加到窗口面板
                container.add(jl);
                //使窗体可视
                jFrame.setVisible(true);
            }
        } catch (Exception e) {
            System.err.println("无法写出二维码图片.");
            e.printStackTrace();
        }
    }

    WindowAdapter wa = new WindowAdapter() {
        //窗口被关闭时的监听
        public void windowClosed(java.awt.event.WindowEvent e) {
            System.err.println("用户关闭登录！");
            tempBot.close();
        };
        //点击窗口关闭按钮监听
        public void windowClosing(java.awt.event.WindowEvent e) {
            jFrame.dispose();
        }
    };

    @Override
    public void onStateChanged(Bot bot, QRCodeLoginListener.State state) {
        tempBot = bot;
        String message = "";
        switch (state) {
            case WAITING_FOR_SCAN:
                message = "等待扫描二维码中";
                break;
            case WAITING_FOR_CONFIRM:
                message = "扫描完成，请在手机 QQ 确认登录";
                break;
            case CANCELLED:
                message = "已取消登录，将会重新获取二维码";
                break;
            case TIMEOUT:
                message = "扫描超时，将会重新获取二维码";
                break;
            case CONFIRMED:
                message = "已确认登录";
                this.qrCodeClose ();
                break;
            default:
                System.err.println("扫码异常");
                tempBot.close();
                this.qrCodeClose();
        }
        System.out.println(message);
    }

    public void qrCodeClose () {
        if (tmpFile != null) {
            try {
                tmpFile.delete();
            } catch (Exception e) {
                System.err.println(e);
            }
        }
        tempBot = null;
        jFrame.setVisible(false);
        jFrame.dispose();
    }

    /**
     * 设置图片
     * @param filePath
     */
    public void setIcon(String filePath) {
        jl.setIcon(null);
        ImageIcon icon = new ImageIcon(filePath);
        icon.setImage(icon.getImage().getScaledInstance(widthIcon, heightIcon,
                Image.SCALE_DEFAULT));
        jl.setIcon(icon);
    }
}
