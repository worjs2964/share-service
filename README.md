# share-service

**share-service는 자기가 구독 중인 서비스를 공유하거나, 다른 사람이 공유하는 서비스에 참여하여 저렴하게 이용할 수 있도록 만든 웹 사이트입니다.**   

- 본 서비스는 실제 서비스가 아니며 결제된 금액은 매일 자정 12시 전후로 환불되며 본 사이트는 PC에 최적화 되어있습니다.
- 링크 : https://project-jg.link

### 테스트용 아이디
  - 인증된 사용자 (인증된 사용자는 모든 기능을 사용할 수 있습니다.)
    - ID: checked@share-service.com 
    - Password: 123123123
  - 인증되지 않은 사용자 (이메일 알림, 공유 생성 등 일부 기능을 사용할 수 없습니다.) 
    - ID: unchecked@share-service.com 
    - Password: 123123123

### 배포 환경 및 배포 방법
- AWS (배포 환경)
    - EC2 (아마존 리눅스)
    - RDS (Mysql)
    - ALB
    - Route53 
    - VPC(subnet, route table)
- 배포 방법
  1. 프로젝트를 Jar 파일로 빌드  
  2. SFTP 연결 후 EC2로 Jar 파일 전송
  3. EC2에서 Jar 파일 실행 

### 사용 기술
- Java 11
- Mysql
- Spring
  - Spring Boot
  - Spring Web
  - Spring Security
- ORM
  - Spring Data JPA
  - Querydsl
- Thymeleaf
- Git

### 구현 기능

- 회원
   - 회원가입, 로그인, 로그아웃, 회원 탈퇴 기능 구현 
   - 이메일을 통한 회원 인증 구현
     - 인증 메일 재전송 기능  
   - 개인 정보 수정 기능 구현
     - 기본 프로필 수정
     - 관심 키워드 등록/삭제
   - 공유 중인 쉐어, 참여 중인 쉐어 확인기능 구현
- 쉐어
   - 쉐어 생성 및 수정 기능 구현
     - 생성, 수정 시 조건에 따른 검증 구현
   - 쉐어 키워드 추가/삭제 기능 구현
   - 쉐어 공개/비공개 전환 기능 구현
- 알림
   - 쉐어에 참여시 참여자와 주인에게 알림기능 구현
   - 쉐어 정보 수정 시 참여 회원들에게 알림기능 구현
   - 웹, 메일 알림 기능 구현
- 검색
   - 카테고리 검색 기능 구현
   - 키워드 검색 기능 구현
   - 페이징 기능 구현
- 결제
   - 아임포트 API를 사용하여 구현
     - 서버 검증 단계를 추가하여 쉐어 상태에 따라 거부 구현

## 웹 화면 및 설명 
### 회원 기능
#### 회원 가입
- 회원 가입 시 서버에서 요청 값에 대해 Bean Validation과 Validator로 검증하여 이메일, 닉네임에 대한 중복 확인 및 유효성 검사 진행하였습니다.
- 패스워드는 저장 시 평문이 아닌 DelegatingPasswordEncoder로 인코딩 후 DB에 저장하였습니다.
<img width="1437" alt="image" src="https://user-images.githubusercontent.com/80329358/186087532-386f886c-8fb0-48fe-a65c-c83bacd77cb3.png">

#### 이메일 인증
- JavaMailSender와 TemplateEngine을 사용하여 메일을 전송하였습니다. 
  - 메일 전송은 부가적인 기능이라 생각하여, 비동기로 메일을 전송하여 메일 전송 시간을 기다리 않고 메일 전송에 오류가 발생해도 회원가입은 정상적으로 진행되도록 하였습니다.
- 메일을 보낸 시간을 기록하여 인증 완료(3분안에 인증 완료) 및 인증 메일 재전송(메일 전송 후 1분 경과)에 유효 시간을 구현하였습니다.
<img width="1439" alt="image" src="https://user-images.githubusercontent.com/80329358/186088351-e53b0918-ca8f-4a7d-ae8e-fecb05071bfe.png">

#### 로그인
- UserDetailsService의 loadUserByUsername를 오버라이딩 하여 스프링 시큐리티의 기본 로그인을 사용하여 로그인하도록 구현하였습니다.
- loadUserByUsername의 반환값으로 User 클래스(UserDetails 구현체)를 상속 받은 UserAccount 클래스를 만들고 필드로 User(도메인 객체)를 
  가지고 있게하여 SecurityHolder에서 User 객체를 쉽게 가져올 수 있게 구현하였습니다.
<img width="1438" alt="image" src="https://user-images.githubusercontent.com/80329358/186088777-4acbbb34-10b9-4adb-afe2-283c3cb2c920.png">

#### 프로필
- 프로필 확인 시 조회한 사람이 주인인지 확인하여, 주인인지 타인인지에 따라 확인할 수 있는 내용을 제한하였습니다.
  - 주인: 소개, 포인트, 공유 목록(종료된 공유도 포함), 이용중인 공유, 수정 버튼
  - 타인: 소개, 공유 목록(가입 가능한 목록)
<img width="1439" alt="image" src="https://user-images.githubusercontent.com/80329358/186089023-f080825a-a3c3-42b4-b7d8-a03bd2ad4ba0.png">
      
- 프로필 수정에는 자기소개, 알림 여부, 키워드, 닉네임 등을 확인하고 수정할 수 있게 만들었습니다.
  - tagify 라이브러리를 사용하여 키워드 수정/삭제 기능을 구현하였습니다.
![Aug-23-2022 16-30-59](https://user-images.githubusercontent.com/80329358/186098431-5ee47f9d-a6ee-4bf2-bd5c-f92de87452a4.gif)

### 공유 기능
  #### 공유 생성
- 시큐리티 설정으로 권한 확인 진행하여 공유 생성은 인증된 회원만 진행할 수 있도록 하였습니다.
  - RoleHierarchy 구현체를 만들어서 넣어주어 회원의 계층을 두어 인증된 회원은 비인증된 회원이 할 수 있는 모든 기능을 포함하도록 하였습니다.
- 텍스트 입력, 날짜 선택 등에 라이브러리 적용하여 좀 더 편하게 사용할 수 있도록 구현하였습니다.
<img width="1428" alt="image" src="https://user-images.githubusercontent.com/80329358/186104518-9ea61a06-9c6c-4804-bbe6-a5ccbf5f5a9d.png">
  
#### 공유 화면  
- 공유의 주인인지 확인하여 주인은 태그 추가/삭제 및 공유 공개/비공개 등 관리 기능이 보이도록 구현하였습니다.
  - 수정 시 참여한 인원이 있다면 인원, 일정 변경등에 제약을 두었습니다. (ex: 참여인원을 모집인원이 적게 변경 불가)
(공유자 화면)
![공유자](https://user-images.githubusercontent.com/80329358/186100485-36ba1fe1-17e1-4f95-a028-9f1f84e65e27.gif)
    
- 공유에 참가한 회원은 계정 정보 확인할 수 있도록 구현하였습니다.
(참여자 화면)
![참여자2](https://user-images.githubusercontent.com/80329358/186102362-4ac7a28a-abd3-445a-8b82-99e8d1454ac1.gif)

### 결제
- 아임포트 API 사용하여 프론트에서 JS를 통해 결제를 요청하면 서버에서 결제 금액, 공유 상태 등을 검증하고 결제 승인이 진행되도록 구현하였습니다.
![결제](https://user-images.githubusercontent.com/80329358/186101949-7978ac98-93b2-404d-80d4-483ce487dc3f.gif)

### 공유 목록 확인

#### 홈 화면
- 홈 화면은 카테고리 구분 없이 가입이 가입이 가능한 공유를 최신순으로 정렬하여 최대 12개까지 표시해주었습니다.
<img width="1428" alt="image" src="https://user-images.githubusercontent.com/80329358/186103070-1b3295dc-6df4-4351-a4fa-b0979f3513d0.png">

#### 검색
- Querydsl로 동적 쿼리를 생성하여 검색어에 해당하는 키워드, 제목 등을 가진 공유룰 최신순으로 정렬하여 출력하였습니다.
    - 페이징 처리를 구현하여 검색 결과가 12개(기본값)이 넘어가면 페이지 선택할 수 있도록 구현하였습니다. 
![검색](https://user-images.githubusercontent.com/80329358/186103489-8ee31c4c-c032-441b-b960-4f856e2f18e9.gif)

### 알림
- Spring Interceptor를 사용하여 요청 시마다 회원이 새로운 알림이 있는지 조회하여, 알림이 있을 시 화면 오른쪽 상단에 알림 표시의 색을 변경하여 확인할 수있도록 
구현하였습니다.
  - 알림은 @EventListener를 사용하여 ShareService가 알림 기능에 대해 과한 의존성을 가지지 않게 분리하였습니다. 
  - 알림의 타입을 Enum 타입으로 구분하여 공유에 대한 알림, 관심 키워드에 대한 알림을 나눠서 확인할 수 있도록 하였으며, 알림을 확인 시 알림의 상태를 
  읽음 상태로 변경하여 새로운 알림과 구분할 수 있도록 구현하였습니다.
![알림](https://user-images.githubusercontent.com/80329358/186103931-4d215df1-3598-4707-bfa6-02debe87e18d.gif)



