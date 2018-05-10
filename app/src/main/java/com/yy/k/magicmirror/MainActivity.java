package com.yy.k.magicmirror;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("native-lib");
    }

    private static final String TAG = "test";
    //请求码，表明当前activity的身份
    int REQUEST_CODE=1;
    //结果返回码，表明返回数据来自哪个activity
    int RESULT_CODE=22;

    private Switch pingBaoSwitch;

    private String callNumber=null;
    private byte[] phoneNumber;
    private int numberLength;
    private int numberLenthTemp;
    private boolean wenDuSetStatus=false;//温湿度设置按钮第一次按的时候不会改变值，只会显示设定值
    private boolean shiDuSetStatus=false;//此状态为用于判断是否是第一次按

    private short wenDuSetTemp=250;//温度设置缓存，在设置温度时只会改变这个值，跳回温度显示时，这个值会传给setWenDuSet
    private short shiDuSetTemp=500;//湿度设置缓存

    private Button ButStart_shuoshu;
    private Button ButStop_shuoshu;
    private Button ButReset_shuoshu;
    private Button ButStart_mazui;
    private Button ButStop_mazui;
    private Button ButReset_mazui;

    private Button ButDown_wendu;
    private Button ButUp_wendu;
    private TextView tv_WenduDispay;
    private Button ButDown_shidu;
    private Button ButUp_shidu;
    private TextView tv_ShiduDispay;
    private Button ButDown_Yacha;
    private Button ButUp_yacha;
    private TextView tv_YaChaDispay;
    private Button ButJizu_start_stop;

    private Button ButZhiban_start_stop;
    private Button ButFuya_start_stop;
    private Button ButJizhuyunxing_led;
    private Button ButZhibanyunxing_led;
    private Button ButFuyayunxing_led;
    private Button ButJizhuGuzhang_led;
    private Button ButGaoXiao_led;
    private TextView Telephone_display;
    private Button ButBoHao_1;
    private Button ButBoHao_2;
    private Button ButBoHao_3;
    private Button ButBoHao_4;
    private Button ButBoHao_5;
    private Button ButBoHao_6;
    private Button ButBoHao_7;
    private Button ButBoHao_8;
    private Button ButBoHao_9;
    private Button ButBoHao_xinghao;
    private Button ButBoHao_jinghao;
    private Button ButBoHao_0;
    private Button ButMianTi;
    private Button ButDuiJiang;
    private Button ButMusic_start_stop;
    private Button ButMusic_dizeng;
    private Button ButMusic_dijian;
    private Button ButContacts;
    /***
     * 氧气
     */
    private Button ButOxygen_Display_normal;
    private Button ButOxygen_Display_under;
    private Button ButOxygen_Display_over;


    /***
     * 笑气
     */
    private Button ButLaughingGas_Display_normal;
    private Button ButLaughingGas_Display_under;
    private Button ButLaughingGas_Display_over;
    /***
     * 氩气
     */
    private Button ButArgonGas_Display_normal;
    private Button ButArgonGas_Display_under;
    private Button ButArgonGas_Display_over;

    /***
     * 氮气
     */
    private Button ButNitrogenGas_Display_normal;
    private Button ButNitrogenGas_Display_under;
    private Button ButNitrogenGas_Display_over;
    /***
     * 负压
     */
    private Button ButNegativePressure_Display_normal;
    private Button ButNegativePressure_Display_under;
    private Button ButNegativePressure_Display_over;
    /***
     * 压缩空气
     */
    private Button ButPressAirGas_Display_normal;
    private Button ButPressAirGas_Display_under;
    private Button ButPressAirGas_Display_over;
    /***
     * 二氧化碳
     */
    private Button ButCarbon_Display_normal;
    private Button ButCarbon_Display_under;
    private Button ButCarbon_Display_over;

    private Button ButLightling_1;
    private Button ButLightling_2;
    private Button ButShadowless_Lamp;//无影灯
    private Button ButIntraoperative_Lamp;//术中灯
    private Button But_OfLightThe_Lamp;//光片灯
    private Button ButPrepare;//备用
    private Button ButErasure;//消音

    private int jiZuQiTingCount = 0;
    private int zhiBanQiTingCount = 0;
    private int fuYaQiTingCount = 0;

    private int shoushu_sec = 0;
    private int shoushu_minue = 0;
    private int shoushu_hour = 0;
    private int mazui_sec = 0;
    private int mazui_minue = 0;
    private int mazui_hour = 0;
    private int shoushu_temp = 0;
    private int mazui_temp = 0;
    private String beijing;
    private TextView tv_BeiJing;
    private TextView tv_ShouShu;
    private TextView tv_MaZui;
    private TextView tv_Calendar;
    private SimpleDateFormat df;
    private SimpleDateFormat df_data;
    private short setWenDu = 250;
    private short setShiDu = 500;
    private short setYaCha = 500;
    private int music_dongtai_temp;
    private short music_UpDown;
    private int ButLightling_1_variabe = 1;
    private int ButLightling_2_variabe = 1;
    private int ButShadowless_Lamp_variabe = 1;//无影灯
    private int ButIntraoperative_Lamp_variabe = 1;//术中灯
    private int But_OfLightThe_Lamp_variabe = 1;//光片灯
    private int ButPrepare_variabe = 1;//备用
    private int ButErasure_variabe = 1;//消音
    private int wendu_DisplaySet_Change = 0;
    private int shidu_DisplaySet_Change = 0;
    private int yacha_DisplaySet_Change = 0;

    Timer timer1 = new Timer();
    Timer timer2 = new Timer();
    Timer timer4 = new Timer();

    private int musicValue=8;

    TimerTask task1;
    TimerTask task2;
    TimerTask task4;

    Intent intent = new Intent();

    SharedPreferences sharedPreferences;
    SharedPreferences sharedSlaveAdd;

    SharedPreferences.Editor editor;

    String data;

    Modbus_Slav modbus_salve = new Modbus_Slav();
    Modbus_Slav1 modbus_save_1 = new Modbus_Slav1();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        sharedPreferences = getSharedPreferences("ljq", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
        sharedSlaveAdd=this.getSharedPreferences("slaveAdd", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);

        modbus_salve.start();
      //  modbus_save_1.start();
        InitView();
        initOnTouchListener();

        df = new SimpleDateFormat("HH:mm:ss");
        df_data = new SimpleDateFormat("yyyy年MM月dd日     EE");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (timer1!=null){
            if (task1!=null){
                task1.cancel();
            }
        }
        task1=new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {      // UI thread


                    public void run() {

                        String shoushu_secc;
                        String shoushu_minuec;
                        String shoushu_hourc;

                        //机组起停，值班起停，负压起停的脉冲模式
                        //按下相应按键，跳变到高电平，持续7s，跳变回低电平
                        if (modbus_salve.getJiZuStartStop() == 1) {
                            jiZuQiTingCount++;
                        }
                        if (jiZuQiTingCount > 7) {
                            jiZuQiTingCount = 0;
                            modbus_salve.setJiZuStartStop((short) 0);
                        }

                        if (modbus_salve.getZhiBanStartStop() == 1) {
                            zhiBanQiTingCount++;
                        }
                        if (zhiBanQiTingCount > 7) {
                            zhiBanQiTingCount = 0;
                            modbus_salve.setZhiBanStartStop((short) 0);
                        }

                        if (modbus_salve.getFuYaStartStop() == 1) {
                            fuYaQiTingCount++;
                        }
                        if (fuYaQiTingCount > 7) {
                            fuYaQiTingCount = 0;
                            modbus_salve.setFuYaStartStop((short) 0);
                        }


                        beijing = df.format(new Date());
                        data = df_data.format(new Date());
                        tv_BeiJing.setText(beijing);
                        tv_Calendar.setText(data);

                        if (shoushu_temp == 1) {
                            shoushu_sec++;
                            if (shoushu_sec >= 60) {
                                shoushu_sec = 0;
                                shoushu_minue++;
                            }
                            if (shoushu_minue >= 60) {
                                shoushu_minue = 0;
                                shoushu_hour++;
                            }

                            if (shoushu_hour >= 24)
                                shoushu_hour = 0;

                        }
                        shoushu_secc = "0" + shoushu_sec;
                        shoushu_minuec = "0" + shoushu_minue;
                        shoushu_hourc = "0" + shoushu_hour;
                        tv_ShouShu.setText(shoushu_hourc.substring(shoushu_hourc.length() - 2, shoushu_hourc.length()) + ":" + shoushu_minuec.substring(shoushu_minuec.length() - 2, shoushu_minuec.length()) + ":" + shoushu_secc.substring(shoushu_secc.length() - 2, shoushu_secc.length()));
                        String mazui_secc;
                        String mazui_minuec;
                        String mazui_hourc;
                        if (mazui_temp == 1) {
                            mazui_sec++;
                            if (mazui_sec >= 60) {
                                mazui_sec = 0;
                                mazui_minue++;
                            }
                            if (mazui_minue >= 60) {
                                mazui_minue = 0;
                                mazui_hour++;
                            }

                            if (mazui_hour >= 24)
                                mazui_hour = 0;

                        }
                        mazui_secc = "0" + mazui_sec;
                        mazui_minuec = "0" + mazui_minue;
                        mazui_hourc = "0" + mazui_hour;
                        tv_MaZui.setText(mazui_hourc.substring(mazui_hourc.length() - 2, mazui_hourc.length()) + ":" + mazui_minuec.substring(mazui_minuec.length() - 2, mazui_minuec.length()) + ":" + mazui_secc.substring(mazui_secc.length() - 2, mazui_secc.length()));
                    }
                });
            }
        };
        timer1.schedule(task1, 1000, 1000);

        if (timer4!=null){
            if (task4!=null){
                task4.cancel();
            }
        }
        task4=new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {      // UI thread

                    public void run() {
                        callPhone();
                    /*
                    music_dongtai_temp++;
                    if (music_dongtai_temp > 3) {
                        music_dongtai_temp = 1;
                    }
                    switch (music_dongtai_temp) {
                        case 1:
                            ButMusic_dongTai.setBackgroundResource(R.drawable.music_display2);
                            break;
                        case 2:
                            ButMusic_dongTai.setBackgroundResource(R.drawable.music_display1);
                            break;
                        case 3:
                            ButMusic_dongTai.setBackgroundResource(R.drawable.music_display3);
                            break;
                        default:
                            break;
                    }
*/
                        if (modbus_save_1.getYangQiChaoYaValue()==1){
                            ButOxygen_Display_normal.setBackgroundResource(R.drawable.init_ing);
                            ButOxygen_Display_under.setBackgroundResource(R.drawable.init_ing);
                            ButOxygen_Display_over.setBackgroundResource(R.drawable.qitichaoya);
                        }else if(modbus_save_1.getyangQiQianYa()==1){
                            ButOxygen_Display_normal.setBackgroundResource(R.drawable.init_ing);
                            ButOxygen_Display_under.setBackgroundResource(R.drawable.qitiqianya);
                            ButOxygen_Display_over.setBackgroundResource(R.drawable.init_ing);
                        }else {
                            ButOxygen_Display_normal.setBackgroundResource(R.drawable.qitizhengchang);
                            ButOxygen_Display_under.setBackgroundResource(R.drawable.init_ing);
                            ButOxygen_Display_over.setBackgroundResource(R.drawable.init_ing);
                        }

                        if (modbus_save_1.getYaSuoKongQiChaoYa()==1){
                            ButPressAirGas_Display_normal.setBackgroundResource(R.drawable.init_ing);
                            ButPressAirGas_Display_under.setBackgroundResource(R.drawable.init_ing);
                            ButPressAirGas_Display_over.setBackgroundResource(R.drawable.qitichaoya);
                        }else if(modbus_save_1.getYaSUoKongQiQianYa()==1){
                            ButPressAirGas_Display_normal.setBackgroundResource(R.drawable.init_ing);
                            ButPressAirGas_Display_under.setBackgroundResource(R.drawable.qitiqianya);
                            ButPressAirGas_Display_over.setBackgroundResource(R.drawable.init_ing);
                        }else{
                            ButPressAirGas_Display_normal.setBackgroundResource(R.drawable.qitizhengchang);
                            ButPressAirGas_Display_under.setBackgroundResource(R.drawable.init_ing);
                            ButPressAirGas_Display_over.setBackgroundResource(R.drawable.init_ing);
                        }

                        if(modbus_save_1.getXiaoQiChaoYa()==1){
                            ButLaughingGas_Display_normal.setBackgroundResource(R.drawable.init_ing);
                            ButLaughingGas_Display_under.setBackgroundResource(R.drawable.init_ing);
                            ButLaughingGas_Display_over.setBackgroundResource(R.drawable.qitichaoya);
                        }else if(modbus_save_1.getXiaoQiQianYa()==1){
                            ButLaughingGas_Display_normal.setBackgroundResource(R.drawable.init_ing);
                            ButLaughingGas_Display_under.setBackgroundResource(R.drawable.qitiqianya);
                            ButLaughingGas_Display_over.setBackgroundResource(R.drawable.init_ing);
                        }else {
                            ButLaughingGas_Display_normal.setBackgroundResource(R.drawable.qitizhengchang);
                            ButLaughingGas_Display_under.setBackgroundResource(R.drawable.init_ing);
                            ButLaughingGas_Display_over.setBackgroundResource(R.drawable.init_ing);
                        }

                        if (modbus_save_1.getErYangHuaYanChaoYa()==1){
                            ButCarbon_Display_normal.setBackgroundResource(R.drawable.init_ing);
                            ButCarbon_Display_under.setBackgroundResource(R.drawable.init_ing);
                            ButCarbon_Display_over.setBackgroundResource(R.drawable.qitichaoya);
                        }else if (modbus_save_1.getErYangHuaTanQianYa()==1){
                            ButCarbon_Display_normal.setBackgroundResource(R.drawable.init_ing);
                            ButCarbon_Display_under.setBackgroundResource(R.drawable.qitiqianya);
                            ButCarbon_Display_over.setBackgroundResource(R.drawable.init_ing);
                        }else {
                            ButCarbon_Display_normal.setBackgroundResource(R.drawable.qitizhengchang);
                            ButCarbon_Display_under.setBackgroundResource(R.drawable.init_ing);
                            ButCarbon_Display_over.setBackgroundResource(R.drawable.init_ing);
                        }

                        if (modbus_save_1.getFuYaXiYinChaoYa()==1){
                            ButNegativePressure_Display_normal.setBackgroundResource(R.drawable.init_ing);
                            ButNegativePressure_Display_under.setBackgroundResource(R.drawable.init_ing);
                            ButNegativePressure_Display_over.setBackgroundResource(R.drawable.qitichaoya);
                        }else if (modbus_save_1.getFuYaXiYinQianYa()==1){
                            ButNegativePressure_Display_normal.setBackgroundResource(R.drawable.init_ing);
                            ButNegativePressure_Display_under.setBackgroundResource(R.drawable.qitiqianya);
                            ButNegativePressure_Display_over.setBackgroundResource(R.drawable.init_ing);
                        }else {
                            ButNegativePressure_Display_normal.setBackgroundResource(R.drawable.qitizhengchang);
                            ButNegativePressure_Display_under.setBackgroundResource(R.drawable.init_ing);
                            ButNegativePressure_Display_over.setBackgroundResource(R.drawable.init_ing);
                        }

                        if (modbus_save_1.getYaQiChaoYa()==1){
                            ButArgonGas_Display_normal.setBackgroundResource(R.drawable.init_ing);
                            ButArgonGas_Display_under.setBackgroundResource(R.drawable.init_ing);
                            ButArgonGas_Display_over.setBackgroundResource(R.drawable.qitichaoya);
                        }else if (modbus_save_1.getYaQiQianYa()==1){
                            ButArgonGas_Display_normal.setBackgroundResource(R.drawable.init_ing);
                            ButArgonGas_Display_under.setBackgroundResource(R.drawable.qitiqianya);
                            ButArgonGas_Display_over.setBackgroundResource(R.drawable.init_ing);
                        }else {
                            ButArgonGas_Display_normal.setBackgroundResource(R.drawable.qitizhengchang);
                            ButArgonGas_Display_under.setBackgroundResource(R.drawable.init_ing);
                            ButArgonGas_Display_over.setBackgroundResource(R.drawable.init_ing);
                        }

                        if (modbus_save_1.getDanQiChaoYa()==1){
                            ButNitrogenGas_Display_normal.setBackgroundResource(R.drawable.init_ing);
                            ButNitrogenGas_Display_under.setBackgroundResource(R.drawable.init_ing);
                            ButNitrogenGas_Display_over.setBackgroundResource(R.drawable.qitichaoya);
                        }else if(modbus_save_1.getDanQiQianYa()==1){
                            ButNitrogenGas_Display_normal.setBackgroundResource(R.drawable.init_ing);
                            ButNitrogenGas_Display_under.setBackgroundResource(R.drawable.qitiqianya);
                            ButNitrogenGas_Display_over.setBackgroundResource(R.drawable.init_ing);
                        }else {
                            ButNitrogenGas_Display_normal.setBackgroundResource(R.drawable.qitizhengchang);
                            ButNitrogenGas_Display_under.setBackgroundResource(R.drawable.init_ing);
                            ButNitrogenGas_Display_over.setBackgroundResource(R.drawable.init_ing);
                        }

                        if (modbus_save_1.getGasStatus()!=0){
                            beepOn();
                        }else {
                            beepOff();
                        }
                    }

                });
            }
        };
        timer4.schedule(task4, 500 , 500);


        if (timer2!=null){
            if (task2!=null){
                task2.cancel();
            }
        }
        task2=new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {      // UI thread

                    public void run() {

                        String SwenduDislay_bai;
                        String SwenduDislay_shi;
                        String SwenduDislay_ge;
                        String Swenduset_bai;
                        String Swenduset_shi;
                        String Swenduset_ge;
                    /*
                    Swenduset_bai="0"+setWenDu/100;
                    Swenduset_shi="0"+setWenDu/10%10;
                    Swenduset_ge="0"+setWenDu%10;
                    */
                    /*
                    Swenduset_bai = "0" + modbus_salve.getWenDuSet() / 100;
                    Swenduset_shi = "0" + modbus_salve.getWenDuSet() / 10 % 10;
                    Swenduset_ge = "0" + modbus_salve.getWenDuSet() % 10;
                    */

                        Swenduset_bai = "0" + wenDuSetTemp / 100;
                        Swenduset_shi = "0" + wenDuSetTemp / 10 % 10;
                        Swenduset_ge = "0" + wenDuSetTemp % 10;

                        SwenduDislay_bai = "0" + modbus_salve.getWenDu() / 100;
                        SwenduDislay_shi = "0" + modbus_salve.getWenDu() / 10 % 10;
                        SwenduDislay_ge = "0" + modbus_salve.getWenDu() % 10;

                        wendu_DisplaySet_Change++;
/*
                    if (wendu_DisplaySet_Change>33){
                        wenDuSetTemp=modbus_salve.getWenDuSet();
                        wendu_DisplaySet_Change=34;
                        tv_WenduDispay.setText(SwenduDislay_bai.substring(SwenduDislay_bai.length() - 1, SwenduDislay_bai.length()) + SwenduDislay_shi.substring(SwenduDislay_shi.length() - 1, SwenduDislay_shi.length()) + "." + SwenduDislay_ge.substring(SwenduDislay_ge.length() - 1, SwenduDislay_ge.length()));
                    }
                    if (wendu_DisplaySet_Change<=33&&wendu_DisplaySet_Change>=30){
                        modbus_salve.setWenDuSet(wenDuSetTemp);
                        wenDuSetStatus=false;
                        modbus_salve.allowWriteWenDuSet = true;
                    }
                    if (wendu_DisplaySet_Change<30){
                        modbus_salve.allowWriteWenDuSet = false;
                        tv_WenduDispay.setText(Swenduset_bai.substring(Swenduset_bai.length() - 1, Swenduset_bai.length()) + Swenduset_shi.substring(Swenduset_shi.length() - 1, Swenduset_shi.length()) + "." + Swenduset_ge.substring(Swenduset_ge.length() - 1, Swenduset_ge.length()));
                    }
*/


                        if (wendu_DisplaySet_Change < 30) {
                            modbus_salve.allowWriteWenDuSet = false;
                            tv_WenduDispay.setText(Swenduset_bai.substring(Swenduset_bai.length() - 1, Swenduset_bai.length()) + Swenduset_shi.substring(Swenduset_shi.length() - 1, Swenduset_shi.length()) + "." + Swenduset_ge.substring(Swenduset_ge.length() - 1, Swenduset_ge.length())+"℃");
                        } else {
                            tv_WenduDispay.setText(SwenduDislay_bai.substring(SwenduDislay_bai.length() - 1, SwenduDislay_bai.length()) + SwenduDislay_shi.substring(SwenduDislay_shi.length() - 1, SwenduDislay_shi.length()) + "." + SwenduDislay_ge.substring(SwenduDislay_ge.length() - 1, SwenduDislay_ge.length())+"℃");

                            if (wendu_DisplaySet_Change<33){
                                modbus_salve.setWenDuSet(wenDuSetTemp);
                                wenDuSetStatus=false;
                                modbus_salve.allowWriteWenDuSet = true;
                            }else {
                                wenDuSetTemp=modbus_salve.getWenDuSet();
                                wendu_DisplaySet_Change=34;
                            }
                        }

                        String SshiduDislay_bai;
                        String SshiduDislay_shi;
                        String SshiduDislay_ge;
                        String Sshiduset_bai;
                        String Sshiduset_shi;
                        String Sshiduset_ge;
                    /*
                    Sshiduset_bai="0"+setShiDu/100;
                    Sshiduset_shi="0"+setShiDu/10%10;
                    Sshiduset_ge="0"+setShiDu%10;
                    */

                    /*
                    Sshiduset_bai = "0" + modbus_salve.getShiDuSet() / 100;
                    Sshiduset_shi = "0" + modbus_salve.getShiDuSet() / 10 % 10;
                    Sshiduset_ge = "0" + modbus_salve.getShiDuSet() % 10;
                    */
                        Sshiduset_bai = "0" + shiDuSetTemp / 100;
                        Sshiduset_shi = "0" + shiDuSetTemp / 10 % 10;
                        Sshiduset_ge = "0" + shiDuSetTemp % 10;

                        SshiduDislay_bai = "0" + modbus_salve.getShiDu() / 100;
                        SshiduDislay_shi = "0" + modbus_salve.getShiDu() / 10 % 10;
                        SshiduDislay_ge = "0" + modbus_salve.getShiDu() % 10;
                        shidu_DisplaySet_Change++;

                        if (shidu_DisplaySet_Change < 30) {
                            modbus_salve.allowWriteShiDuSet = false;
                            tv_ShiduDispay.setText(Sshiduset_bai.substring(Sshiduset_bai.length() - 1, Sshiduset_bai.length()) + Sshiduset_shi.substring(Sshiduset_shi.length() - 1, Sshiduset_shi.length()) + "." + Sshiduset_ge.substring(Sshiduset_ge.length() - 1, Sshiduset_ge.length())+"%");
                        } else {
                            tv_ShiduDispay.setText(SshiduDislay_bai.substring(SshiduDislay_bai.length() - 1, SshiduDislay_bai.length()) + SshiduDislay_shi.substring(SshiduDislay_shi.length() - 1, SshiduDislay_shi.length()) + "." + SshiduDislay_ge.substring(SshiduDislay_ge.length() - 1, SshiduDislay_ge.length())+"%");
                            if (shidu_DisplaySet_Change<33){
                                modbus_salve.setShiDuSet(shiDuSetTemp);
                                shiDuSetStatus=false;
                                modbus_salve.allowWriteShiDuSet = true;
                            }else {
                                shiDuSetTemp=modbus_salve.getShiDuSet();
                                shidu_DisplaySet_Change = 34;
                            }
                        }

                        String SyachaDislay_bai;
                        String SyachaDislay_shi;
                        String SyachaDislay_ge;
                        String Syachaset_bai;
                        String Syachaset_shi;
                        String Syachaset_ge;
                        Syachaset_bai = "0" + setYaCha / 100;
                        Syachaset_shi = "0" + setYaCha / 10 % 10;
                        Syachaset_ge = "0" + setYaCha % 10;
                        SyachaDislay_bai = "0" + modbus_salve.getYaCha() / 100;
                        SyachaDislay_shi = "0" + modbus_salve.getYaCha() / 10 % 10;
                        SyachaDislay_ge = "0" + modbus_salve.getYaCha() % 10;
                        yacha_DisplaySet_Change++;

                        if (yacha_DisplaySet_Change < 30)
                            tv_YaChaDispay.setText(Syachaset_bai.substring(Syachaset_bai.length() - 1, Syachaset_bai.length()) + Syachaset_shi.substring(Syachaset_shi.length() - 1, Syachaset_shi.length()) + "." + Syachaset_ge.substring(Syachaset_ge.length() - 1, Syachaset_ge.length())+"pa");

                        else {
                            tv_YaChaDispay.setText(SyachaDislay_bai.substring(SyachaDislay_bai.length() - 1, SyachaDislay_bai.length()) + SyachaDislay_shi.substring(SyachaDislay_shi.length() - 1, SyachaDislay_shi.length()) + "." + SyachaDislay_ge.substring(SyachaDislay_ge.length() - 1, SyachaDislay_ge.length())+"pa");
                            yacha_DisplaySet_Change = 30;

                        }



		      /*  modbus_salve.setWenDuSet(modbus_save_2.getWenDuSet());
		        modbus_salve.setShiDuSet(modbus_save_2.getShiDuSet());
	     	    modbus_save_2.setWenDu(modbus_salve.getWenDu());
		        modbus_save_2.setShiDu(modbus_salve.getShiDu());
		        modbus_salve.setJiZuStartStop(modbus_save_2.getJiZuStartStop());
		        */

                        if (modbus_salve.getFengJiZhuangTai() == 1) {
                            ButJizhuyunxing_led.setBackgroundResource(R.drawable.running);
                        } else {
                            ButJizhuyunxing_led.setBackgroundResource(R.drawable.init_ing);
                        }


                        if (modbus_salve.getZhiBanZhuangTai() == 1) {
                            ButZhibanyunxing_led.setBackgroundResource(R.drawable.running);
                        } else {
                            ButZhibanyunxing_led.setBackgroundResource(R.drawable.init_ing);
                        }

                        if (modbus_salve.getFuYaZhuangtai() == 1) {
                            ButFuyayunxing_led.setBackgroundResource(R.drawable.running);
                        } else {
                            ButFuyayunxing_led.setBackgroundResource(R.drawable.init_ing);
                        }

                        if (modbus_salve.getFengJiGuZhang() == 1) {
                            ButJizhuGuzhang_led.setBackgroundResource(R.drawable.waring);
                        } else {
                            ButJizhuGuzhang_led.setBackgroundResource(R.drawable.init_ing);
                        }

                        if (modbus_salve.getGaoXiao() == 1) {
                            ButGaoXiao_led.setBackgroundResource(R.drawable.waring);
                        } else {
                            ButGaoXiao_led.setBackgroundResource(R.drawable.init_ing);
                        }

                        editor = sharedPreferences.edit();//获取编辑器

                        editor.putInt("机组状态", modbus_salve.getFengJiZhuangTai());
                        editor.putInt("冷水阀", modbus_salve.getColdWaterValveOpening());
                        editor.putInt("热水阀", modbus_salve.getHotWaterValveOpening());
                        editor.putInt("加湿器", modbus_salve.getHumidifieOpening());
                        editor.putInt("新风温度", modbus_salve.getTheAirTemperature());

                        editor.putInt("上位机心跳监控点", modbus_salve.getUpperComputerHeartBeatMonitoringPoint());
                        editor.putInt("上位机手自动监控点", modbus_salve.getUpperComputerHandAutomaticallyMonitoringPoint());
                        editor.putInt("上位机风机状态监控点", modbus_salve.getUpperComputerFengjiZHuangTaiMonitoringPoint());
                        editor.putInt("上位机盘管低温监控点", modbus_salve.getUpperComputerZhongXiaoMonitoringPoint());
                        editor.putInt("上位机高效报警监控点", modbus_salve.getUpperComputerGaoXiaoMonitoringPoint());
                        editor.putInt("上位机中效报警监控点", modbus_salve.getUpperComputerChuXiaoMonitoringPoint());


                        editor.putInt("上位机电加热1监控点", modbus_salve.getUpperComputerElectricWarmOneMonitoringPoint());
                        editor.putInt("上位机电加热2监控点", modbus_salve.getUpperComputerElectricWarmTwoMonitoringPoint());
                        editor.putInt("上位机电加热3监控点", modbus_salve.getUpperComputerElectricWarmThreeMonitoringPoint());
                        editor.putInt("上位机电加热高温监控点", modbus_salve.getUpperComputerElectricWarmHighTemperatureMonitoringPoint());
                        editor.putInt("上位机风机缺风监控点", modbus_salve.getUpperComputerFengJiQueFengMonitoringPoint());
                        editor.putInt("上位机灭菌监控点", modbus_salve.getUpperComputerSterilizationMonitoringPoint());
                        editor.putInt("上位机风机已启动监控点", modbus_salve.getUpperComputerFengJiStartMonitoringPoint());
                        editor.putInt("上位机排风机已启动监控点", modbus_salve.getUpperComputerPaiFengJiStartMonitoringPoint());
                        editor.putInt("上位机值班已启动监控点", modbus_salve.getUpperComputerZhiBanStartMonitoringPoint());
                        editor.putInt("上位机负压启动监控点", modbus_salve.getUpperComputerFuYaStartMonitoringPoint());
                        editor.putInt("上位机电预热1监控点", modbus_salve.getUpperComputerElectricPreheatOneMonitoringPoint());
                        editor.putInt("上位机电预热2监控点", modbus_salve.getUpperComputerElectricPreheatTwoMonitoringPoint());
                        editor.putInt("上位机电预热3监控点", modbus_salve.getUpperComputerElectricPreheatThreeMonitoringPoint());
                        editor.putInt("上位机电预热高温监控点", modbus_salve.getUpperComputerElectricPreheatHighTemperatureMonitoringPoint());
                        editor.putInt("上位机压缩机1运行监控点", modbus_salve.getUpperComputerCompressorOneStartMonitoringPoint());
                        editor.putInt("上位机压缩机2运行监控点", modbus_salve.getUpperComputerCompressorTwoStartMonitoringPoint());
                        editor.putInt("上位机压缩机3运行监控点", modbus_salve.getUpperComputerCompressorThreeStartMonitoringPoint());
                        editor.putInt("上位机压缩机4运行监控点", modbus_salve.getUpperComputerCompressorFourStartMonitoringPoint());
                        editor.putInt("上位机压缩机1故障监控点", modbus_salve.getUpperComputerCompressorOneBreakdownMonitoringPoint());
                        editor.putInt("上位机压缩机2故障监控点", modbus_salve.getUpperComputerCompressorTwoBreakdownMonitoringPoint());
                        editor.putInt("上位机压缩机3故障监控点", modbus_salve.getUpperComputerCompressorThreeBreakdownMonitoringPoint());
                        editor.putInt("上位机压缩机4故障监控点", modbus_salve.getUpperComputerCompressorFourBreakdownMonitoringPoint());
                        editor.putInt("冬夏季", modbus_salve.getWinterInSummer());

                        editor.commit();//提交修改

                        modbus_salve.SLAV_addr= (short) Integer.parseInt(sharedSlaveAdd.getString("从机地址","1"));

                    }

                });
            }
        };
        timer2.schedule(task2, 300, 300);
    }


    public void ButStart_shuoshu(View v) {
        shoushu_temp = 1;
    }

    public void ButStop_shuoshu(View v) {
        shoushu_temp = 0;
    }

    public void ButReset_shuoshu(View v) {
        shoushu_temp = 0;
        shoushu_sec = 0;
        shoushu_minue = 0;
        shoushu_hour = 0;
    }


    public void ButStart_mazui(View v) {
        mazui_temp = 1;
    }

    public void ButStop_mazui(View v) {
        mazui_temp = 0;
    }


    public void ButReset_mazui(View v) {
        mazui_temp = 0;
        mazui_sec = 0;
        mazui_minue = 0;
        mazui_hour = 0;
    }


    /***
     * 温度递减调节
     * @param v
     */

    public void Butwendu_down(View v) {
        if(wenDuSetStatus){
            /*
            if (modbus_salve.getWenDuSet() > 10) {
                modbus_salve.setWenDuSet((short) (modbus_salve.getWenDuSet() - 10));
            }
            */
            if(wenDuSetTemp>10){
                wenDuSetTemp-=10;
            }
        }
        wendu_DisplaySet_Change = 0;
        wenDuSetStatus=true;
    }

    /***
     * 温度递增调节
     * @param v
     */
    public void Butwendu_up(View v) {

        /*if(setWenDu<500)
            setWenDu+=10;
            */
        if(wenDuSetStatus){
            /*
            if (modbus_salve.getWenDuSet() < 500) {
                modbus_salve.setWenDuSet((short) (modbus_salve.getWenDuSet() + 10));
            }
           */
            if(wenDuSetTemp<500){
                wenDuSetTemp+=10;
            }
        }
        wendu_DisplaySet_Change = 0;
        wenDuSetStatus=true;
    }

    /***
     * 湿度递减调节
     * @param v
     */

    public void Butshidu_down(View v) {
        /*
        if(	setShiDu>10)
            setShiDu-=10;
            */
        if(shiDuSetStatus){
            /*
            if (modbus_salve.getShiDuSet() > 10) {
                modbus_salve.setShiDuSet((short) (modbus_salve.getShiDuSet() - 10));
            }
            */
            if(shiDuSetTemp>10){
                shiDuSetTemp-=10;
            }
        }
        shidu_DisplaySet_Change = 0;
        shiDuSetStatus=true;
        // modbus_salve.setShiDuSet(setShiDu);
    }

    /***
     * 湿度递增调节
     * @param v
     */
    public void Butshidu_up(View v) {
        /*
        if(  setShiDu<990)
            setShiDu+=10;
            */
        if(shiDuSetStatus){
            /*
            if (modbus_salve.getShiDuSet() < 990) {
                modbus_salve.setShiDuSet((short) (modbus_salve.getShiDuSet() + 10));
            }
            */
            if(shiDuSetTemp<990){
                shiDuSetTemp+=10;
            }
        }
        shidu_DisplaySet_Change = 0;
        shiDuSetStatus=true;
        // modbus_salve.setShiDuSet(setShiDu);
    }

    /***
     * 压差递减调节
     * @param v
     */
    public void Butyacha_down(View v) {
        if (setYaCha > 10)
            setYaCha -= 10;
        yacha_DisplaySet_Change = 0;
        modbus_salve.setYaChaSet(setYaCha);
    }

    /***
     * 压差递减调节
     * @param v
     */
    public void Butyacha_up(View v) {
        if (setYaCha < 990)
            setYaCha += 10;
        yacha_DisplaySet_Change = 0;

        modbus_salve.setYaChaSet(setYaCha);

    }

    /***
     * 机组起停
     * @param v
     */
    public void Butjizustart_stop(View v) {
        /*  常规按键翻转
        if(modbus_salve.getJiZuStartStop()==1)
        {
            modbus_salve.setJiZuStartStop((short)0);

        }
        else
        {
            modbus_salve.setJiZuStartStop((short)1);
        }
        */

        //脉冲模式
        modbus_salve.setJiZuStartStop((short) 1);

    }

    /***
     * 值班起停
     * @param v
     */
    public void zhibanstart_stop(View v) {
        /*
        if(modbus_salve.getZhiBanStartStop()==1)
        {
            modbus_salve.setZhiBanStartStop((short)0);
        }
        else
        {
            modbus_salve.setZhiBanStartStop((short)1);
        }
        */
        modbus_salve.setZhiBanStartStop((short) 1);
    }

    /***
     * 负压起停
     * @param v
     */

    public void fuyastart_stop(View v) {
        /*
        if( modbus_salve.getFuYaStartStop()==1)
        {
            modbus_salve.setFuYaStartStop((short)0);
        }
        else
        {
            modbus_salve.setFuYaStartStop((short)1);
        }
        */
        modbus_salve.setFuYaStartStop((short) 1);
    }

    /***
     * 拨号1
     * @param v
     */
    public void Butbohao_1(View v) {
        if (Telephone_display.length() < 15)
            Telephone_display.setText(Telephone_display.getText() + "1");
        if(modbus_save_1.getPhone_dial_1()==1){
            modbus_save_1.setPhone_dial_1((short) 0);
        }else {
            modbus_save_1.setPhone_dial_1((short) 1);
        }
    }

    /***
     * 拨号2
     * @param v
     */
    public void Butbohao_2(View v) {
        if (Telephone_display.length() < 15)
            Telephone_display.setText(Telephone_display.getText() + "2");
        if(modbus_save_1.getPhone_dial_2()==1){
            modbus_save_1.setPhone_dial_2((short) 0);
        }else {
            modbus_save_1.setPhone_dial_2((short) 1);
        }
    }

    /***
     * 拨号3
     * @param v
     */
    public void Butbohao_3(View v) {
        if (Telephone_display.length() < 15)
            Telephone_display.setText(Telephone_display.getText() + "3");
        if(modbus_save_1.getPhone_dial_3()==1){
            modbus_save_1.setPhone_dial_3((short) 0);
        }else {
            modbus_save_1.setPhone_dial_3((short) 1);
        }
    }

    /***
     * 拨号4
     * @param v
     */
    public void Butbohao_4(View v) {
        if (Telephone_display.length() < 15)
            Telephone_display.setText(Telephone_display.getText() + "4");
        if(modbus_save_1.getPhone_dial_4()==1){
            modbus_save_1.setPhone_dial_4((short) 0);
        }else {
            modbus_save_1.setPhone_dial_4((short) 1);
        }
    }

    /***
     * 拨号5
     * @param v
     */
    public void Butbohao_5(View v) {
        if (Telephone_display.length() < 15)
            Telephone_display.setText(Telephone_display.getText() + "5");
        if(modbus_save_1.getPhone_dial_5()==1){
            modbus_save_1.setPhone_dial_5((short) 0);
        }else {
            modbus_save_1.setPhone_dial_5((short) 1);
        }
    }


    /***
     * 拨号6
     * @param v
     */
    public void Butbohao_6(View v) {
        if (Telephone_display.length() < 15)
            Telephone_display.setText(Telephone_display.getText() + "6");
        if(modbus_save_1.getPhone_dial_6()==1){
            modbus_save_1.setPhone_dial_6((short) 0);
        }else {
            modbus_save_1.setPhone_dial_6((short) 1);
        }
    }

    /***
     * 拨号7
     * @param v
     */

    public void Butbohao_7(View v) {
        if (Telephone_display.length() < 15)
            Telephone_display.setText(Telephone_display.getText() + "7");
        if(modbus_save_1.getPhone_dial_7()==1){
            modbus_save_1.setPhone_dial_7((short) 0);
        }else {
            modbus_save_1.setPhone_dial_7((short) 1);
        }
    }


    /***
     * 拨号8
     * @param v
     */

    public void Butbohao_8(View v) {
        if (Telephone_display.length() < 15)
            Telephone_display.setText(Telephone_display.getText() + "8");
        if(modbus_save_1.getPhone_dial_8()==1){
            modbus_save_1.setPhone_dial_8((short) 0);
        }else {
            modbus_save_1.setPhone_dial_8((short) 1);
        }
    }


    /***
     * 拨号9
     * @param v
     */
    public void Butbohao_9(View v) {
        if (Telephone_display.length() < 15)
            Telephone_display.setText(Telephone_display.getText() + "9");
        if(modbus_save_1.getPhone_dial_9()==1){
            modbus_save_1.setPhone_dial_9((short) 0);
        }else {
            modbus_save_1.setPhone_dial_9((short) 1);
        }
    }


    /***
     * 拨号*
     * @param v
     */

    public void Butbohaoxing(View v) {
        if (Telephone_display.length() < 15)
            Telephone_display.setText(Telephone_display.getText() + "*");
        if(modbus_save_1.getPhone_dial_miHao()==1){
            modbus_save_1.setPhone_dial_miHao((short) 0);
        }else {
            modbus_save_1.setPhone_dial_miHao((short) 1);
        }
    }

    /***
     * 拨号0
     * @param v
     */

    public void Butbohao_0(View v) {
        if (Telephone_display.length() < 15)
            Telephone_display.setText(Telephone_display.getText() + "0");
        if(modbus_save_1.getPhone_dial_0()==1){
            modbus_save_1.setPhone_dial_0((short) 0);
        }else {
            modbus_save_1.setPhone_dial_0((short) 1);
        }
    }

    /***
     * 拨号#
     * @param v
     */

    public void Butbohaojing(View v) {
        if (Telephone_display.length() < 15)
            Telephone_display.setText(Telephone_display.getText() + "#");
        if(modbus_save_1.getPhone_dial_jingHao()==1){
            modbus_save_1.setPhone_dial_jingHao((short) 0);
        }else {
            modbus_save_1.setPhone_dial_jingHao((short) 1);
        }
    }

    /***
     * 拨号免提
     * @param v
     */

    public void Butbohao(View v) {
        Telephone_display.setText("");
        if(modbus_save_1.getPhone_dial_miantiJian()==1){
            modbus_save_1.setPhone_dial_miantiJian((short) 0);
        }else {
            modbus_save_1.setPhone_dial_miantiJian((short) 1);
        }
    }

    /***
     * 拨号对讲
     * @param v
     */

    public void Butduijiang(View v) {
        if (modbus_save_1.getDuiJiangJian()==1){
            modbus_save_1.setDuiJiangJian((short) 0);
        }else{
            modbus_save_1.setDuiJiangJian((short) 1);
        }

        //       intent.setClass(this, PingBao.class);
        //     startActivity(intent);
    }

    /***
     * 对讲音乐
     * @param v
     */

    public void Butbeijingyinyue(View v) {

        if (modbus_save_1.getBackMusic_upDown()!=0){
            musicValue = modbus_save_1.getBackMusic_upDown();
            modbus_save_1.setBackMusic_upDown((short) 0);
        }else{
            modbus_save_1.setBackMusic_upDown((short) musicValue);
        }
    }

    /***
     * 音乐+
     * @param v
     */

    public void Butyinyuezen(View v) {
        music_UpDown=modbus_save_1.getBackMusic_upDown();
        music_UpDown++;
        if (music_UpDown > 8) {
            music_UpDown = 8;
        }
        modbus_save_1.setBackMusic_upDown(music_UpDown);
    }

    /***
     * 音乐
     * @param v
     */

    public void Butyinyuejian(View v) {
        music_UpDown=modbus_save_1.getBackMusic_upDown();
        music_UpDown--;
        if (music_UpDown < 0) {
            music_UpDown = 0;
        }

        modbus_save_1.setBackMusic_upDown(music_UpDown);
    }

    public void ButContacts(View v){
        intent.setClass(this, Contacts.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    /***
     * 照明1
     * @param v
     */

    public void Butzhaoming_1(View v) {
        if (ButLightling_1_variabe == 1) {
            modbus_save_1.setLightling_1((short) 0);
            ButLightling_1_variabe = 0;
            ButLightling_1.setBackgroundResource(R.drawable.led_off);
        } else {
            ButLightling_1_variabe = 1;
            ButLightling_1.setBackgroundResource(R.drawable.led_on);
            modbus_save_1.setLightling_1((short) 1);
        }
    }

    /***
     * 照明2
     * @param v
     */

    public void Butzhaoming_2(View v) {

        if (ButLightling_2_variabe == 1) {
            modbus_save_1.setLightling_2((short) 0);
            ButLightling_2_variabe = 0;
            ButLightling_2.setBackgroundResource(R.drawable.led_off);

        } else {
            ButLightling_2_variabe = 1;
            ButLightling_2.setBackgroundResource(R.drawable.led_on);
            modbus_save_1.setLightling_2((short) 1);
        }

    }

    /***
     * 无影灯
     * @param v
     */

    public void Butwuyingdeng(View v) {
        if (ButShadowless_Lamp_variabe == 1)//无影灯
        {
            modbus_save_1.setShadowless_Lamp((short) 0);
            ButShadowless_Lamp_variabe = 0;//无影灯
            ButShadowless_Lamp.setBackgroundResource(R.drawable.led_off);
        } else {
            ButShadowless_Lamp_variabe = 1;//无影灯
            ButShadowless_Lamp.setBackgroundResource(R.drawable.led_on);
            modbus_save_1.setShadowless_Lamp((short) 1);
        }

    }

    /***
     * 术中灯
     * @param v
     */

    public void Butshuzhongdeng(View v) {
        if (ButIntraoperative_Lamp_variabe == 1)//术中灯
        {
            modbus_save_1.setIntraoperative_Lamp((short) 0);
            ButIntraoperative_Lamp_variabe = 0;
            ButIntraoperative_Lamp.setBackgroundResource(R.drawable.led_off);
        } else {
            ButIntraoperative_Lamp_variabe = 1;
            ButIntraoperative_Lamp.setBackgroundResource(R.drawable.led_on);
            modbus_save_1.setIntraoperative_Lamp((short) 1);
        }
    }

    /***
     * 观片灯
     * @param v
     */

    public void Butguanpiandeng(View v) {
        if (But_OfLightThe_Lamp_variabe == 1) {
            modbus_save_1.setOfLightThe_Lamp((short) 0);
            But_OfLightThe_Lamp_variabe = 0;
            But_OfLightThe_Lamp.setBackgroundResource(R.drawable.led_off);
        } else {
            But_OfLightThe_Lamp_variabe = 1;
            But_OfLightThe_Lamp.setBackgroundResource(R.drawable.led_on);
            modbus_save_1.setOfLightThe_Lamp((short) 1);
        }
    }


    /***
     * 备用
     * @param v
     */

    public void Butbeiyong(View v) {
        if (ButPrepare_variabe == 1) {
            modbus_save_1.setPrepare((short) 0);
            ButPrepare_variabe = 0;
            ButPrepare.setBackgroundResource(R.drawable.led_off);

        } else {
            ButPrepare_variabe = 1;
            ButPrepare.setBackgroundResource(R.drawable.led_on);
            modbus_save_1.setPrepare((short) 1);
        }
    }

    /***
     * 消音
     * @param v
     */

    public void Butxiaoyin(View v) {
        if (ButErasure_variabe == 1) {
            modbus_save_1.setErasure((short) 0);
            ButErasure_variabe = 0;
            ButErasure.setBackgroundResource(R.drawable.jingyin_press);
        } else {
            ButErasure_variabe = 1;
            ButErasure.setBackgroundResource(R.drawable.jingyin);
            modbus_save_1.setErasure((short) 1);
        }
    }

    public void loginInto(View v) {

        intent.setClass(this, UnitMonitoringDataActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (1==requestCode){
            if (resultCode==RESULT_CODE){
                mianTiStart();
                callNumber=data.getStringExtra("data");
                Log.d("test", "onActivityResult: "+callNumber);
                phoneNumber=callNumber.getBytes();
                numberLength=phoneNumber.length+2;
                numberLenthTemp=numberLength;

            }
        }
    }

    private void callPhone(){
        if (numberLength>0){
            if (numberLenthTemp-numberLength<2){
                numberLength--;
                return;
            }
            switch (phoneNumber[numberLenthTemp-numberLength-2]-48){
                case 0:{
                    if (Telephone_display.length() < 15)
                        Telephone_display.setText(Telephone_display.getText() + "0");
                    if(modbus_save_1.getPhone_dial_0()==1){
                        modbus_save_1.setPhone_dial_0((short) 0);
                    }else {
                        modbus_save_1.setPhone_dial_0((short) 1);
                    }
                    break;
                }
                case 1:{
                    if (Telephone_display.length() < 15)
                        Telephone_display.setText(Telephone_display.getText() + "1");
                    if(modbus_save_1.getPhone_dial_1()==1){
                        modbus_save_1.setPhone_dial_1((short) 0);
                    }else {
                        modbus_save_1.setPhone_dial_1((short) 1);
                    }
                    break;
                }
                case 2:{
                    if (Telephone_display.length() < 15)
                        Telephone_display.setText(Telephone_display.getText() + "2");
                    if(modbus_save_1.getPhone_dial_2()==1){
                        modbus_save_1.setPhone_dial_2((short) 0);
                    }else {
                        modbus_save_1.setPhone_dial_2((short) 1);
                    }
                    break;
                }
                case 3:{
                    if (Telephone_display.length() < 15)
                        Telephone_display.setText(Telephone_display.getText() + "3");
                    if(modbus_save_1.getPhone_dial_3()==1){
                        modbus_save_1.setPhone_dial_3((short) 0);
                    }else {
                        modbus_save_1.setPhone_dial_3((short) 1);
                    }
                    break;
                }
                case 4:{
                    if (Telephone_display.length() < 15)
                        Telephone_display.setText(Telephone_display.getText() + "4");
                    if(modbus_save_1.getPhone_dial_4()==1){
                        modbus_save_1.setPhone_dial_4((short) 0);
                    }else {
                        modbus_save_1.setPhone_dial_4((short) 1);
                    }
                    break;
                }
                case 5:{
                    if (Telephone_display.length() < 15)
                        Telephone_display.setText(Telephone_display.getText() + "5");
                    if(modbus_save_1.getPhone_dial_5()==1){
                        modbus_save_1.setPhone_dial_5((short) 0);
                    }else {
                        modbus_save_1.setPhone_dial_5((short) 1);
                    }
                    break;
                }
                case 6:{
                    if (Telephone_display.length() < 15)
                        Telephone_display.setText(Telephone_display.getText() + "6");
                    if(modbus_save_1.getPhone_dial_6()==1){
                        modbus_save_1.setPhone_dial_6((short) 0);
                    }else {
                        modbus_save_1.setPhone_dial_6((short) 1);
                    }
                    break;
                }
                case 7:{
                    if (Telephone_display.length() < 15)
                        Telephone_display.setText(Telephone_display.getText() + "7");
                    if(modbus_save_1.getPhone_dial_7()==1){
                        modbus_save_1.setPhone_dial_7((short) 0);
                    }else {
                        modbus_save_1.setPhone_dial_7((short) 1);
                    }
                    break;
                }
                case 8:{
                    if (Telephone_display.length() < 15)
                        Telephone_display.setText(Telephone_display.getText() + "8");
                    if(modbus_save_1.getPhone_dial_8()==1){
                        modbus_save_1.setPhone_dial_8((short) 0);
                    }else {
                        modbus_save_1.setPhone_dial_8((short) 1);
                    }
                    break;
                }
                case 9:{
                    if (Telephone_display.length() < 15)
                        Telephone_display.setText(Telephone_display.getText() + "9");
                    if(modbus_save_1.getPhone_dial_9()==1){
                        modbus_save_1.setPhone_dial_9((short) 0);
                    }else {
                        modbus_save_1.setPhone_dial_9((short) 1);
                    }
                    break;
                }
            }
            numberLength--;
        }
    }

    public void mianTiStart(){
        Telephone_display.setText("");
        if(modbus_save_1.getPhone_dial_miantiJian()==1){
            modbus_save_1.setPhone_dial_miantiJian((short) 0);
        }else {
            modbus_save_1.setPhone_dial_miantiJian((short) 1);
        }
    }

    public void timeset(View view) {
    }

    private void beepOn(){
        if (ButErasure_variabe==1){
            modbus_save_1.setErasure((short) 1);
        }
    }
    private void beepOff(){
        modbus_save_1.setErasure((short) 0);
    }
    private void initOnTouchListener(){
        ButStart_shuoshu.setOnTouchListener(new View.OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.start_down);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.start_up);
                }

                return false;
            }
        });


        ButStop_shuoshu.setOnTouchListener(new View.OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.stop_down);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.stop_up);
                }

                return false;
            }
        });


        ButReset_shuoshu.setOnTouchListener(new View.OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.reset_down);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.reset_up);
                }


                return false;
            }
        });


        ButStart_mazui.setOnTouchListener(new View.OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.start_down);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.start_up);
                }


                return false;
            }
        });


        ButStop_mazui.setOnTouchListener(new View.OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.stop_down);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.stop_up);
                }


                return false;
            }
        });


        ButReset_mazui.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.reset_down);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.reset_up);
                }

                return false;
            }
        });

        ButDown_wendu.setOnTouchListener(new View.OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.dijian_down);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.up_dijian);
                }


                return false;
            }
        });


        ButUp_wendu.setOnTouchListener(new View.OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.dizeng_down);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.up_dizeng);
                }


                return false;
            }
        });


        ButDown_shidu.setOnTouchListener(new View.OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.dijian_down);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.up_dijian);
                }


                return false;
            }
        });


        ButUp_shidu.setOnTouchListener(new View.OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.dizeng_down);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.up_dizeng);
                }


                return false;
            }
        });


        ButDown_Yacha.setOnTouchListener(new View.OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.dijian_down);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.up_dijian);
                }


                return false;
            }
        });


        ButUp_yacha.setOnTouchListener(new View.OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.dizeng_down);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.up_dizeng);
                }


                return false;
            }
        });

        ButJizu_start_stop.setOnTouchListener(new View.OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.jizustart_down);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.jizustart_up);
                }


                return false;
            }
        });

        ButZhiban_start_stop.setOnTouchListener(new View.OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.zhiban_down);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.zhiban_up);

                }
                return false;
            }
        });


        ButFuya_start_stop.setOnTouchListener(new View.OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.fuya_down);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.fuya_up);

                }
                return false;
            }
        });


        ButBoHao_1.setOnTouchListener(new View.OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.bohao1_press);
                    // modbus_save_1.setPhone_dial_1((short) 1);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.up_1);
                    // modbus_save_1.setPhone_dial_1((short) 0);
                }
                return false;
            }
        });


        ButBoHao_2.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.bohao2_press);
                    // modbus_save_1.setPhone_dial_2((short) 1);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.up_2);
                    // modbus_save_1.setPhone_dial_2((short) 0);
                }
                return false;
            }
        });


        ButBoHao_3.setOnTouchListener(new View.OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.bohao3_press);
                    //  modbus_save_1.setPhone_dial_3((short) 1);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.up_3);
                    //  modbus_save_1.setPhone_dial_3((short) 0);
                }
                return false;
            }
        });


        ButBoHao_4.setOnTouchListener(new View.OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.bohao4_press);
                    // modbus_save_1.setPhone_dial_4((short) 1);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.up_4);
                    //  modbus_save_1.setPhone_dial_4((short) 0);
                }
                return false;
            }
        });


        ButBoHao_5.setOnTouchListener(new View.OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.bohao5_press);
                    // modbus_save_1.setPhone_dial_5((short) 1);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.up_5);
                    // modbus_save_1.setPhone_dial_5((short) 0);
                }
                return false;
            }
        });


        ButBoHao_6.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.bohao6_press);
                    //   modbus_save_1.setPhone_dial_6((short) 1);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.up_6);
                    //   modbus_save_1.setPhone_dial_6((short) 0);
                }
                return false;
            }
        });


        ButBoHao_7.setOnTouchListener(new View.OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.bohao7_press);
                    //    modbus_save_1.setPhone_dial_7((short) 1);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.up_7);
                    //   modbus_save_1.setPhone_dial_7((short) 0);
                }
                return false;
            }
        });


        ButBoHao_8.setOnTouchListener(new View.OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.bohao8_press);
                    //   modbus_save_1.setPhone_dial_8((short) 1);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.up_8);
                    //   modbus_save_1.setPhone_dial_8((short) 0);
                }
                return false;
            }
        });


        ButBoHao_9.setOnTouchListener(new View.OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.bohao9_press);
                    //   modbus_save_1.setPhone_dial_9((short) 1);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.up_9);
                    //   modbus_save_1.setPhone_dial_9((short) 0);
                }
                return false;
            }
        });


        ButBoHao_xinghao.setOnTouchListener(new View.OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.bohaoxinghao_press);
                    //    modbus_save_1.setPhone_dial_miHao((short) 1);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.xing_up);
                    //   modbus_save_1.setPhone_dial_miHao((short) 0);
                }
                return false;
            }
        });


        ButBoHao_jinghao.setOnTouchListener(new View.OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.bohaojinghao_press);
                    //   modbus_save_1.setPhone_dial_jingHao((short) 1);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.up_jinghao);
                    //   modbus_save_1.setPhone_dial_jingHao((short) 0);
                }
                return false;
            }
        });


        ButBoHao_0.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.bohao0_press);
                    //   modbus_save_1.setPhone_dial_0((short) 1);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.up_0);
                    //   modbus_save_1.setPhone_dial_0((short) 0);
                }
                return false;
            }
        });


        ButMianTi.setOnTouchListener(new View.OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.phone_down);
                    //      modbus_save_1.setPhone_dial_miantiJian((short) 1);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.phone_up);
                    //      modbus_save_1.setPhone_dial_miantiJian((short) 0);
                }
                return false;
            }
        });


        ButDuiJiang.setOnTouchListener(new View.OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.duijiang_down);
                    // modbus_save_1.setDuiJiangJian((short) 1);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.duijiang_up);
                    // modbus_save_1.setDuiJiangJian((short) 0);
                }


                return false;
            }
        });


        ButMusic_start_stop.setOnTouchListener(new View.OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.music_down);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.music_up);

                }

                return false;
            }
        });


        ButMusic_dizeng.setOnTouchListener(new View.OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.musicup_down);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.musicup_up);

                }
                return false;
            }
        });

        ButMusic_dijian.setOnTouchListener(new View.OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.musicdown_down);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.musicdown_up);
                }
                return false;
            }
        });

        ButContacts.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    view.setBackgroundResource(R.drawable.contacts_down);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.setBackgroundResource(R.drawable.contacts_up);
                }
                return false;
            }
        });
    }
    private void InitView(){
        ButStart_shuoshu = (Button) findViewById(R.id.shuoshu_start_id);
        ButStop_shuoshu = (Button) findViewById(R.id.shuoshu_stop_id);
        ButReset_shuoshu = (Button) findViewById(R.id.shuoshu_reset_id);
        ButStart_mazui = (Button) findViewById(R.id.mazui_start_id);
        ButStop_mazui = (Button) findViewById(R.id.mazui_stop_id);
        ButReset_mazui = (Button) findViewById(R.id.mazui_reset_id);
        tv_BeiJing = (TextView) findViewById(R.id.tv_beijing_id);
        tv_ShouShu = (TextView) findViewById(R.id.tv_shoushu_id);
        tv_MaZui = (TextView) findViewById(R.id.tv_mazui_id);
        tv_Calendar = (TextView) findViewById(R.id.tv_calendar);

        ButDown_wendu = (Button) findViewById(R.id.wendu_down_id);
        ButUp_wendu = (Button) findViewById(R.id.wendu_up_id);
        ButDown_shidu = (Button) findViewById(R.id.shidu_down_id);
        ButUp_shidu = (Button) findViewById(R.id.shidu_up_id);

        ButDown_Yacha = (Button) findViewById(R.id.yacha_down_id);
        ButUp_yacha = (Button) findViewById(R.id.yacha_up_id);
        ButJizu_start_stop = (Button) findViewById(R.id.jizhustart_stop);
        ButZhiban_start_stop = (Button) findViewById(R.id.zhibanstart_stop);
        ButFuya_start_stop = (Button) findViewById(R.id.fuyastart_stop);

        ButJizhuyunxing_led = (Button) findViewById(R.id.jizuyunxing_led);
        ButZhibanyunxing_led = (Button) findViewById(R.id.zhibanyunxing_led);
        ButFuyayunxing_led = (Button) findViewById(R.id.fuyayunxing_led);
        ButJizhuGuzhang_led = (Button) findViewById(R.id.jizuguzhang_led);
        ButGaoXiao_led = (Button) findViewById(R.id.gaoxiao_led);
        Telephone_display = (TextView) findViewById(R.id.tv_dianhuadisplay_id);
        ButBoHao_1 = (Button) findViewById(R.id.bohao_1_id);
        ButBoHao_2 = (Button) findViewById(R.id.bohao_2_id);
        ButBoHao_3 = (Button) findViewById(R.id.bohao_3_id);
        ButBoHao_4 = (Button) findViewById(R.id.bohao_4_id);
        ButBoHao_5 = (Button) findViewById(R.id.bohao_5_id);
        ButBoHao_6 = (Button) findViewById(R.id.bohao_6_id);
        ButBoHao_7 = (Button) findViewById(R.id.bohao_7_id);
        ButBoHao_8 = (Button) findViewById(R.id.bohao_8_id);
        ButBoHao_9 = (Button) findViewById(R.id.bohao_9_id);
        ButBoHao_xinghao = (Button) findViewById(R.id.bohaoxing_id);
        ButBoHao_jinghao = (Button) findViewById(R.id.bohaojing_id);
        ButBoHao_0 = (Button) findViewById(R.id.bohao_0_id);
        ButMianTi = (Button) findViewById(R.id.bohao_id);
        ButDuiJiang = (Button) findViewById(R.id.duijiang_id);
        ButMusic_start_stop = (Button) findViewById(R.id.beijingyinyue_id);
        ButMusic_dizeng = (Button) findViewById(R.id.yinyuezen_id);
        ButMusic_dijian = (Button) findViewById(R.id.yinyuejian_id);
        ButContacts=findViewById(R.id.bt_contacts);
        //  ButMusic_dongTai = (Button) findViewById(R.id.yinyuedongdai_id);
        tv_WenduDispay = (TextView) findViewById(R.id.tv_wendudisplay_id);
        tv_ShiduDispay = (TextView) findViewById(R.id.tv_shidudisplay_id);
        tv_YaChaDispay = (TextView) findViewById(R.id.tv_yachadisplay_id);


        /***
         * 氧气
         */

        ButOxygen_Display_normal = (Button) findViewById(R.id.yangqi_normal_id);
        ButOxygen_Display_under = (Button) findViewById(R.id.yangqi_under_id);
        ButOxygen_Display_over = (Button) findViewById(R.id.yangqi_over_id);

        /***
         * 笑气
         */

        ButLaughingGas_Display_normal = (Button) findViewById(R.id.xiaoqi_normal_id);
        ButLaughingGas_Display_under = (Button) findViewById(R.id.xiaoqi_under_id);
        ButLaughingGas_Display_over = (Button) findViewById(R.id.xiaoqi_over_id);
        /***
         * 氩气
         */
        ButArgonGas_Display_normal = (Button) findViewById(R.id.yaqi_normal_id);
        ButArgonGas_Display_under = (Button) findViewById(R.id.yaqi_under_id);
        ButArgonGas_Display_over = (Button) findViewById(R.id.yaqi_over_id);

        /***
         * 氮气
         */
        ButNitrogenGas_Display_normal = (Button) findViewById(R.id.danqi_normal_id);
        ButNitrogenGas_Display_under = (Button) findViewById(R.id.danqi_under_id);
        ButNitrogenGas_Display_over = (Button) findViewById(R.id.danqi_over_id);


        /***
         * 负压气体
         */
        ButNegativePressure_Display_normal = (Button) findViewById(R.id.fuyaqi_normal_id);
        ButNegativePressure_Display_under = (Button) findViewById(R.id.fuyaqi_under_id);
        ButNegativePressure_Display_over = (Button) findViewById(R.id.fuyaqi_over_id);


        /***
         * 压缩空气
         */
        ButPressAirGas_Display_normal = (Button) findViewById(R.id.yasuoqi_normal_id);
        ButPressAirGas_Display_under = (Button) findViewById(R.id.yasuoqi_under_id);
        ButPressAirGas_Display_over = (Button) findViewById(R.id.yasuoqi_over_id);


        /***
         * 二氧化碳气体
         */
        ButCarbon_Display_normal = (Button) findViewById(R.id.eryanghuatanqi_normal_id);
        ButCarbon_Display_under = (Button) findViewById(R.id.eryanghuatanqi_under_id);
        ButCarbon_Display_over = (Button) findViewById(R.id.eryanghuatanqi_over_id);

        /***
         * 照明1,2
         */
        ButLightling_1 = (Button) findViewById(R.id.zhaoming_1_id);
        ButLightling_2 = (Button) findViewById(R.id.zhaoming_2_id);
        /***
         * 无影灯
         */
        ButShadowless_Lamp = (Button) findViewById(R.id.wuyingdeng_id);
        /***
         * 术中灯
         */
        ButIntraoperative_Lamp = (Button) findViewById(R.id.shuzhongdeng_id);
        /***
         * 观片灯
         */
        But_OfLightThe_Lamp = (Button) findViewById(R.id.guanpiandeng_id);
        /***
         * 备用
         */
        ButPrepare = (Button) findViewById(R.id.beiyong_id);
        /***
         * 消音
         */
        ButErasure = (Button) findViewById(R.id.xiaoyin_id);
    }
}