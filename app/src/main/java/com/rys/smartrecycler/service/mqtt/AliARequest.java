package com.rys.smartrecycler.service.mqtt;

import com.aliyun.alink.linksdk.cmp.core.base.ARequest;

/**
 * 创建时间：2019/6/26
 * 作者：李伟斌
 * 功能描述:
 */
public class AliARequest extends ARequest {
    private String replyTopic;
    private String topic;

    public AliARequest() {
        super();
    }

    public AliARequest(String replyTopic, String topic) {
        this.replyTopic = replyTopic;
        this.topic = topic;
    }

    public String getReplyTopic() {
        return replyTopic;
    }

    public void setReplyTopic(String replyTopic) {
        this.replyTopic = replyTopic;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
