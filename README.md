# share-service

share-service는 자기가 구독 중인 서비스를 공유하거나, 다른 사람이 공유하는 서비스에 참여하여 저렴하게 이용할 수 있도록 만든 웹 사이트입니다.   
- 실제 서비스가 아닌 프로젝트를 위해 만든 사이트로 결제된 금액은 자정 전에 자동으로 환불됩니다.

# 배포 환경
link: [share-service 링크](http://52.78.87.137:8080/)
- aws ec2 (리눅스)
- aws rds (mysql)

# 사용 기술
- Java 11
- Spring Boot
- Spring Data JPA
- Spring Security
- Thymeleaf
- QueryDSL
- Mysql
- Lombok

# 구현 기능
- 계정
   - 회원가입, 로그인, 로그아웃, 회원 탈퇴 구현
   - email을 이용한 인증
   - 개인 정보 수정 기능
   - 관심 태그 등록
   - 공유 중인 쉐어, 참여 중인 쉐어 확인
- 쉐어
   - 쉐어 생성, 수정
   - 태그 추가/삭제
   - 쉐어 공유 여부 설정
- 알림
   - 쉐어 참여시 참여 회원과 주인에게 알림
   - 공유 계정 정보 수정 시 참여 회원에게 알림
   - 알림을 웹, 메일로 받을지 설정 가능
- 검색
   - 카테고리 검색 기능
   - 키워드 검색 기능
   - 검색 결과가 12개가 넘을 시 페이징 기능
- 결제
   - 아임포트 API 사용
   - 서버 검증 단계 추가

# 웹 화면 
- 홈 화면
   - 카테고리 구분 없이 가입이 가능한 공유를 최대 12개 조회하여 표시
   - ![image](https://user-images.githubusercontent.com/80329358/150930604-8dab48ab-c97d-4a38-a10c-a52994a7a574.png)   
   
- 회원가입
   - 서버에서 아이디, 닉네임 중복 확인 및 유효성 검사 
   - 비밀번호는 스프링 시큐리티의 DelegatingPasswordEncoder로 인코딩 후 저장
   - ![image](https://user-images.githubusercontent.com/80329358/150930961-155770ec-82df-4788-a435-6126449b7b9a.png)  
    
- 로그인
   - 로그인 시 UserDetailsService의 loadUserByUsername를 오버라이딩 하여 DB에 저장된 값을 UserDetails 객체로 변환하여 사용
   - ![image](https://user-images.githubusercontent.com/80329358/150944184-a933a426-ef22-473a-a8d9-848c09d94c46.png)   
   
- 프로필
   - 프로필 확인 시 본인이면 사용 중인 공유, 수정 버튼 등 추가적인 정보 표시
   - ![image](https://user-images.githubusercontent.com/80329358/150932781-aac7a9e1-8db7-4813-a492-1afed0dd4914.png)   
   
   - 프로필 정보, 패스워드, 알림 등 수정 가능
   - tagify 라이브러리를 사용하여 태그 수정/삭제 가능(비동기 처리)
   - ![image](https://user-images.githubusercontent.com/80329358/150933255-6c584dbd-dd59-4380-98c1-28a08c30f9f6.png)   
   
- 공유
   - 시큐리티로 공유 생성은 인증된 사용자만 접근 가능하도록 설정
   - 텍스트 입력, 날짜 선택에 라이브러리 적용
   - ![image](https://user-images.githubusercontent.com/80329358/150933611-a232fe93-ca1d-4f31-a529-38b56194206f.png)   
   
   - (공유 주인 화면)
   - 공유 주인은 태그 추가/삭제, 공유 공개/비공개, 수정이 표시
   - 수정 시 참여한 인원이 있다면 인원, 일정 변경 등에 제약(ex 참여한 회원보다 모집 인원을 낮게 설정 불가능)
   - ![image](https://user-images.githubusercontent.com/80329358/150935319-a455af6d-8f16-4624-b0ec-f2d6361c7853.png)   
   
   - (참여자 화면)
   - 공유 참가 시 계정 정보 확인 가능
   - ![image](https://user-images.githubusercontent.com/80329358/150935552-3990d6b1-d2f2-4ec8-ad5e-714905d0c375.png)
   - ![image](https://user-images.githubusercontent.com/80329358/150935591-1878001b-3ed4-4b9a-904d-2c8b3dc07ed4.png)   

- 결제
   - 아임포트 API 사용
   - 결제 성공 시 서버에서 아임포트 API를 통해 실제 결제 금액을 확인하고, 검증을 진행하여 문제가 있을 시 환불
   - ![image](https://user-images.githubusercontent.com/80329358/150936007-1c2f6a78-ad51-4102-b3d1-9ea0a19c7ba6.png)   

- 검색
   - QueryDsl로 동적 쿼리를 생성하여 키워드, 공유 타입으로 검색 가능
   - 페이징 
   - ![image](https://user-images.githubusercontent.com/80329358/150942982-6206f53b-0597-42f2-a43b-f238799c4ddd.png)   

- 알림
   - 스프링 인터셉터를 사용하여 로그인이 되어있다면 알림을 확인하여 표시
   - 공유에 대한 알림, 관심 키워드와 관련된 알림이 구분되어 표시됨
   - ![image](https://user-images.githubusercontent.com/80329358/150943888-e801467e-a6cd-4dbd-bf2d-d28468fb13f3.png)




