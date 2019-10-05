package com.rys.smartrecycler.tool;

import com.rys.smartrecycler.net.bean.DeskPriceBean;
import com.rys.smartrecycler.net.bean.DeskSatus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * 创建时间：2019/6/18
 * 作者：李伟斌
 * 功能描述:
 */
public class AppTool {

    public static String getTypeName(String type){
        if("1".equals(type)){
            return "bottle";
        }else if("2".equals(type)){
            return "paper";
        }else if("3".equals(type)){
            return "spin";
        }else if("4".equals(type)){
            return "plastic";
        }else if("5".equals(type)){
            return "metal";
        }else if("6".equals(type)){
            return "glass";
        }else{
            return "bottle";
        }
    }
    public static String getTypeCnName(String type){
        if("1".equals(type)){
            return "塑料瓶";
        }else if("2".equals(type)){
            return "纸类";
        }else if("3".equals(type)){
            return "纺织物";
        }else if("4".equals(type)){
            return "生活塑料";
        }else if("5".equals(type)){
            return "金属";
        }else if("6".equals(type)){
            return "玻璃";
        }else{
            return "bottle";
        }
    }
    public static int getTypeValue(String typeName){
        if("bottle".equals(typeName)){
            return 1;
        }else if("paper".equals(typeName)){
            return 2;
        }else if("spin".equals(typeName)){
            return 3;
        }else if("plastic".equals(typeName)){
            return 4;
        }else if("metal".equals(typeName)){
            return 5;
        }else {
            return 6;
        }
    }

    /**
     * 获取对应箱满阀值
     * @param vo
     * @param deskType
     * @return
     */
    public static int getFullMin(DeskSatus vo,String deskType){
        if("bottle".equals(deskType)){
            return vo.getBottle();
        }else if("paper".equals(deskType)){
            return vo.getPaper();
        }else if("spin".equals(deskType)){
            return vo.getSpin();
        }else if("plastic".equals(deskType)){
            return vo.getPlastic();
        }else if("metal".equals(deskType)){
            return vo.getMetal();
        }else {
            return vo.getGlass();
        }
    }
    public static String getPrice(String typeName, DeskPriceBean bean){
        if("bottle".equals(typeName)){
            return bean.getBottle();
        }else if("paper".equals(typeName)){
            return bean.getPaper();
        }else if("spin".equals(typeName)){
            return bean.getSpin();
        }else if("plastic".equals(typeName)){
            return bean.getPlastic();
        }else if("metal".equals(typeName)){
            return bean.getMetal();
        }else {
            return bean.getGlass();
        }
    }

    /**
     * 设置故障，值为十六进制，取值需将十六进制转换成八位二进制 0表示正常 1表示故障
     *
     * 十六进制（00000000）网络状态、投递门状态、回收门状态、电子称状态、烟雾报警状态、主板故障、备用、备用
     * eg:0  (00000000)  无故障
     * eg:40（01000000） 投递门故障
     * eg:30（00110000） 回收们故障、电子秤故障
     *
     * @return
     */
    public static String setError(String oldStatus,int target,String status){
        if(oldStatus.length() != 8){
            oldStatus = "00000000";
        }
        String oldStr[] = oldStatus.split("");
        if(target >0 && target <= 8){
            oldStr[target-1] = status;
        }
        return oldStr[0]+oldStr[1]+oldStr[2]+oldStr[3]+oldStr[4]+oldStr[5]+oldStr[6]+oldStr[7];
    }

    /**
     * 系统重启
     */
    public static void systemReboot(){
        try {
            Process proc;
            proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "reboot "});
            proc.waitFor();
        } catch (IOException e) {
        } catch (InterruptedException e) {
        }
    }

    /**
     * 获取cpu温度
     * @return
     */
    public static String getCpuTemp() {
        String temp = "Unknow";
        BufferedReader br = null;
        FileReader fr = null;
        try {
            File dir = new File("/sys/class/thermal/");
            File[] files = dir.listFiles(file -> {
                if (Pattern.matches("thermal_zone[0-9]+", file.getName())) {
                    return true;
                }
                return false;
            });
            final int SIZE = files.length;
            String line = "";
            String type = "";
            for (int i = 0; i < SIZE; i++) {
                fr = new FileReader("/sys/class/thermal/thermal_zone" + i + "/type");
                br = new BufferedReader(fr);
                line = br.readLine();
                if (line != null) {
                    type = line;
                }

                fr = new FileReader("/sys/class/thermal/thermal_zone" + i + "/temp");
                br = new BufferedReader(fr);
                line = br.readLine();
                if (line != null) {
                    // MTK CPU
                    if (type.contains("cpu")) {
                        long temperature = Long.parseLong(line);
                        if (temperature < 0) {
                            temp = "Unknow";
                        } else {
                            temp = (float) (temperature / 1000.0) + "";
                        }
                    } else if (type.contains("tsens_tz_sensor")) {
                        // Qualcomm CPU
                        long temperature = Long.parseLong(line);
                        if (temperature < 0) {
                            temp = "Unknow";
                        } else if (temperature > 100){
                            temp = (float) (temperature / 10.0) + "";
                        } else {
                            temp = temperature + "";
                        }
                    }

                }
            }

            if (fr != null) {
                fr.close();
            }
            if (br != null) {
                br.close();
            }
        } catch (Exception e) {
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (Exception e) {
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                }
            }
        }

        return temp;
    }
}
