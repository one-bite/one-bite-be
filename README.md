# Spaghetti-be

- v0.1.0 : Oauth 로그인 기능, 문제 풀이 기능 추가
- v0.2.0 : 데이터베이스 연동 로직 추가
- v0.3.0 : 사용자 인증 로직 추가
```
spaghetti-be-1
├─ docker-compose.yml
├─ dockerfile
├─ gradle
│  └─ wrapper
│     ├─ gradle-wrapper.jar
│     └─ gradle-wrapper.properties
├─ gradlew
├─ gradlew.bat
├─ README.md
└─ src
   ├─ main
   │  ├─ java
   │  │  └─ code
   │  │     └─ rice
   │  │        └─ bowl
   │  │           └─ spaghetti
   │  │              ├─ config
   │  │              │  ├─ CorsConfig.java
   │  │              │  ├─ JwtAuthenticationFilter.java
   │  │              │  ├─ NotFoundApiFilter.java
   │  │              │  ├─ RedisConfig.java
   │  │              │  ├─ SecurityConfig.java
   │  │              │  └─ SwaggerConfig.java
   │  │              ├─ controller
   │  │              │  ├─ crud
   │  │              │  │  ├─ BadgeCrudController.java
   │  │              │  │  ├─ ProblemCrudController.java
   │  │              │  │  ├─ RankCrudController.java
   │  │              │  │  ├─ TopicCrudController.java
   │  │              │  │  ├─ UserProblemHistoryCrudController.java
   │  │              │  │  └─ UserTopicCrudController.java
   │  │              │  ├─ OAuthController.java
   │  │              │  ├─ SubmitController.java
   │  │              │  ├─ TestController.java
   │  │              │  └─ UserController.java
   │  │              ├─ dto
   │  │              │  ├─ badge
   │  │              │  │  ├─ BadgeRequest.java
   │  │              │  │  └─ BadgeResponse.java
   │  │              │  ├─ JwtTokenDto.java
   │  │              │  ├─ problem
   │  │              │  │  ├─ ProblemDetailResponse.java
   │  │              │  │  ├─ ProblemRequest.java
   │  │              │  │  ├─ ProblemResponse.java
   │  │              │  │  └─ ProblemSimpleResponse.java
   │  │              │  ├─ rank
   │  │              │  │  ├─ RankRequest.java
   │  │              │  │  ├─ RankResponse.java
   │  │              │  │  └─ RankSimpleResponse.java
   │  │              │  ├─ request
   │  │              │  │  ├─ LoginRequest.java
   │  │              │  │  └─ SubmitRequest.java
   │  │              │  ├─ response
   │  │              │  │  ├─ GoogleErrorResponse.java
   │  │              │  │  ├─ GoogleTokenResponse.java
   │  │              │  │  ├─ GoogleUserInfoResponse.java
   │  │              │  │  ├─ SimpleOkResponse.java
   │  │              │  │  └─ SubmitResponse.java
   │  │              │  ├─ streak
   │  │              │  │  └─ StreakInfoResponse.java
   │  │              │  ├─ topic
   │  │              │  │  ├─ TopicRequest.java
   │  │              │  │  └─ TopicResponse.java
   │  │              │  ├─ user
   │  │              │  │  ├─ UserCurrentResponse.java
   │  │              │  │  ├─ UserPatchRequest.java
   │  │              │  │  ├─ UserSimpleResponse.java
   │  │              │  │  └─ UserTodayProblemResponse.java
   │  │              │  ├─ userproblemhistory
   │  │              │  │  ├─ UserProblemHistoryRequest.java
   │  │              │  │  ├─ UserProblemHistoryResponse.java
   │  │              │  │  └─ UserProblemHistorySummaryResponse.java
   │  │              │  ├─ userprogress
   │  │              │  │  └─ UserProgressResponse.java
   │  │              │  └─ usertopic
   │  │              │     └─ UserTopicResponse.java
   │  │              ├─ entity
   │  │              │  ├─ Admin.java
   │  │              │  ├─ Badge.java
   │  │              │  ├─ Course.java
   │  │              │  ├─ Problem.java
   │  │              │  ├─ Rank.java
   │  │              │  ├─ Streak.java
   │  │              │  ├─ TodayProblem.java
   │  │              │  ├─ Topic.java
   │  │              │  ├─ User.java
   │  │              │  ├─ UserBadge.java
   │  │              │  ├─ UserProblemHistory.java
   │  │              │  ├─ UserProgress.java
   │  │              │  ├─ UserProgressId.java
   │  │              │  ├─ UserTopic.java
   │  │              │  └─ UserTopicId.java
   │  │              ├─ exception
   │  │              │  ├─ BaseExceptionIF.java
   │  │              │  ├─ GlobalExceptionHandler.java
   │  │              │  ├─ InternalServerError.java
   │  │              │  ├─ InvalidRequestException.java
   │  │              │  ├─ JwtAccessDeniedHandler.java
   │  │              │  ├─ JwtAuthenticationEntryPoint.java
   │  │              │  ├─ NotFoundException.java
   │  │              │  └─ NotImplementedException.java
   │  │              ├─ mapper
   │  │              │  ├─ BadgeMapper.java
   │  │              │  ├─ ProblemMapper.java
   │  │              │  ├─ RankMapper.java
   │  │              │  ├─ StreakMapper.java
   │  │              │  ├─ TopicMapper.java
   │  │              │  ├─ UserMapper.java
   │  │              │  ├─ UserProblemHistoryMapper.java
   │  │              │  ├─ UserProgressMapper.java
   │  │              │  └─ UserTopicMapper.java
   │  │              ├─ repository
   │  │              │  ├─ AdminRepository.java
   │  │              │  ├─ BadgeRepository.java
   │  │              │  ├─ CourseRepository.java
   │  │              │  ├─ ProblemRepository.java
   │  │              │  ├─ RankRepository.java
   │  │              │  ├─ TodayProblemRepository.java
   │  │              │  ├─ TopicRepository.java
   │  │              │  ├─ UserBadgeRepository.java
   │  │              │  ├─ UserProblemHistoryRepository.java
   │  │              │  ├─ UserProgressRepository.java
   │  │              │  ├─ UserRepository.java
   │  │              │  └─ UserTopicRepository.java
   │  │              ├─ service
   │  │              │  ├─ AdminService.java
   │  │              │  ├─ AuthService.java
   │  │              │  ├─ BadgeService.java
   │  │              │  ├─ CourseService.java
   │  │              │  ├─ GoogleLoginService.java
   │  │              │  ├─ GradingService.java
   │  │              │  ├─ ProblemService.java
   │  │              │  ├─ RankService.java
   │  │              │  ├─ RedisService.java
   │  │              │  ├─ StreakService.java
   │  │              │  ├─ TodayProblemService.java
   │  │              │  ├─ TopicService.java
   │  │              │  ├─ UserBadgeService.java
   │  │              │  ├─ UserProblemHistoryService.java
   │  │              │  ├─ UserProgressService.java
   │  │              │  ├─ UserService.java
   │  │              │  └─ UserTopicService.java
   │  │              ├─ SpaghettiApplication.java
   │  │              ├─ TestDataLoader.java
   │  │              └─ utils
   │  │                 ├─ DateUtils.java
   │  │                 ├─ HashSetConverter.java
   │  │                 ├─ JsonNodeConverter.java
   │  │                 ├─ JwtProvider.java
   │  │                 └─ LoginProvider.java
   │  └─ resources
   │     └─ application.properties
   └─ test
      └─ java
         └─ code
            └─ rice
               └─ bowl
                  └─ spaghetti
                     └─ SpaghettiApplicationTests.java

```