package com.demo.service.impl;

import com.demo.dao.MessageDao;
import com.demo.dao.UserDao;
import com.demo.entity.Message;
import com.demo.entity.User;
import com.demo.entity.vo.MessageVo;
import com.demo.service.MessageVoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageVoServiceImpl implements MessageVoService {
    @Autowired
    private MessageDao messageDao;
    @Autowired
    private UserDao userDao;

    @Override
    public MessageVo returnMessageVoByMessageID(int messageID) {
        Message message=messageDao.findByMessageID(messageID);
        User user=userDao.findByUserID(message.getUserID());
        MessageVo messageVo=new MessageVo(message.getMessageID(),user.getUserID(),message.getContent(),message.getTime(),user.getUserName(),user.getPicture(),message.getState());

        return messageVo;
    }

    @Override
    public List<MessageVo> returnVo(List<Message> messages) {
        List<MessageVo> list=new ArrayList<>();
        for(int i=0;i<messages.size();i++){
            list.add(returnMessageVoByMessageID(messages.get(i).getMessageID()));
        }
        return list;
    }
}
