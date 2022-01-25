# share-service

share-service는 자기가 구독중인 서비스를 공유하거나, 다른 사람이 공유하는 서비스에 참여하여 저렴하게 이용할 수 있도록 만든 웹 사이트입니다.   
- 실제 서비스가 아닌 프로젝트를 위해 만든 사이트로 결제된 금액은 자정 전에 자동으로 환불됩니다.

# 배포환경
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
- 계정 관리
   - 회원가입, 로그인, 로그아웃, 회원탈퇴
   - email을 이용한 인증
   - 개인정보 수정, 관심 태그 등록
   - 공유중인 쉐어, 참여한 쉐어 확인
- 쉐어
   - 쉐어 생성, 수정
   - 태그 추가/삭제
   - 쉐어 공유, 비공개 설정
- 검색
   - 카테고리 검색 기능
   - 키워드 검색 기능
   - 페이징
- 결제
   - 아임포트 API 사용
   - 서버 검증 단계 추가
