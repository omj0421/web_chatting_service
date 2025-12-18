package com.example.chat_service; // 패키지명은 본인 프로젝트에 맞게!

public class ChatMessage {
    private String type;        // "TEXT", "IMAGE"
    private boolean encrypted;  // 암호화 여부
    private String content;     // 내용
    private String sender;      // 보낸 사람

    // 기본 생성자 (필수!)
    public ChatMessage() {}

    // 여기서부터 Getter와 Setter 메서드들
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}