# 문제 및 해결방안

## 공공API 데이터를 DB에 저장 작업

### 1. 문제정의
- 여행지 조회 화면 구현 불가

### 2. 사실수집
- 여행관련 기능이 사용될때마다 화면을 구현해야하기 때문에 공공데이터 API 트래픽이 무수히 사용되고 있음을 확인
- 공공데이터 API 데이터를 DB로 저장하지 않을 경우 트래픽 초과로 인한 데이터 조회가 안되는 상황이 발생할 것으로 예상

### 3. 원인추론
- 공공데이터(한국관광공사_관광정보 서비스) API 트래픽 초과로 인한 데이터 조회 불가

### 4. 조사방법결정
- REST API, HTTP기반의 요청, 응답을 처리할 수 있는 라이브러리 **[OkHttp]** 를 활용한 데이터 추출

### 5. 조사방법구현
``` Java
// 관광정보조회 [여행지 DB저장]
    // GET > http://localhost:8080/REST/tourapi/DB/TD
    @GetMapping(value = "/tourapi/DB/TD")
    public Map<String, Object> getTourApiTDDB(
        @RequestParam(name = "page", defaultValue = "1") String page,                   
        @RequestParam("cnt") String cnt,            
        @RequestParam(name = "arrange", defaultValue = "P") String arrange,
        @RequestParam(name = "contentTypeId", defaultValue = "") String contentTypeId,
        @RequestParam("areaCode") String areaCode) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 공공API_저역코드조회 URL
            String url = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?serviceKey="
                    + tourApiKey + "&pageNo=" + page + "&numOfRows=" + cnt + "&MobileApp=AppTest&MobileOS=ETC&arrange="
                    + arrange + "&contentTypeId=" + contentTypeId + "&areaCode=" + areaCode + "&listYN=Y&_type=json";
            
            // OkHttp 클라이언트 객체 생성
            OkHttpClient client = new OkHttpClient();

            // GET 요청 객체 생성
            Request.Builder builder = new Request.Builder().url(url).get();
            Request request = builder.build();

            // OkHttp 클라이언트로 GET 요청 객체 전송
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                // 응답 받아서 처리
                ResponseBody body = response.body();

                if (body != null) {
                    String bodyString = body.string();
                    
                    // body 데이터를 String -> JSON 형태로 변환
                    JSONObject jsonObject = new JSONObject(bodyString);
                    
                    // body 데이터 중 필요 데이터 수집
                    JSONArray j1 = jsonObject.getJSONObject("response").getJSONObject("body").getJSONObject("items")
                            .getJSONArray("item");
                    for (int i = 0; i < j1.length(); i++) {
                        TD td = new TD();
                        if (!j1.getJSONObject(i).isNull("contentid")){
                            if (!tdRepository.findById(j1.getJSONObject(i).getInt("contentid")).isPresent()) {
                                if (!j1.getJSONObject(i).isNull("mapx") && !j1.getJSONObject(i).isNull("mapy")) {
                                    td.setXlocation(j1.getJSONObject(i).getFloat("mapx"));
                                    td.setYlocation(j1.getJSONObject(i).getFloat("mapy"));
                                    td.setCode(j1.getJSONObject(i).getInt("contentid"));
                                    if (!j1.getJSONObject(i).isNull("title"))
                                        td.setTitle(j1.getJSONObject(i).getString("title"));
                                    if (!j1.getJSONObject(i).isNull("addr1"))
                                        td.setAddr(j1.getJSONObject(i).getString("addr1"));
                                    if (!j1.getJSONObject(i).isNull("areacode"))
                                        if (cityRepository.findById(j1.getJSONObject(i).getInt("areacode")).isPresent())
                                            td.setCity(cityRepository.findById(j1.getJSONObject(i).getInt("areacode"))
                                                    .orElseThrow());
                                    if (!j1.getJSONObject(i).isNull("firstimage"))
                                        td.setFirstimage(j1.getJSONObject(i).getString("firstimage"));
                                    if (!j1.getJSONObject(i).isNull("tel"))
                                        td.setTel(j1.getJSONObject(i).getString("tel"));
                                    if (!j1.getJSONObject(i).isNull("contenttypeid"))
                                        if (typeRepository.findById(j1.getJSONObject(i).getInt("contenttypeid"))
                                                .isPresent())
                                            td.setTdtype(typeRepository.findById(j1.getJSONObject(i).getInt("contenttypeid")).orElseThrow());
                                }
                                tdRepository.save(td);
                            }
                        }
                    }
                }
            } else
                System.err.println("Error Occurred");

            map.put("status", 200);
            map.put("data", "관광정보 저장에 성공하였습니다.");
        } catch (Exception e) {
            // 에러를 출력한다.
            e.printStackTrace();
            map.put("status", e.hashCode());
            map.put("data", "관광정보 저장에 실패하였습니다.");
        }
        // 결과 값 리턴
        return map;
    }
```
- `OkHttpClient client = new OkHttpClient();`를 입력하여 client 객체를 생성한다.
- `Request.Builder builder = new Request.Builder().url(url).get();`를 입력하여 GET 요청 객체를 생성한다.
- `Response response = client.newCall(request).execute();`를 통해 OkHttp 클라이언트로 GET 요청 객체를 전송한다.
- `ResponseBody body = response.body();`와 같이 응답을 받는다.
- String 정보를 JSON타입으로 변형 후 필요한 값을 추출하여 DB에 저장한다.

### 6. 문제해결
- DB에 저장된 데이터를 이용하여 Front에 Data제공하므로 공공데이터 트래픽 사용 최소화

**[결과화면]**

![image](https://user-images.githubusercontent.com/82141580/147756778-4a36d16a-0763-4450-845f-6bd03331ee93.png)


---

# 향후 변경 혹은 개발시 주의사항

## 1. 쿼리에서 로직 빼기
- 코드의 가독성 증가
  - 전반적인 로직이 더 잘 드러남.
- 쿼리의 단순화
  - 데이터베이스 리소스 최소화
- 주요 로직에 대한 단위테스트 검증 가능함.
- 참고했던 자료(https://www.youtube.com/watch?v=fnH_SR3n9Ew)
