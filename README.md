# :paperclip: Transaction Project (가계부 어플)
> 지출과 수입을 쉽게 관리할 수 있도록 하는 가계부 어플리케이션입니다.

<img width="544" alt="스크린샷 2024-07-31 오후 5 59 10" src="https://github.com/user-attachments/assets/63269f12-78b3-4e94-9bfc-6681c5c77c90">

## 목차
- [들어가며](#들어가며)
  - [프로젝트 소개](#1-프로젝트-소개)    
  - [프로젝트 기능](#2-프로젝트-기능)    
  - [사용 기술](#3-사용-기술)   
     - [백엔드](#3-1-백엔드)
     - [프론트엔드](#3-2-프론트엔드)
     - [협업도구](#3-3-협업도구)
  - [실행 화면](#4-실행-화면)


- [구조 및 설계](#구조-및-설계)
  - [패키지 구조](#1-패키지-구조)
  - [DB 설계](#2-DB-설계)
  - [API 설계](#3-api-설계)

- [개발 내용](#개발-내용)

- [마치며](#마치며)
  - [프로젝트 보완사항](#1-프로젝트-보완사항)
  - [후기](#2-후기)

## 들어가며
### 1. 프로젝트 소개

Pay Breeze
쉽고 간편하게 지출과 수입을 관리하고 수입/지출, 날짜, 카테고리별 필터를 통해 쉽게 확인할 수 있는 가계부 어플리케이션입니다.
3명의 팀원들로 구성되어 작업한 팀 프로젝트입니다.

### 2. 프로젝트 기능

프로젝트의 주요 기능은 다음과 같습니다.
- **홈 화면 -** 수입/지출, 날짜, 카테고리별 필터기능, 오늘 금액의 통계 기능
- **달력 화면 -** 달력으로 상세한 날짜를 하루 or 기간별로 조회하는 기능
- **추가 화면 -** 가계부 추가 기능, 카테고리 선택, 카테고리 추가 기능
- **통계 화면 -** 날짜별, 수입/지출 별로 통계를 구해주는 기능
- **추가 화면 -** 카테고리 수정(추가/삭제) 기능, 삭제된 카테고리들이 저장된 가계부 조회 기능

### 3. 사용 기술

#### 3-1 백엔드

##### 통합 개발 환경 (IDE)
- Android Studio Jellyfish 2023.3.1

##### 주요 프레임워크 / 라이브러리
- Java 17
- Kotlin 1.9.0

##### Build Tool
- Gradle 8.6

##### DataBase
- Room 2.5.0
- SQLite

#### 3-2 프론트엔드
- Material 1.12.0
- Material Calendarview 2.0.1

#### 3-3 협업도구
- GitHub
- Slack
- Notion
- Figma


### 4. 실행 화면
  <details>
    <summary>주요 기능 화면</summary>   
       
    
  **1. 홈 화면**   
  <img width="555" alt="스크린샷 2024-07-31 오후 6 39 46" src="https://github.com/user-attachments/assets/ee32ce6b-0c1b-441b-8f2a-d4b4d7649ded">
  
  수입/지출, 기간, 카테고리별 필터로 조회가 가능하다.
     
  **2. 가계부 등록**   
  <img width="515" alt="스크린샷 2024-07-31 오후 6 40 20" src="https://github.com/user-attachments/assets/fd3bf587-b66c-4b2c-acc6-f109f88976f6">
  
  가계부 등록기능, 나만의 카테고리를 추가/삭제가 가능하다.
     
     
  </details>
  <br/>   
 
   
## 구조 및 설계   
   
### 1. 패키지 구조
   
<details>
  
<summary>패키지 구조 보기</summary>   
 

```
📦 src  
 ┣ 📂 androidTest  
 ┣ 📂 main  
 ┃ ┣ 📂 java  
 ┃ ┃ ┗ 📂 com.kei037.pay_breeze_mvc  
 ┃ ┃ ┃ ┗ 📂 data  
 ┃ ┃ ┃ ┃ ┗ 📂 db  
 ┃ ┃ ┃ ┃ ┃ ┗ 📂 dao  
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 CategoryDao  
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 TransactionDao  
 ┃ ┃ ┃ ┃ ┃ ┗ 📂 entity  
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 CategoryEntity  
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 TransactionEntity  
 ┃ ┃ ┃ ┃ ┃ ┗ 📜 AppDatabase  
 ┃ ┃ ┃ ┃ ┗ 📂 repository  
 ┃ ┃ ┃ ┃ ┃ ┣ 📜 CategoryRepository  
 ┃ ┃ ┃ ┃ ┃ ┣ 📜 TransactionRepository  
 ┃ ┃ ┃ ┃ ┗ 📜 MyApplication  
 ┃ ┃ ┃ ┗ 📂 ui  
 ┃ ┃ ┃ ┃ ┗ 📂 addition  
 ┃ ┃ ┃ ┃ ┃ ┣ 📜 AdditionAdapter  
 ┃ ┃ ┃ ┃ ┃ ┣ 📜 AdditionFragment  
 ┃ ┃ ┃ ┃ ┃ ┣ 📜 AdditionRepository  
 ┃ ┃ ┃ ┃ ┃ ┣ 📜 CustomChip  
 ┃ ┃ ┃ ┃ ┃ ┗ 📜 RegisterBottomSheetFragment  
 ┃ ┃ ┃ ┃ ┗ 📂 analysis  
 ┃ ┃ ┃ ┃ ┃ ┣ 📜 AnalysisFragment  
 ┃ ┃ ┃ ┃ ┃ ┣ 📜 AnalysisPageAdapter.kt  
 ┃ ┃ ┃ ┃ ┃ ┣ 📜 ExpenseTransactionsFragment  
 ┃ ┃ ┃ ┃ ┃ ┗ 📜 IncomeTransactionsFragment  
 ┃ ┃ ┃ ┃ ┗ 📂 calender  
 ┃ ┃ ┃ ┃ ┃ ┗ 📂 calenderAdapter  
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 ListItem.kt  
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 ViewType  
 ┃ ┃ ┃ ┃ ┃ ┗ 📂 dacorators  
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 MiddleDateDecorator  
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 SingleDateDecorator  
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 StartEndDateDecorator  
 ┃ ┃ ┃ ┃ ┃ ┗ 📜 CalenderFragment  
 ┃ ┃ ┃ ┃ ┗ 📂 commons  
 ┃ ┃ ┃ ┃ ┃ ┗ 📂 commonsAdapter  
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 CategoryAdapter  
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 EventAdapter  
 ┃ ┃ ┃ ┃ ┃ ┣ 📜 DetailedActivity  
 ┃ ┃ ┃ ┃ ┃ ┣ 📜 EditActivity  
 ┃ ┃ ┃ ┃ ┃ ┣ 📜 EditCategoryActivity  
 ┃ ┃ ┃ ┃ ┃ ┗ 📜 utils.kt  
 ┃ ┃ ┃ ┃ ┗ 📂 home  
 ┃ ┃ ┃ ┃ ┃ ┗ 📂 homeAdapter  
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 HomeItem  
 ┃ ┃ ┃ ┃ ┃ ┗ 📜 HomeFragment  
 ┃ ┃ ┃ ┃ ┗ 📂 setting  
 ┃ ┃ ┃ ┃ ┃ ┗ 📂 settingAdapter  
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 CustomCategoryAdapter  
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 HistoricalTransAdapter  
 ┃ ┃ ┃ ┃ ┃ ┣ 📜 TransactionDiffCallback  
 ┃ ┃ ┃ ┃ ┃ ┣ 📜 CustomCategoryActivity  
 ┃ ┃ ┃ ┃ ┃ ┣ 📜 HistoricalTransActivity  
 ┃ ┃ ┃ ┃ ┃ ┗ 📜 SettingFragment  
 ┃ ┃ ┃ ┗ 📜 MainActivity  
 ┃ ┗ 📂 res  
 ┃ ┗ 📜 AndroidManifest.xml  

 ```
  
 </details>   
 <br/>    
   
     
 ### 2. DB 설계

![DB설계 및 구성](https://github.com/user-attachments/assets/90cff346-c1ab-472d-9997-9e99750ca404)
   
<br/><br/><br/><br/><br/>


======= 해당하지 않는 내용 =======


### 3. API 설계

## 개발 내용

## 마치며(수정해야함)
### 1. 프로젝트 보완사항   

초기에 구상한 기능은 기본적인 CRUD 즉, 게시판에 올라오는 게시글을 대상으로 Create, Read, Update, Delete가 가능한 게시판이었습니다.   
템플릿 엔진으로 Mustache를 선택했는데, 그 이유는 Mustache는 단순히 화면에 데이터를 렌더링 하는 엔진이고   
Logic-less 하기 때문에 View의 역할과 서버의 역할이 명확하게 분리되어 OOP의 5원칙 중 하나인 SRP를 지킬 수 있어    
MVC 설계에서 Model, View, Controller의 역할에 대한 구분도 명확하게 할 수 있겠다는 생각이 들었습니다.   
또한, 다른 템플릿에 비해 빠른 로딩 속도를 자랑하며, xss를 기본적으로 이스케이프 할 수 있다는 장점들에 이끌려 Mustache를 사용하게 되었습니다.   
그러나 게시판 CRUD 기능이 완성되어 갈 때 쯤, 아쉬운 부분이 계속해서 생겨 몇몇 기능들을 추가하게 되었습니다.   
mustache는 로직을 넣을 수 없어 그 과정에 데이터를 렌더링 하기 전 서버에서 전처리를 하거나,    
화면에 표시된 후에 자바스크립트로 후처리를 해줬지만 조금 아쉬운 부분이 몇 가지 남아있다고 생각합니다.   
<details>
  <summary>보완사항</summary>
     
  
- 페이징 처리 및 검색 페이징에서 페이지 번호 활성화
- 페이지 번호는 10페이지 단위로 보여주기
- 페이지 처음, 끝으로 이동하는 버튼
- 생성, 수정시간 format 설정 varchar > datetime
- 다른 사용자와 자신의 댓글이 댓글란에 있을때 자신의 댓글만 수정,삭제 버튼 보이기
  
</details>   

추후에 브랜치를 나눠 Mustache에서 Thymeleaf로 조금씩 바꾸며 프로젝트 완성도를 높이고, 고도화 할 계획에 있습니다.   
   
   <details>
  <summary>추가할 기능 </summary>
     
  
- 댓글 페이징 처리
- 쿠키나 세션을 이용해 조회수 중복 카운트 방지
- 파일 업로드 기능 추가
- 좋아요 기능 추가
  
</details>  


### 2. 후기(수정해야함)

혼자 독학하며 처음 만들어본 프로젝트이기 때문에,   
공부한 내용을 사용해보는 설렘만큼이나 부족한 부분에 대한 아쉬움도 많이 남았습니다.   
효율적인 설계를 위해 고민하고 찾아보며 실제로 많이 공부할 수 있었던 부분도 많았습니다.   
책이나 블로그, 강의로 공부한 예제에서 납득했던 부분들은 실제로 코드를 짜면서 다양한 애로 사항을 마주했고   
'이 로직은 이 단계에서 처리하는게 맞는가', '각 레이어간 데이터 전달은 어떤 방식이든 DTO로 하는게 맞는가' 등   
여러 고민에 빠져 헤맨적도 있었지만, 다행히 결과는 대부분 최선을 찾았었던 것 같습니다.   
그리고 내가 만든 코드를 남에게 보여줬을 때, 누군가 코드의 근거를 물어본다면   
과연 자신 있게 나의 생각을 잘 얘기할 수 있을까 라는 생각을 굉장히 많이 하게 되었습니다.   
그래서 하나를 구현할 때 '이렇게 구현 하는 것이 과연 최선인가', '더 나은 Best Practice는 없을까'   
스스로 의심하고 고민하게 되는 습관을 가지게 되었습니다.   

두 번째로 기술적인 부분에서 더 공부하고 싶은 '방향'을 찾을 수 있었습니다.   
이번 프로젝트는 저에게 좋은 경험이 되었고, 저의 부족한 부분을 스스로 알 수 있는 좋은 계기가 되었습니다.   
부족한 부분에 대해 스스로 인지하고 있고, 더 깊게 공부하며 스스로 발전할 수 있는 '방향'을 다시한번 찾을 수 있게 되었습니다.   
이를 통해 더 나은 웹 애플리케이션을 만들 수 있을 것 같다는 자신감도 생겼습니다.   

끝까지 읽어주셔서 감사합니다.
