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




## 개발 내용

## 마치며
### 1. 후기

프로젝트 초기에 조금 이 프로젝트에 대한 걱정도 됐습니다. 팀원 한 분이 연락두절 후 그만둬버리셔서 당황하기도 했지만 
협업 시에 나머지 팀원과 의사소통이 원만하게 이루어졌고, 프로젝트 중요 결정사항에서의 의견취합, 서로 부족한 부분을 메꿔주거나 
파트분배 방식, 기능 몇 개를 포기하는 그것으로 잘 해결한 것 같습니다.
그럼에도 안드로이드 개발이 처음이었기 때문에 UI 디자인, 데이터베이스 연동, 다양한 안드로이드 컴포넌트를 활용하는데 시간이 오래 걸리긴 했습니다. 
예기치 않은 버그도 포함해서요. 하지만 이 과정에서 많은 것을 배웠습니다. 화면 전환 시 생명주기와 데이터 이동 등 안드로이드 개발에 대한 이해도가 올라간 것 같습니다. 
안드로이드 앱 개발 프로젝트는 처음이었지만 좋은 팀원분들을 만난 덕분에 어느 정도 잘 마무리한 것 같습니다.

끝까지 읽어주셔서 감사합니다.
