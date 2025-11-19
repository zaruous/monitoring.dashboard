![image](https://github.com/user-attachments/assets/00755493-1f63-4855-9dc2-83775c5c6c30)

# 인터페이스 모니터링 대시보드

## 1. 개요

본 프로젝트는 시스템 인터페이스의 상태, 스케줄 실행 현황, 데이터 변동 및 서비스 에러를 실시간으로 모니터링하는 데스크톱 및 웹 대시보드 애플리케이션입니다. JavaFX를 사용하여 풍부한 사용자 경험의 데스크톱 UI를 제공하며, Javalin 기반의 내장 웹 서버를 통해 웹 브라우저에서도 핵심 정보를 확인할 수 있습니다.

## 2. 주요 기능

### 공통 기능
- **데이터 제공**: `DataProvider` 인터페이스를 통해 데이터 소스를 유연하게 교체할 수 있습니다. (현재 `AkcDataProvider`, `SqliteDataProvider` 구현)
- **리포트 생성**: 모니터링 데이터를 기반으로 Markdown 또는 HTML 형식의 보고서를 생성합니다.
- **플러그인 아키텍처**: `plugin` 디렉토리에 `.jar` 파일을 추가하여 기능을 동적으로 확장할 수 있습니다.

### JavaFX 대시보드
- **전체 인터페이스 현황**: 성공, 실패, 재시도 등 인터페이스의 전체 상태를 시각적으로 표시합니다.
- **스케줄 모니터링**: 등록된 스케줄의 실행 상태와 이력을 추적합니다.
- **데이터 변동률**: 주요 테이블의 데이터 로우 수 변화를 추적하여 변동률을 표시합니다.
- **서비스 에러 모니터링**: 발생한 서비스 에러의 목록과 횟수를 집계하여 보여줍니다.
- **동적 새로고침**: '새로고침' 버튼을 통해 모든 데이터를 최신 상태로 업데이트합니다.
- **플러그인 뷰**: 로드된 플러그인의 UI를 메인 화면에 표시합니다.

### 웹 대시보드
- **실시간 API**: REST API를 통해 모니터링 데이터를 JSON 형식으로 제공합니다.
  - `/api/status/overall`: 전체 상태 현황
  - `/api/data/fluctuation`: 데이터 변동률
  - `/api/errors`: 서비스 에러 목록
  - `/api/schedules`: 스케줄 실행 현황
- **웹 UI**: `public` 디렉토리의 HTML, CSS, JS 파일을 통해 기본적인 웹 기반 모니터링 화면을 제공합니다. (기본 포트: `10024`)

### 배치 모드
- **자동 에러 리포팅**: 배치 모드로 실행 시 서비스 에러를 감지하여 자동으로 HTML 보고서를 생성합니다.

## 3. 시스템 아키텍처

- **UI**: JavaFX, FXML
- **Web Server**: Javalin
- **Database**: SQLite (기본), MSSQL 지원
- **Build**: Apache Maven
- **Runtime**: Java 21 (JLink를 통해 Custom Runtime Image 생성)

## 4. 실행 방법

### 사전 요구사항
- JDK 21 이상
- Apache Maven

### 빌드
프로젝트 루트 디렉토리에서 다음 명령어를 실행하여 빌드합니다.
```bash
mvn clean package
```
빌드가 성공하면 `target` 디렉토리에 실행 가능한 `jlink-image`와 `modules`가 생성됩니다.

### 실행

#### 1. JavaFX 애플리케이션 실행
`target` 디렉토리에 생성된 `runme.bat` 파일을 실행합니다.
```bash
# target/runme.bat 내부 명령어
start jlink-image\bin\org.kyj.fx.bat
```

#### 2. 웹 애플리케이션 실행
다음 명령어를 통해 내장 웹 서버를 실행할 수 있습니다.
```bash
java -p target/modules -m monitoring.dashboard/org.kyj.fx.monitoring.dashboard.web.WebApp
```
서버가 실행되면 웹 브라우저에서 `http://localhost:10024` 로 접속하여 확인할 수 있습니다.

#### 3. 배치 모드 실행
서비스 에러를 확인하고 HTML 리포트를 생성하려면 다음 명령어를 사용합니다.
```bash
java -p target/modules -m monitoring.dashboard/org.kyj.fx.monitoring.dashboard.InterfaceMonitoringDashboardApp batch
```

## 5. 설정

- **데이터베이스 연결**: `.config/.AkcDataProvider.properties` 파일이 없다면, 새로 만들고 데이터베이스 연결 정보를 설정할 수 있습니다. (프로그램 첫 실행시 자동으로 더미파일이 생성됨.)
- 
```
db.password=
db.url=
db.user=
```
- **데이터 소스 변경**: `InterfaceMonitoringDashboardApp.java` 와 `WebApp.java` 에서 `setDataProvider` 메소드를 호출하여 다른 데이터 제공자(예: `SqliteDataProvider`)로 변경할 수 있습니다.

## 6. 플러그인 시스템

1. `MonitoringPlugin` 인터페이스를 구현한 커스텀 플러그인 `.jar` 파일을 생성합니다.
2. 생성된 `.jar` 파일을 `plugin` 디렉토리에 복사합니다.
3. 애플리케이션을 실행하면 'Plugins' 메뉴에 플러그인이 자동으로 로드됩니다.