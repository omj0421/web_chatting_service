# 🔒 Secret Chat (웹소켓 암호화 채팅)

Spring Boot와 WebSocket을 활용한 **실시간 1:1 암호화 채팅 서비스**입니다.

## 🛠 기술 스택
- **Backend:** Java 17, Spring Boot, WebSocket (STOMP)
- **Frontend:** HTML5, JavaScript (CryptoJS), Thymeleaf
- **Security:** AES-256 Encryption (Client-side)

## 📌 주요 기능
1. **실시간 통신:** WebSocket + STOMP Pub/Sub 모델 적용
2. **보안 전송:** 전송 전 JS단에서 암호화 -> 서버는 암호문 중계 -> 수신 후 복호화
3. **멀티룸 시스템:** 사용자가 직접 방을 개설하고 입장하는 동적 룸 관리
4. **미디어 전송:** 이미지 파일 전송 지원 (Base64)
5. **UI/UX:** 디스코드 스타일의 다크 테마 및 반응형 레이아웃

## 📸 실행 화면
<img width="532" height="969" alt="image" src="https://github.com/user-attachments/assets/acd794a8-15b2-41b5-be65-a7e6f3783bdd" />
<img width="1190" height="960" alt="image" src="https://github.com/user-attachments/assets/4524d2d2-b059-4a59-9dcb-d91ce3919853" />
<img width="1201" height="976" alt="image" src="https://github.com/user-attachments/assets/a3146e7c-74ae-4478-9b8a-112e6dfcf3c2" />
