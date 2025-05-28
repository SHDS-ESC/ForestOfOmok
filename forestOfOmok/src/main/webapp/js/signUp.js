function checkUsername() {
  const username = document.getElementById("username").value;
  const msg = document.getElementById("usernameMsg");

  // 예시: 특정 아이디는 사용 불가능하다고 가정
  const takenUsernames = ["admin", "user", "test"];
  if (username.trim() === "") {
    msg.textContent = "아이디를 입력하세요.";
    msg.style.color = "red";
  } else if (takenUsernames.includes(username)) {
    msg.textContent = "이미 사용 중인 아이디입니다.";
    msg.style.color = "red";
  } else {
    msg.textContent = "사용 가능한 닉네임입니다.";
    msg.style.color = "green";
  }
}

function submitForm() {
  const password = document.getElementById("password").value;
  const confirm = document.getElementById("confirm-password").value;
  const passwordMsg = document.getElementById("passwordMsg");

  if (password !== confirm) {
    passwordMsg.textContent = "비밀번호가 일치하지 않습니다.";
    passwordMsg.style.color = "red";

    return false;
  } else {
    passwordMsg.textContent = "";
    alert("회원가입이 완료되었습니다!");
    location.href = 'login.html'
    return true;
  } 
}

let selectedImgSrc = "";

function openModal() {
      document.getElementById("imgSelectModal").classList.remove("hidden");
      document.querySelector(".signUpBox").style.opacity = "0.3";
    }

function closeModal() {
      document.getElementById("imgSelectModal").classList.add("hidden");
      document.querySelector(".signUpBox").style.opacity = "1";
    }

function selectImage(imgElement) {
      document.querySelectorAll(".profileOption").forEach(img => {
        img.classList.remove("selected");
      });
      imgElement.classList.add("selected");
      selectedImgSrc = imgElement.src;
    }

function applySelectedImage() {
      if (selectedImgSrc) {
        const profileCircle = document.getElementById("profileCircle");
        profileCircle.style.backgroundImage = `url(${selectedImgSrc})`;
        profileCircle.style.backgroundSize = "cover";
        profileCircle.style.backgroundPosition = "center";
        closeModal();
      }
    }
