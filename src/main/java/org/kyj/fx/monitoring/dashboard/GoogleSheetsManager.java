package org.kyj.fx.monitoring.dashboard;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class GoogleSheetsManager {

    private static final String APPLICATION_NAME = "Monitoring Dashboard";
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    
    // 인증 정보를 저장할 폴더
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    // API 스코프: 읽기 전용
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    
    // `src/main/resources` 폴더에 위치한 인증 파일
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * OAuth 2.0 인증을 통해 Credential 객체를 생성하고 반환합니다.
     * @return 인증된 Credential 객체
     * @throws IOException 파일 입출력 오류 발생 시
     * @throws GeneralSecurityException 보안 관련 오류 발생 시
     */
    private Credential getCredentials() throws IOException, GeneralSecurityException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        // credentials.json 파일 로드
        InputStream in = GoogleSheetsManager.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // 인증 흐름 설정
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        // 로컬 서버를 통해 사용자 인증 진행
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /**
     * 지정된 스프레드시트와 범위에서 데이터를 가져옵니다.
     * @param spreadsheetId 구글 스프레드시트 ID
     * @param range 데이터를 가져올 범위 (예: "Sheet1!A1:B2")
     * @return 시트에서 읽어온 데이터 (2차원 리스트)
     * @throws IOException 파일 입출력 오류 발생 시
     * @throws GeneralSecurityException 보안 관련 오류 발생 시
     */
    public List<List<Object>> getSheetData(String spreadsheetId, String range) throws IOException, GeneralSecurityException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        
        // 인증 정보 가져오기
        Credential credential = getCredentials();
        
        // Sheets 서비스 객체 생성
        Sheets service = new Sheets.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        // 데이터 요청 및 결과 반환
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();

        return response.getValues();
    }
}
