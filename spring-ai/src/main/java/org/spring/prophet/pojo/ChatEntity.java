package org.spring.prophet.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChatEntity {
    private String chatId;
    private String type;
    private String text;
}
