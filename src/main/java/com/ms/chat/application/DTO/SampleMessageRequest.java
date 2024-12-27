package com.ms.chat.application.DTO;


import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SampleMessageRequest {

    private String id;
    private  String username;
    private  String content;
}
