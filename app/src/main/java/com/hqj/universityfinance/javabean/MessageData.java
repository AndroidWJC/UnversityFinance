package com.hqj.universityfinance.javabean;

/**
 * Created by wang on 17-11-26.
 */

public class MessageData extends BmobBaseBean {

    private Integer send_id;
    private Integer receive_id;
    private String msg_content;

    public String getMsg_centent() {
        return msg_content;
    }

    public void setMsg_content(String msg_content) {
        this.msg_content = msg_content;
    }

    public Integer getSend_id() {
        return send_id;
    }

    public void setSend_id(Integer send_id) {
        this.send_id = send_id;
    }

    public Integer getReceive_id() {
        return receive_id;
    }

    public void setReceive_id(Integer receive_id) {
        this.receive_id = receive_id;
    }

}
