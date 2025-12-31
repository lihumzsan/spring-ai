package org.spring.prophet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.spring.prophet.pojo.ChatEntity;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ChatRedisMemory implements ChatMemory {

    private static final String KEY_PREFIX = "chat:history:";

    private final RedissonClient redissonClient;

    public ChatRedisMemory(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        String key = KEY_PREFIX + conversationId;

        // 使用 Redisson 的 RList 存储 ChatEntity
        RList<ChatEntity> list = redissonClient.getList(key);

        List<ChatEntity> listIn = new ArrayList<>();
        for (Message msg : messages) {
            String[] strs = msg.getText().split("</think>");
            String text = strs.length == 2 ? strs[1] : strs[0];

            ChatEntity ent = new ChatEntity();
            ent.setChatId(conversationId);
            ent.setType(msg.getMessageType().getValue());
            ent.setText(text);
            listIn.add(ent);
        }

        // 追加到列表末尾
        list.addAll(listIn);
        // 设置过期时间
        list.expire(30, TimeUnit.MINUTES);
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        String key = KEY_PREFIX + conversationId;
        RList<ChatEntity> list = redissonClient.getList(key);
        int size = list.size();
        if (size == 0) {
            return Collections.emptyList();
        }
        int start = Math.max(0, size - lastN);
        List<ChatEntity> subList = list.subList(start, size);

        List<Message> listOut = new ArrayList<>();
        for (ChatEntity chat : subList) {
            if (MessageType.USER.getValue().equals(chat.getType())) {
                listOut.add(new UserMessage(chat.getText()));
            } else if (MessageType.ASSISTANT.getValue().equals(chat.getType())) {
                listOut.add(new AssistantMessage(chat.getText()));
            } else if (MessageType.SYSTEM.getValue().equals(chat.getType())) {
                listOut.add(new SystemMessage(chat.getText()));
            }
        }
        return listOut;
    }

    @Override
    public void clear(String conversationId) {
        String key = KEY_PREFIX + conversationId;
        // 直接删除整个 list
        redissonClient.getKeys().delete(key);
    }
}
