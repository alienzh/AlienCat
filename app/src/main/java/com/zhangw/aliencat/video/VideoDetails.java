package com.zhangw.aliencat.video;

import android.text.TextUtils;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * 视频详情
 *
 * @author xiemy
 * @date 2017/4/30.
 */
public class VideoDetails extends DataSupport implements Serializable {

    private String des;
    /**
     * 1收藏/0没有收藏
     */
    private int favor_flag;
    private int gift_count;
    private int height;
    private String img_url;
    private int like_count;
    private int play_count;
    private int share_count;
    //学习人数
    private int study_count;
    private int comment_count;
    private int timelen;
    private int vid;
    private String video_url;
    private int width;
    private int from_rcmd;//是否来自推荐

    private int sort_id; //视频一级分类id
    private int sort_id3;
    private int sort_label_id;
    private String sort_name;

    /**
     * 关注用户状态
     * （0：未关注，1：已关注，2：互粉）
     */
    private int follow_status;
    private String head;
    private String nick;
    private int pupil_count;
    private int uid;
    private int video_count;
    private int history_usage_linked_my_uid;
    @Column(ignore = true)
    private int impact; //影响力
    @Column(ignore = true)
    private int master_star; //师父星级
    @Column(ignore = true)
    private int dislike_flag; //视频是否被踩
    @Column(ignore = true)
    private int like_flag; //视频是否被点赞
    @Column(ignore = true)
    private int intimacy; //亲密度
    /**
     * 是否需要拜师（先判断该字段，为0时，
     * 则判断price看是否收费，为1时表示需要拜师，返回拜师价格baishi_price）
     * （0：不需要，1：需要）
     */
    @Column(ignore = true)
    private int need_baishi;
    @Column(ignore = true)
    private int baishi_flag;
    @Column(ignore = true)
    private int baishi_price; //拜师需要银元数（0：免费 >0：收费银元数）
    @Column(ignore = true)
    private int price; //观看视频需要银元数（负数已经购买过 0：免费 >0：收费银元数）
    /**
     * 我的体验券余额（need_baishi为1时）
     */
    @Column(ignore = true)
    private int my_ex_tickets;
    /**
     * 我的剩余银元
     * （need_baishi为1时）
     */
    @Column(ignore = true)
    private int silver;

    private long playTimeStamp; //播放视频存放本地时间戳ms

    public int getHistory_usage_linked_my_uid() {
        return history_usage_linked_my_uid;
    }

    public void setHistory_usage_linked_my_uid(int history_usage_linked_my_uid) {
        this.history_usage_linked_my_uid = history_usage_linked_my_uid;
    }

    public int getMaster_star() {
        return master_star;
    }

    public void setMaster_star(int master_star) {
        this.master_star = master_star;
    }

    public int addLikeCount() {
        this.like_count++;
        return like_count;
    }

    public int reductionLikeCount() {
        if (like_count > 0) {
            this.like_count--;
        }
        return like_count;
    }

    public int addCommentCount() {
        this.comment_count++;
        return comment_count;
    }

    public int reductionCommentCount() {
        if (comment_count > 0) {
            this.comment_count--;
        }
        return comment_count;
    }

    public int addShareCount() {
        this.share_count++;
        return share_count;
    }

    public int addGiftCount() {
        this.gift_count++;
        return gift_count;
    }

    public void setSort_label_id(int sort_label_id) {
        this.sort_label_id = sort_label_id;
    }

    public int getSort_label_id() {
        return sort_label_id;
    }

    public long getPlayTimeStamp() {
        return playTimeStamp;
    }

    public void setPlayTimeStamp(long playTimeStamp) {
        this.playTimeStamp = playTimeStamp;
    }

    public int getSort_id() {
        return sort_id;
    }

    public void setSort_id(int sort_id) {
        this.sort_id = sort_id;
    }

    public int getSort_id3() {
        return sort_id3;
    }

    public String getSort_name() {
        return sort_name;
    }

    public void setSort_name(String sort_name) {
        this.sort_name = sort_name;
    }

    public String getDes() {
        return des != null ? des : "";
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getFavor_flag() {
        return favor_flag;
    }

    public void setFavor_flag(int favor_flag) {
        this.favor_flag = favor_flag;
    }

    public int getGift_count() {
        return gift_count;
    }

    public void setGift_count(int gift_count) {
        this.gift_count = gift_count;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public int getPlay_count() {
        return play_count;
    }

    public void setPlay_count(int play_count) {
        this.play_count = play_count;
    }

    public int getShare_count() {
        return share_count;
    }

    public void setShare_count(int share_count) {
        this.share_count = share_count;
    }

    public int getStudy_count() {
        return study_count;
    }

    public void setStudy_count(int study_count) {
        this.study_count = study_count;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public int getTimelen() {
        return timelen;
    }

    public void setTimelen(int timelen) {
        this.timelen = timelen;
    }

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getLike_flag() {
        return like_flag;
    }

    public void setSort_id3(int sort_id3) {
        this.sort_id3 = sort_id3;
    }

    public int getDislike_flag() {
        return dislike_flag;
    }

    public void setDislike_flag(int dislike_flag) {
        this.dislike_flag = dislike_flag;
    }

    public void setLike_flag(int like_flag) {
        this.like_flag = like_flag;
    }

    public int getIntimacy() {
        return intimacy;
    }

    public void setIntimacy(int intimacy) {
        this.intimacy = intimacy;
    }

    public int getNeed_baishi() {
        return need_baishi;
    }

    public void setNeed_baishi(int need_baishi) {
        this.need_baishi = need_baishi;
    }

    public int getBaishi_flag() {
        return baishi_flag;
    }

    public void setBaishi_flag(int baishi_flag) {
        this.baishi_flag = baishi_flag;
    }

    public int getMy_ex_tickets() {
        return my_ex_tickets;
    }

    public void setMy_ex_tickets(int my_ex_tickets) {
        this.my_ex_tickets = my_ex_tickets;
    }

    public int getSilver() {
        return silver;
    }

    public void setSilver(int silver) {
        this.silver = silver;
    }

    public int getBaishi_price() {
        return baishi_price;
    }

    public void setBaishi_price(int baishi_price) {
        this.baishi_price = baishi_price;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getFollow_status() {
        return follow_status;
    }

    public void setFollow_status(int follow_status) {
        this.follow_status = follow_status;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public int getImpact() {
        return impact;
    }

    public void setImpact(int impact) {
        this.impact = impact;
    }

    public String getNick() {
        if (TextUtils.isEmpty(nick)) {
            return "";
        }
        return nick;
    }

    public int getFrom_rcmd() {
        return from_rcmd;
    }

    public void setFrom_rcmd(int from_rcmd) {
        this.from_rcmd = from_rcmd;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getPupil_count() {
        return pupil_count;
    }

    public void setPupil_count(int pupil_count) {
        this.pupil_count = pupil_count;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getVideo_count() {
        return video_count;
    }

    public void setVideo_count(int video_count) {
        this.video_count = video_count;
    }



    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof VideoDetails) {
            return getVid() == (((VideoDetails) o).getVid());
        }
        return super.equals(o);
    }
}
