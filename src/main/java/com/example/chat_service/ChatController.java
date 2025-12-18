package com.example.chat_service;

import com.example.chat_service.ChatMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Controller
public class ChatController {

    // [중요] 개설된 방들의 목록을 저장하는 저장소 (메모리)
    // 중복 방지(Set) + 동시성 처리(Synchronized)
    private final Set<String> roomList = Collections.synchronizedSet(new HashSet<>());

    // 1. 로비 화면 (메인 접속 시)
    @GetMapping("/")
    public String lobby() {
        return "lobby"; // templates/lobby.html을 보여줌
    }

    // 2. 방 만들기 요청 처리
    @PostMapping("/room/create")
    public String createRoom(@RequestParam String roomId, RedirectAttributes rttr) {
        if (roomList.contains(roomId)) {
            rttr.addFlashAttribute("error", "이미 존재하는 방 이름입니다.");
            return "redirect:/"; // 로비로 다시 보냄
        }

        roomList.add(roomId); // 방 명단에 추가
        return "redirect:/room/" + roomId; // 해당 방으로 이동
    }

    // 3. 방 입장 요청 처리
    @PostMapping("/room/join")
    public String joinRoom(@RequestParam String roomId, RedirectAttributes rttr) {
        if (!roomList.contains(roomId)) {
            rttr.addFlashAttribute("error", "개설되지 않은 방입니다.");
            return "redirect:/"; // 로비로 튕겨냄
        }
        return "redirect:/room/" + roomId;
    }

    // 4. 실제 채팅방 화면 연결
    @GetMapping("/room/{roomId}")
    public String enterRoom(@PathVariable String roomId, RedirectAttributes rttr) {
        // 주소창에 직접 입력해서 들어오는 경우도 체크해야 함
        if (!roomList.contains(roomId)) {
            rttr.addFlashAttribute("error", "존재하지 않는 방입니다. 먼저 방을 만들어주세요.");
            return "redirect:/";
        }
        return "chat"; // templates/chat.html
    }

    // 5. 메시지 중계 (기존과 동일)
    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public ChatMessage sendMessage(@DestinationVariable String roomId, ChatMessage message) {
        return message;
    }
}