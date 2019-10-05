package com.rys.smartrecycler.db.retbean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 创作时间： 2019/6/1 on 下午4:19
 * <p>
 * 描述：广告管理
 * <p>
 * 作者：lwb
 */
@Entity
public class AdvInfo {
    @Id(autoincrement = true)
    private Long id;
    private int advId;//广告版本id
    private String name;//广告名称
    private String img;//图片广告集合
    private String imgPath;//本地广告地址
    private String video;//视频
    private String videoPath;//本地视频地址
    private String start_time;//开始时间
    private String end_time;//结束时间
    private String create_at;//创建时间
    private String update_at;//更新时间
    private int    upflag;//上传标识
    private int    errorTimes;//下载失败次数
    private String lastDownloadImg = "";//上次下载的资源地址
    private int type;
    @Generated(hash = 678717584)
    public AdvInfo(Long id, int advId, String name, String img, String imgPath, String video,
            String videoPath, String start_time, String end_time, String create_at,
            String update_at, int upflag, int errorTimes, String lastDownloadImg, int type) {
        this.id = id;
        this.advId = advId;
        this.name = name;
        this.img = img;
        this.imgPath = imgPath;
        this.video = video;
        this.videoPath = videoPath;
        this.start_time = start_time;
        this.end_time = end_time;
        this.create_at = create_at;
        this.update_at = update_at;
        this.upflag = upflag;
        this.errorTimes = errorTimes;
        this.lastDownloadImg = lastDownloadImg;
        this.type = type;
    }
    public AdvInfo(int advId, String name, String img, String imgPath,
                   String video, String videoPath, String start_time, String end_time,
                   String create_at, String update_at,int upflag, int errorTimes, String lastDownloadImg,
                   int type) {
        this.advId = advId;
        this.name = name;
        this.img = img;
        this.imgPath = imgPath;
        this.video = video;
        this.videoPath = videoPath;
        this.start_time = start_time;
        this.end_time = end_time;
        this.create_at = create_at;
        this.update_at = update_at;
        this.upflag = upflag;
        this.errorTimes = errorTimes;
        this.lastDownloadImg = lastDownloadImg;
        this.type = type;
    }
    @Generated(hash = 1954739720)
    public AdvInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getAdvId() {
        return this.advId;
    }
    public void setAdvId(int advId) {
        this.advId = advId;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImg() {
        return this.img;
    }
    public void setImg(String img) {
        this.img = img;
    }
    public String getImgPath() {
        return this.imgPath;
    }
    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
    public String getVideo() {
        return this.video;
    }
    public void setVideo(String video) {
        this.video = video;
    }
    public String getVideoPath() {
        return this.videoPath;
    }
    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }
    public String getStart_time() {
        return this.start_time;
    }
    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }
    public String getEnd_time() {
        return this.end_time;
    }
    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
    public String getCreate_at() {
        return this.create_at;
    }
    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getUpflag() {
        return this.upflag;
    }
    public void setUpflag(int upflag) {
        this.upflag = upflag;
    }
    public int getErrorTimes() {
        return this.errorTimes;
    }
    public void setErrorTimes(int errorTimes) {
        this.errorTimes = errorTimes;
    }
    public String getLastDownloadImg() {
        return this.lastDownloadImg;
    }
    public void setLastDownloadImg(String lastDownloadImg) {
        this.lastDownloadImg = lastDownloadImg;
    }
    public String getUpdate_at() {
        return this.update_at;
    }
    public void setUpdate_at(String update_at) {
        this.update_at = update_at;
    }



}
