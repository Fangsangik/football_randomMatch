# ⚽ Football Project
> 축구 팀 매칭 및 구장 예약 플랫폼

## 📋 프로젝트 개요
축구를 사랑하는 사람들을 위한 종합 플랫폼으로, 팀 생성/관리, 랜덤 매치 시스템, 구장 예약, 실시간 알림 등의 기능을 제공합니다.

## 🔧 기술 스택

### Backend (본인 개발)
- **Framework**: Spring Boot 3.3.2
- **Language**: Java 17
- **Database**: MySQL
- **Storage**: Redis (토큰 관리, 세션), AWS S3 (파일 저장)
- **Security**: Spring Security + JWT
- **External API**: CoolSMS (SMS), Kakao Map API, Gemini API

### Frontend (Claude AI 활용)
- **Framework**: React 18.2.0
- **Language**: TypeScript 4.9.0
- **State Management**: React Query
- **Styling**: Styled Components
- **Form**: React Hook Form
- **UI/UX**: Toast 알림, Loading Skeleton

### Infrastructure
- **Build Tool**: Gradle
- **Version Control**: Git

## ⚙️ 주요 기능

### 👥 사용자 관리
- 회원가입/로그인 (JWT 기반 인증)
- 사용자 등급 시스템 (티어)
- 위치 기반 사용자 검색

### 🏃‍♂️ 팀 관리
- 팀 생성 및 관리
- 팀원 초대/승인 시스템
- 팀 역할 관리 (리더, 멤버)

### ⚽ 매치 시스템
- 랜덤 매치 생성 및 참여
- 팀 대 팀 매치
- 매치 평가 시스템
- 매치 결과 기록

### 🏟️ 구장 예약
- 구장 검색 및 예약
- 예약 상태 관리
- 벤더(구장 사업자) 관리 시스템

### 📱 실시간 알림
- 매치 알림 구독 시스템
- SMS 알림 발송

### 🎬 숏츠 기능
- 축구 관련 숏츠 업로드/조회

### 🔧 관리자 시스템
- 관리자 인증 및 권한 관리
- 벤더 승인 시스템 (기본 구조)
- 시스템 관리 기능

## 🏗️ 아키텍처

### Backend 구조
```
src/main/java/com/side/football_project/
├── domain/                    # 도메인별 패키지
│   ├── user/                 # 사용자 관리
│   ├── team/                 # 팀 관리
│   ├── match/                # 매치 시스템
│   ├── reservation/          # 예약 시스템
│   ├── stadium/              # 구장 관리
│   ├── shorts/               # 숏츠 기능
│   ├── notification/         # 알림 시스템
│   ├── admin/                # 관리자 시스템
│   └── vendor/               # 벤더 시스템
└── global/                   # 공통 기능
    ├── config/               # 설정
    ├── security/             # 보안
    ├── common/               # 공통 유틸
    └── util/                 # 유틸리티
```

### 주요 설계 패턴
- **DDD (Domain Driven Design)**: 도메인별 패키지 구조
- **Layered Architecture**: Controller → Service → Repository
- **DTO Pattern**: 계층 간 데이터 전송
- **Exception Handling**: 커스텀 예외 처리

## 🔐 보안 기능
- JWT 기반 인증/인가
- Spring Security 설정
- 토큰 블랙리스트 관리
- 비밀번호 암호화
- CORS 설정

## 📊 데이터베이스 설계
- **User**: 사용자 정보 및 인증
- **Team**: 팀 정보 및 멤버십
- **Match**: 매치 정보 및 참여자
- **Stadium**: 구장 정보 및 주소
- **Reservation**: 예약 정보
- **Notification**: 알림 구독 정보

## 🚀 실행 방법

### Prerequisites
- Java 17
- MySQL 8.0
- Redis

### 환경 변수 설정
```properties
# Database
DB_URL=jdbc:mysql://localhost:3306/football
DB_USERNAME=root
DB_PASSWORD=your_password

# JWT
JWT_SECRET_KEY=your_jwt_secret_key
JWT_EXPIRATION_TIME=604800000

# AWS S3 (선택사항)
AWS_ENABLED=false
AWS_ACCESS_KEY=your_access_key
AWS_SECRET_KEY=your_secret_key

# SMS API
SMS_API_KEY=your_sms_api_key
SMS_API_SECRET=your_sms_secret

# Kakao API
KAKAO_CLIENT_ID=your_kakao_client_id
```


## 🧪 테스트
- **MatchUserServiceImplTest**: 매치 사용자 서비스 테스트
- **RandomMatchServiceTest**: 랜덤 매치 시스템 테스트
- JUnit 5 기반 단위 테스트 (일부 구현)

## 📈 성능 최적화
- **Redis 활용**: JWT 토큰 블랙리스트 저장, 세션 관리
- **ConcurrentHashMap**: 티어별 매치 대기열 동시성 처리 (Lock-Free)
- **JPA 최적화**: 필요한 곳에 Fetch Join 적용
- **환경별 설정**: 개발/운영 환경 분리 (Mock/Real S3)

## 🔄 API 문서
주요 엔드포인트:
- `POST /api/auth/login` - 로그인
- `GET /api/teams` - 팀 목록 조회
- `POST /api/matches/random` - 랜덤 매치 생성
- `GET /api/stadiums` - 구장 검색
- `POST /api/reservations` - 예약 생성

## 🛠️ Troubleshooting

### 주요 기능별 문제 해결 (실제 구현 기준)

#### 1. 사용자 인증/로그인 문제
**문제**: JWT 토큰 인증 실패, 로그인 후 권한 오류
```
403 Forbidden - Access Denied
401 Unauthorized - Authentication failed
```

**실제 구현된 해결책**:
```java
// JWT 토큰 검증 (JwtProvider.java)
public boolean validateToken(String token) {
    try {
        Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token);
        return true;
    } catch (ExpiredJwtException e) {
        log.error("JWT token expired: {}", e.getMessage());
        return false;
    } catch (JwtException | IllegalArgumentException e) {
        log.error("Invalid JWT token: {}", e.getMessage());
        return false;
    }
}

// 블랙리스트 토큰 확인 (BlackListToken 엔티티)
public boolean isBlacklisted(String token) {
    return redisTemplate.hasKey("blacklist:" + token);
}
```

#### 2. 팀 생성/관리 오류
**문제**: 팀원 가입/탈퇴 시 권한 오류, 중복 가입 시도
```
TeamErrorCode.TEAM_NOT_FOUND
TeamErrorCode.ALREADY_JOINED
TeamErrorCode.UNAUTHORIZED_ACCESS
```

**실제 구현된 해결책**:
```java
// 팀 가입 시 중복 검증 (TeamServiceImpl.java)
public void joinTeam(Long teamId, Long userId) {
    // 이미 가입된 팀원인지 확인
    if (teamMemberRepository.existsByTeamIdAndUserId(teamId, userId)) {
        throw new CustomException(TeamErrorCode.ALREADY_JOINED);
    }
    
    Team team = teamRepository.findById(teamId)
        .orElseThrow(() -> new CustomException(TeamErrorCode.TEAM_NOT_FOUND));
}

// 팀 리더 권한 검증
private void validateTeamLeader(Long teamId, Long userId) {
    TeamMember member = teamMemberRepository.findByTeamIdAndUserId(teamId, userId)
        .orElseThrow(() -> new CustomException(TeamErrorCode.MEMBER_NOT_FOUND));
    
    if (member.getRole() != TeamRole.LEADER) {
        throw new CustomException(TeamErrorCode.UNAUTHORIZED_ACCESS);
    }
}
```

#### 3. 랜덤 매치 시스템 오류
**문제**: 매치 생성 실패, 티어 매칭 오류, 대기열 관리 문제
```
Match creation failed
Tier matching error
Queue management issue
```

**실제 구현된 해결책**:
```java
// 랜덤 매치 생성 (RandomMatchService.java)
@Transactional
public void createRandomMatch(MatchRequestDto requestDto) {
    User user = userRepository.findById(requestDto.getUserId())
        .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
    
    // 티어 기반 매칭 대기열에 추가
    UserTier userTier = user.getTier();
    ConcurrentLinkedQueue<Long> queue = waitingQueues.get(userTier);
    
    if (queue == null) {
        queue = new ConcurrentLinkedQueue<>();
        waitingQueues.put(userTier, queue);
    }
    
    queue.offer(requestDto.getUserId());
}

// 매치 참여자 관리
public void processMatching() {
    for (Map.Entry<UserTier, ConcurrentLinkedQueue<Long>> entry : waitingQueues.entrySet()) {
        ConcurrentLinkedQueue<Long> queue = entry.getValue();
        
        if (queue.size() >= MATCH_SIZE) {
            createMatch(queue, entry.getKey());
        }
    }
}
```

#### 4. 구장 예약 시스템 문제
**문제**: 예약 상태 불일치, 예약 취소 처리 오류
```
ReservationErrorCode.NOT_FOUND
ReservationErrorCode.INVALID_STATUS
ReservationErrorCode.UNAUTHORIZED_ACCESS
```

**실제 구현된 해결책**:
```java
// 예약 상태 변경 검증 (Reservation.java)
public void confirm() {
    if (this.status != ReservationStatus.PENDING) {
        throw new CustomException(ReservationErrorCode.INVALID_STATUS);
    }
    this.status = ReservationStatus.CONFIRMED;
}

public void cancel() {
    if (this.status == ReservationStatus.COMPLETED || 
        this.status == ReservationStatus.CANCELLED) {
        throw new CustomException(ReservationErrorCode.INVALID_STATUS);
    }
    this.status = ReservationStatus.CANCELLED;
}

// 예약 권한 검증 (UserReservationServiceImpl.java)
private void validateReservationOwner(Reservation reservation, Long userId) {
    if (!reservation.getUser().getId().equals(userId)) {
        throw new CustomException(ReservationErrorCode.UNAUTHORIZED_ACCESS);
    }
}
```

#### 5. 알림 시스템 오류
**문제**: SMS 발송 실패, 알림 구독 관리 오류
```
SMS delivery failed
Notification subscription error
```

**실제 구현된 해결책**:
```java
// SMS 발송 로직 (SmsUtil.java)
public void sendSms(String to, String text) {
    try {
        Message message = new Message(smsConfig.getApiKey(), sms Config.getApiSecret());
        
        HashMap<String, String> params = new HashMap<>();
        params.put("to", to);
        params.put("from", smsConfig.getPhoneNumber());
        params.put("type", "SMS");
        params.put("text", text);
        
        JSONObject result = message.send(params);
        
        // 발송 로그 저장
        smsRepository.save(SmsLog.builder()
            .phoneNumber(to)
            .message(text)
            .success(true)
            .build());
        
    } catch (Exception e) {
        log.error("SMS 발송 실패: {}", e.getMessage());
        smsRepository.save(SmsLog.builder()
            .phoneNumber(to)
            .message(text)
            .success(false)
            .errorMessage(e.getMessage())
            .build());
        throw new RuntimeException("SMS 발송에 실패했습니다.");
    }
}

// 알림 구독 관리 (MatchNotificationServiceImpl.java)
public void subscribe(MatchNotificationSubscriptionRequestDto requestDto) {
    // 중복 구독 방지
    if (subscriptionRepository.existsByUserIdAndMatchId(
            requestDto.getUserId(), requestDto.getMatchId())) {
        return; // 이미 구독된 상태
    }
    
    MatchNotificationSubscription subscription = MatchNotificationSubscription.builder()
        .userId(requestDto.getUserId())
        .matchId(requestDto.getMatchId())
        .build();
    
    subscriptionRepository.save(subscription);
}
```

#### 6. 파일 업로드 문제 (S3 연동)
**문제**: S3 업로드 실패, 파일 크기 제한 오류
```
S3 upload failed
File size exceeded
AWS credentials error
```

**실제 구현된 해결책**:
```java
// S3 업로드 서비스 (S3Service.java)
public String uploadFile(MultipartFile file, String fileName) {
    if (!cloudAwsEnabled) {
        // AWS가 비활성화된 경우 Mock 서비스 사용
        return mockS3Service.uploadFile(file, fileName);
    }
    
    try {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(fileName)
            .contentType(file.getContentType())
            .build();
        
        s3Client.putObject(putObjectRequest, 
            RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        
        return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName;
        
    } catch (Exception e) {
        log.error("S3 파일 업로드 실패: {}", e.getMessage());
        throw new RuntimeException("파일 업로드에 실패했습니다.");
    }
}
```

### 실제 프로젝트 특징
- **개발/운영 환경 분리**: `cloud.aws.enabled=false`로 설정하여 개발 시 Mock 서비스 사용
- **Redis 기반 저장소**: JWT 토큰 블랙리스트 관리, 세션 데이터 저장
- **Lock-Free 동시성**: ConcurrentHashMap을 활용한 티어별 매치 대기열 관리
- **티어 기반 매칭**: 사용자 티어에 따른 공정한 랜덤 매치 시스템
- **상태 기반 예약 관리**: PENDING → CONFIRMED → COMPLETED/CANCELLED 플로우
- **SMS 로그 관리**: 발송 성공/실패 기록 저장

## 🤝 기여도
- **Backend**: 100% 본인 개발
  - Spring Boot 기반 RESTful API 설계 및 구현
  - JWT 인증/인가 시스템 구축
  - 도메인 주도 설계 (DDD) 적용
  - 성능 최적화 및 보안 강화
  
- **Frontend**: Claude AI 활용
  - React + TypeScript 기반 SPA 구현
  - 현대적인 UI/UX 디자인
  - 반응형 웹 디자인

## 📱 주요 화면
- 사용자 인증 (로그인/회원가입)
- 팀 관리 대시보드
- 매치 생성/참여
- 구장 검색/예약
- 실시간 알림

## 🎯 향후 계획
- [ ] 팀 이름 중복 검증 로직 구현
- [ ] 예약 시간 충돌 검증 강화
- [ ] 벤더 관리 시스템 완성
- [ ] 테스트 커버리지 확대
- [ ] 캐싱 전략 확장
- [ ] 실시간 채팅 기능
- [ ] 모바일 앱 개발

## 📞 문의
프로젝트에 대한 문의사항이 있으시면 언제든 연락해주세요.

---
*축구를 사랑하는 모든 분들을 위한 플랫폼입니다. ⚽*