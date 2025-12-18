var stompClient = null;
var secretKey = "MySuperSecretKey";

// 1. URL에서 방 번호(roomId) 추출하기
// 예: http://localhost:8080/room/game -> 'game'을 가져옴
var roomId = window.location.pathname.split("/").pop();

// 만약 방 번호 없이 그냥 들어왔다면 강제로 'lobby'로 보냄
if (!roomId || roomId === 'room') {
    roomId = 'lobby';
}

var myName = prompt("사용할 닉네임을 입력하세요", "익명");
if (!myName) {
    myName = "익명" + Math.floor(Math.random() * 1000);
}

const avatarColors = ["#5865F2", "#FAA61A", "#3BA55C", "#ED4245", "#EB459E"];

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        // [핵심 변경 포인트 1] 구독 주소가 동적으로 바뀜
        // 기존: /topic/public
        // 변경: /topic/room/ + roomId
        stompClient.subscribe('/topic/room/' + roomId, function (chatMessage) {
            showMessage(JSON.parse(chatMessage.body));
        });
    });
}

function send() {
    var messageInput = document.getElementById('message');
    var messageContent = messageInput.value;
    var isEncrypt = document.getElementById('encryptToggle').checked;
    var fileInput = document.getElementById('imageInput');

    if (fileInput.files.length > 0) {
        var reader = new FileReader();
        reader.onload = function(e) {
            var rawData = e.target.result;
            processAndSend(rawData, "IMAGE", isEncrypt);
            fileInput.value = '';
            document.getElementById('fileNameDisplay').innerText = '';
        };
        reader.readAsDataURL(fileInput.files[0]);
    } else {
        if(messageContent.trim() !== '') {
            processAndSend(messageContent, "TEXT", isEncrypt);
            messageInput.value = '';
        }
    }
}

function processAndSend(content, type, isEncrypt) {
    var finalContent = content;

    if (isEncrypt) {
        finalContent = CryptoJS.AES.encrypt(content, secretKey).toString();
    }

    var chatMessage = {
        sender: myName,
        content: finalContent,
        type: type,
        encrypted: isEncrypt
    };

    // [핵심 변경 포인트 2] 보낼 때도 방 번호를 포함해서 보냄
    // 기존: /app/chat.sendMessage
    // 변경: /app/chat/ + roomId
    stompClient.send("/app/chat/" + roomId, {}, JSON.stringify(chatMessage));
}

// showMessage 함수는 그대로 두시면 됩니다. (변경 없음)
function showMessage(message) {
    var messageArea = document.getElementById('messageArea');
    var li = document.createElement('li');
    li.className = "message-row";

    var avatar = document.createElement('div');
    avatar.className = "avatar";
    var firstChar = message.sender ? message.sender.charAt(0) : "U";
    avatar.innerText = firstChar;

    var colorIndex = (firstChar.charCodeAt(0) % avatarColors.length);
    avatar.style.backgroundColor = avatarColors[colorIndex];

    var contentDiv = document.createElement('div');
    contentDiv.className = "message-content";

    var header = document.createElement('div');
    header.className = "message-header";

    var usernameSpan = document.createElement('span');
    usernameSpan.className = "username";

    if (message.sender === myName) {
        usernameSpan.innerText = message.sender + " (나)";
        usernameSpan.style.color = "#ffff00";
    } else {
        usernameSpan.innerText = message.sender;
        usernameSpan.style.color = "white";
    }

    var timeSpan = document.createElement('span');
    timeSpan.className = "timestamp";
    var now = new Date();
    timeSpan.innerText = now.getHours() + ":" + String(now.getMinutes()).padStart(2, '0');

    header.appendChild(usernameSpan);
    header.appendChild(timeSpan);

    var textDiv = document.createElement('div');
    textDiv.className = "message-text";

    if (message.encrypted) {
        var secretBtn = document.createElement('div');
        secretBtn.className = 'secret-box';
        secretBtn.innerText = "🔒 암호화된 메시지 (클릭하여 해독)";

        secretBtn.onclick = function() {
            try {
                var bytes = CryptoJS.AES.decrypt(message.content, secretKey);
                var originalText = bytes.toString(CryptoJS.enc.Utf8);
                if (!originalText) throw new Error();

                if(message.type === 'IMAGE') {
                    secretBtn.innerHTML = '<img src="' + originalText + '" class="img-preview"/>';
                } else {
                    secretBtn.innerText = originalText;
                }
                secretBtn.style.border = "none";
                secretBtn.style.color = "#dcddde";
                secretBtn.style.cursor = "default";
                secretBtn.onclick = null;
            } catch (e) {
                alert("복호화 실패!");
            }
        };
        textDiv.appendChild(secretBtn);
    } else {
        if(message.type === 'IMAGE') {
             var img = document.createElement('img');
             img.src = message.content;
             img.className = 'img-preview';
             textDiv.appendChild(img);
        } else {
            textDiv.innerText = message.content;
        }
    }

    contentDiv.appendChild(header);
    contentDiv.appendChild(textDiv);
    li.appendChild(avatar);
    li.appendChild(contentDiv);
    messageArea.appendChild(li);
    messageArea.scrollTop = messageArea.scrollHeight;
}

connect();