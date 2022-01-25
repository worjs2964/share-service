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
![image](https://user-images.githubusercontent.com/80329358/150930330-bf18f527-dc3d-4400-b35f-016a3ee742ef.png) 테스트 

