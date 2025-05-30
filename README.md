# ForestOfOmok

동물의 숲 컨셉의 웹 오목 게임 프로젝트

## 팀원 소개
| 이상윤 (Sangyoon Lee)                                                                                              | 오가희(Gahee Oh)                                                                                              | 박지원(Jiwon Park)                                                                                              | 안민석(Minseok Ahn)                                                                                                | 이선우(Sunwoo Lee)                                                                                            |
|---------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------|

## 프로젝트 소개

### 기능
![image (12)](https://github.com/user-attachments/assets/574471bd-d73c-43ed-8e5a-6c215408a8de)
![image (11)](https://github.com/user-attachments/assets/f3f99d65-6c55-4619-80dc-0148e04f2d48)
![image (10)](https://github.com/user-attachments/assets/9136a7d6-7b66-4711-856d-47d33611ed82)
![image (9)](https://github.com/user-attachments/assets/3688b814-2446-4c67-bd84-a8a92ae41d7f)
![image (8)](https://github.com/user-attachments/assets/570eb4c2-a4f7-4865-b896-0961734d13c0)
![image (7)](https://github.com/user-attachments/assets/f80dc335-9b4b-403f-892c-4c3ceee67263)
![image (6)](https://github.com/user-attachments/assets/c10ed954-e356-4c35-b072-7b2d59d65671)
![image (5)](https://github.com/user-attachments/assets/edadac82-efe0-4c03-807e-4f3a52885170)
![image (4)](https://github.com/user-attachments/assets/e3f6d29f-a94b-4bb5-850c-fd50d0af0592)
![image (3)](https://github.com/user-attachments/assets/8e2dab93-ec67-43db-8fb4-bcc6f0a52e7e)
![image (2)](https://github.com/user-attachments/assets/de549345-7f45-48c3-8f07-a40078b10279)
![image (1)](https://github.com/user-attachments/assets/d68a628f-1c6d-415b-88ce-c50a6c0fcc9c)
![스크린샷 2025-05-30 152733](https://github.com/user-attachments/assets/91907a3b-9b64-4750-86e1-286075394062)
![스크린샷 2025-05-30 152716](https://github.com/user-attachments/assets/28f9cbdc-7fe2-4b8f-b030-f2a3791d61cb)

### UI
![image (13)](https://github.com/user-attachments/assets/1e1d5d75-7e96-41b4-8a4d-d8acdaa1bde5)

# 사용 기술

# 시스템 구조
![Frame 3](https://github.com/user-attachments/assets/9d558097-6b43-4772-b697-0ca6177006bb)

# DB 구조
![Group 45](https://github.com/user-attachments/assets/4af33d11-e45b-4570-966d-de156ef2be27)

## 설치 방법

# 깃허브 전략 

## 🌲 ForestOfOmok Git 브랜치 전략 정리

### 1. 브랜치 기본 구조

* `main`: 운영/배포용 브랜치

  * 안정적인 코드만 머지
  * 직접 작업하지 않음

* `develop`: 개발 통합 브랜치

  * 모든 기능 브랜치는 이 브랜치로 머지
  * 테스트 결과후 `main`으로 복사

---

### 2. 기능 브랜치 네이버 규칙

* 형식: `이름/develop/작업내용`
* 예시:

  * `jiwon/develop/connectServerExam`
  * `sunwoo/develop/join`
  * `sydev/develop/gameRoom`

> 누가, 어느 기능을 작업 중인지 포함하여 관리 편성을 높임

---

### 3. PR(Pull Request) 규칙

* 모든 기능 브랜치는 **`develop`으로 PR**
* 팀원과 코드 리비에우 진행 후 머지
* PR 메시지에는 관련된 이슈 번호 또는 작업 내용 기술적으로 기어

---

### 4. 이슈 & 작업 관리

* 이슈는 GitHub Issues 통해 등록
* 프로젝트 보드에 연결하여 진행 상황 관리
* 상황: `To Do` → `In Progress` → `Done`

---

### 5. 권장 브랜치 예시

| 브랜치 유형 | 예시                        | 설명          |
| ------ | ------------------------- | ----------- |
| 배포용    | `main`                    | 운영 서버 코드    |
| 통합 개발  | `develop`                 | 팀 전체 개발 코드  |
| 기능 개발  | `sunwoo/develop/login`    | 로그인 기능 작업   |
| 버그 수정  | `jiwon/bug/fix-room-null` | 방 생성 오류 수정  |
| 테스트    | `giyong/test/api-connect` | API 연결 테스트용 |

---

업무 중 필요한 것이나 추가 필요한 항목이 있으면 다시 보내주세요!

