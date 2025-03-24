package com.demo.service;

import com.demo.entity.Message;
import com.demo.entity.vo.MessageVo;

import java.util.List;

public interface MessageVoService  {
    MessageVo returnMessageVoByMessageID(int messageID);
    List<MessageVo> returnVo(List<Message> messages);
}
