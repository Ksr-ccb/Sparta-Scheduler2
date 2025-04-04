# 💡 일정관리 앱 기능
  ### API 명세서 
  https://documenter.getpostman.com/view/43364760/2sB2cSgijZ
  
  ### 사용자 인터페이스 방식
   - Postman을 통한 HTTP 통신
   - 데이터 형태 : JSON
     
  ### 입력
   - 정해진 기능에 맞는 HTTP METHOD, Params, Body(json) 값들

  ### 출력
  ```
  - 기능에 알맞는 Json 출력값들
  - HTTP Status Code
      - 200 번대 : 200(+반환 JSON) , 201(+ 반환 JSON) , 204( 반환값 x )
      - 400 번대 : 400(잘못된 형식의 값, 회원 아이디 중복, 로그인안했을 때),
                   401(비밀번호가 틀렸거나 권한이 없을 때),
                   404(id값들이 잘못됐을 때)
      - 500 번대 : 500(요청이 올바르긴한데 쿼리 실행 도중에 문제가 있음 - db연결이나 코드로직 문제)
  ```

  ## 기능

  ### __[할 일]__
  
  일정의 고유 식별자(ID), 할일 제목, 할 일 내용, 작성자 아이디, 작성/수정일을 저장

  - __할 일 등록__
      - ※ 로그인 필수
      - 필수 입력 값 : 할일 제목, 할 일 내용
      - 작성/수정 일은 할 일 등록시 자동 갱신  +  최초 입력 시, 수정일은 작성일과 동일

  - __할 일 내용 및 작성자 수정__
      - ※ 로그인 필수
      - 자기가 작성한 스케줄만 수정이 가능
      - 수정 가능한 값 :  할일 제목, 할 일 내용
      - 수정일은 수정 완료 시, 수정한 시점으로 자동 갱신

  - __모든 할 일 불러오기__
      - 아이디 순서 오름차순 정렬

  - __자기가 쓴 할 일 불러오기__
      - ※ 로그인 필수
      - 자기가 작성한 스케줄만 출력함.
      - `수정일` 기준 내림차순 정렬

  - __아이디 값에 맞는 하나의 할 일 출력__
      - 스케줄 테이블에 있는 아이디값이어야 함

  - __아이디 값에 맞는 하나의 할 일 삭제__
      - ※ 로그인 필수
      - 자기가 작성한 스케줄만 삭제 가능
      - 스케줄 테이블에 있는 아이디 값이어야 함

  - __페이지 넘버와 사이즈에 맞는 모든 할 일 출력__
      - ※ 로그인 필수
      - 페이지 사이즈는 디폴트값으로 10을 가진다.
      - 전달받은 페이지 번호와 크기를 기준으로 쿼리를 작성하여 필요한 데이터만을 조회하고 반환
            - 범위를 넘어선 페이지를 요청하는 경우 빈 배열을 반환

  <br><br>  
  
  ### __[작성자]__
  
  작성자의 고유 식별자(ID), 이름, 이메일, 등록일, 수정일 정보를 가지고 있음.

  - __회원가입__
      - 필수 값 : 작성자 이름, 이메일, 비밀번호
      - 비밀번호는 Bcrypt 함수를 통해 암호화
      - 같은 이메일로 여러 아이디를 만들 수 없음.

  - __로그인__
      - 필수 값 : 이메일, 비밀번호
      - 로그인 성공시, 세션에 회원 객체가 저장됨

  - __회원 정보 수정__
      - 수정 가능한 값 : 작성자 이름, 비밀번호
      - 필수 값 : 현재 비밀 번호
      - 수정 완료시 수정된 객체 출력

  - __회원 탈퇴__
      - 회원 탈퇴시 해당 회원이 작성한 할 일 모두 삭제

---
  ## 예외 발생 처리
  ```
  - 선택한 일정 정보를 조회할 수 없을 때 예외가 발생
      - 잘못된 정보로 조회하려고 할 때 => Bad Requset
      - 이미 삭제된 정보를 조회하려고 할 때 => Not Found
  - 필수 값이 없을 때 예외가 발생
      - null 체크 후 빈값일 때 => Not Found
  - 중복 사용자 등록 예외
      - 사용자 등록할 때 이미 등록되어 있는 `이메일`이 들어오면 가입 방지 => Bad Request 
  - 비밀번호 검사
      - 틀렸을 때 인증 실패 =>  UNAUTHORIZED
  - 로그인이 필요한 서비스 이용시
      - 로그인 하지 않았을 때  => UNAUTHORIZED
      - 권한이 없는 서비스를 이용하려고 할 때 (남의 스케줄,덧글 값들을 수정/삭제시도) => UNAUTHORIZED
  ```
---

### ⚙️ 개발 환경
`JAVA` `spring-security-core:6.4.4` `spring-boot`
`JDK 17.0.14`
`MySQL`

### 🕰️ 개발 기간
25.04.01 - 25.04.04

---

# 📚 설계
## 과제 요구사항과 구조도
### 스케줄
![image](https://github.com/user-attachments/assets/39a3b1e6-b247-4885-92da-730ede89ca98)


### 회원정보
![image](https://github.com/user-attachments/assets/1ae55740-773c-4d9d-88f5-ab1cf54c678f)



### 덧글
![image](https://github.com/user-attachments/assets/67abfb6b-0a2b-46de-be64-f9e147213855)


## ERD
![image](https://github.com/user-attachments/assets/167458a2-615d-4590-a568-ed711de55bdb)



---
  # 깃 컨벤션 
참고 :   https://treasurebear.tistory.com/70


🎨::
코드의 구조/형태 개선
Improve structure / format of the code.

⚡️::
성능 개선
Improve performance.

🔥::
코드/파일 삭제
Remove code or files.

🐛::
버그 수정
Fix a bug.

✨::
새 기능
Introduce new features.

📝::
문서 추가/수정
Add or update documentation.

💄::
UI/스타일 파일 추가/수정
Add or update the UI and style files.

♻️::
코드 리팩토링
Refactor code.

➕::
의존성 추가
Add a dependency.

➖::
의존성 제거
Remove a dependency.

🔨::
개발 스크립트 추가/수정
Add or update development scripts.

💡::
주석 추가/수정
Add or update comments in source code.
