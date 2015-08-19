# Bảo mật API cho WebService

## Sử dụng Database thông thường
### 1. Cơ chế 
  - Server: Database lưu trữ cơ sở dữ liệu về người dùng (username, password, role). Khi client truy cập tài nguyên trên webservice sẽ gửi thông tin người dùng lên (username, password), server kiểm tra username và password có hợp lệ không và kiểm tra api hoặc resource client yêu cầu có được cho phép không thông qua role của nó. 
  - Khi implement có thể tự làm hoặc cấu hình cho webserver (mỗi webserver có một cách cấu hình khác nhau), nhưng vẫn là lưu cơ sở dữ liệu về users và role database user. Cấu hình vất vả nhưng sử dụng thì đơn giản (Dùng annotation)
  - Có thể áp dụng thêm giao thức SSL khi kết nối để tăng tính bảo mật.
  
### 2. Đặc điểm 
  - Điểm mạnh 
    + Cơ chế đơn giản
  - Điểm yếu 
    + Không phù hợp với nhiều mô hình khác nhau (app, user, webservice,...)
    + Phải gửi username và password lên (nguy hiểm, dễ bị cắp)
    + Phải lưu trữ nhiều thứ trên cơ sở dữ liệu 
    + Qúa nhiều request => Qúa nhiều lần query database => Hệ thống bị break.


## Sử dụng token 
### 1. Cơ chế 
Ví dụ đơn giản: Adflex xây dựng Webservice cung cấp các resources như sau 
```
    GET: /adflex/publisher/*                      role: admin
    GET: /adflex/publisher/{publisherid}          role: publisher
    GET: /adflex/advertiser/*                     role: admin
    GET: /adflex/advertiser/{advertiserid}        role: advertiser
```
Ta thiết kế webservice của ta như sau: Mỗi publisher hoặc advertiser khi đăng kí sử dụng API của AdFlex sẽ thực hiện các bước như sau: Đăng nhập adflex.vn, đăng kí dịch vụ sử dụng API, sau khi đăng kí sẽ nhận được 1 chuỗi token nào đó ví dụ:
    
```
eyJhbGciOiJBMTI4S1ciLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0.qlLiOYvX_fT6-JX0rk0vBzzBQ1ImYyk5P3p0ecCF6o-Zd0WurYm3zQ.x4vQ6zgOBkKMq1XafQ4NRw.BlZYafA-VRrlgz__Fh2Xs1wFeaueVTkut8l8FzVcnQ7CXtnWfnuzypvUxJkhqbHw.KJVKwetSec51-QemtpNz9Q
```

Token này thực ra kết qủa của việc encode 1 chuỗi JSON dạng như sau (ví dụ):
    
```json
    {
      "id": "hycongtu",
      "role": "publisher"
    }
```
   Server có thể encode và decode chuỗi json này sử dụng 1 key riêng mà chỉ AdFlex mới biết, ví dụ "adflexewayadflexeway112233". Key này sẽ không ai biết ngoài đội hệ thống của AdFlex. 1 chuỗi json khi được encode bằng 1 key nào đó thì phải sử dụng key đó để decode.
  
   Vậy hycongtu đã có chuỗi token của riêng mình. Token này được sử dụng bằng cách đặt trong header "Authorization" của request. Khi request gửi lên server, server sẽ lại sử dụng key ""adflexewayadflexeway112233" để convert token này thành chuỗi json ban đầu. Khi đó server sẽ biết được client là ai, client là kiểu khách hàng gì và có những quyền gì, từ đó sẽ quyết định xem API và resource mà client yêu cầu truy cập có trong thẩm quyền không.
    
   Qua đây có thể thấy token có thể được sử dụng rất linh hoạt và có những đặc điểm sau đây:
    
### 2. Token là gì
    
  - Token là 1 chuỗi mang thông tin được encode theo 1 paraphase nào đó mà người tạo ra nó chọn. Chỉ ai nắm giữ cách mã hóa và paraphase này mới có thể decode được token. Trong trường hợp trên, AdFlex chọn key là "adflexewayadflexeway112233" và giữ thật bí mật key này. Một trong các cách mã hóa phổ biến cho việc này là JWT(JSON Web Token) sẽ trình bày phần sau.

  - Người sử dụng webservice sẽ được nhận 1 token khi đăng kí với nhà cung cấp dịch vụ webservice (Qúa trình nhận token này có thể thông qua đăng nhập hoặc nhiều phương thức khác nhau, có thể làm tự động).
  
  - Client tốt nhất nên giữ bí mật token của riêng mình. (Ví dụ publisher nên giữ token của mình nếu không muốn người khác có thể truy cập được như họ).
  
  - Trong mô hình này, token sẽ mang thông tin về role của người yêu cầu truy cập. Token không cần thiết phải mang thông tin user. Vì vậy server phản ứng lại client theo những thông tin có trong token mà thôi.
  
  - Nếu một người sở hữu 1 token do server cung cấp thì người đó có thể sử dụng token để truy cập tài nguyên trên webservice từ nhiều thiết bị và ứng dụng khác nhau.
  
  - Thực chất khi 1 token gửi lên, server decode nó theo key chỉ server xác định từ trước. Nếu không decode được tức token này là token giả mạo. Qúa trình này thực chất giống qúa trình authentication.
   
  - Decode được token này lấy được thông tin về role. Qúa trình này thực chất giống qúa trình authorization.
 
  - Thậm chí, người sử dụng có thể tạo các token khác nhau mang những thẩm quyền khác nhau để sử dụng cho các ứng dụng khác nhau. Để bảo mật, token có thể đặt được thời gian sống. Sau thời gian sống này người dùng sẽ phải request 1 token mới.  

### 3. Đặc điểm
  - Điểm mạnh:
    + Phù hợp với nhiều mô hình khác nhau.
    + Không phải lưu trữ roles của người dùng trên database
    + Không phải query kiểm tra người dùng có trong database không 
    + Không phải query người dùng có những quyền hạn nào, token mang thông tin về quyền hạn 
    + Người dùng mang token thì không biết gì hơn ngoài token, (không biết token mang những thông tin gi,  không biết gì về cơ chế authentication của server, hoặc biết cũng không có cách nào hack nếu không có key của server)
    + Người dùng có thể tạo token với quyền hạn nào đó rồi cung cấp cho bên thứ 3 sử dụng hoặc sử dụng token cho những ứng dụng công ty tự build.
    + Người dùng không phải send các parameter như username và password => an toàn 
    + Mỗi token chỉ có một số quyền hạn nhất định, người dùng không phải qúa lo nếu bị đánh cắp token thay vì bị đánh cắp username, password.
    + Hạn chế nhiều lần request database, tăng tốc độ xử lý.
  
  
### 4. Mã hóa token sử dụng thuật toán JWT. 

**Wesite**: http://jwt.io/

Simple java library, easy to use. Example:

**Encode**: 
```java
        // create key to encode
        Key key = new AesKey("adflexeway112233".getBytes());
        
        // create a token encryption
        JsonWebEncryption jwe = new JsonWebEncryption();
        
        // set json object we want to encode
        jwe.setPlaintext(new JSONObject().put("_id", "hycongtu").put("role","publisher").toString());
        
        // encode
        jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.A128KW);
        jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
        jwe.setKey(key);
        String serializedJwe = jwe.getCompactSerialization();
        System.out.println("Serialized Encrypted JWE: " + serializedJwe);
        
        // send it to server
        ClientResponse response = webResource.path("campaign/test")
                .header("Authorization", "Bearer "+serializedJwe)
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
```

**Decode**
```java
        // get token
        String bearer = headers.getRequestHeader("Authorization").get(0);
        
        // we know this key before, right ?
        Key key = new AesKey("adflexeway112233".getBytes());
        JsonWebEncryption jwe = new JsonWebEncryption();
        jwe.setKey(key);
        
        
        try{
            // good token
            jwe.setCompactSerialization(bearer.split(" ")[1]);
            
            // return to client its token
            return Response.ok(jwe.getPayload()).build();
        } catch (JoseException e) {
            // not match => fraud token
            return Response.status(302)
                    .entity("Token fail")
                    .build();
        }
```
  
