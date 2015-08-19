# Bảo mật API cho WebService

## Sử dụng SecurityContext
### 1. Cơ chế 
  - Server: Database lưu trữ cơ sở dữ liệu về người dùng (username, password, role). Khi client truy cập tài nguyên trên webservice sẽ gửi thông tin người dùng lên (username, password), server kiểm tra username và password có hợp lệ không và kiểm tra api hoặc resource client yêu cầu có được cho phép không thông qua role của nó. 
  - Khi implement có thể tự làm hoặc cấu hình cho webserver (mỗi webserver có một cách cấu hình khác nhau), nhưng vẫn là lưu cơ sở dữ liệu về và role database user. Cấu hình vất vả nhưng sử dụng thì đơn giản (Dùng annotation)
  - Có thể áp dụng thêm giao thức SSL khi kết nối để tăng tính bảo mật.
  
### 2. Đặc điểm 
  - Điểm mạnh 
    + Cơ chế đơn giản
  - Điểm yếu 
    + Không phù hợp với nhiều mô hình khác nhau (app, user, webservice,...)
    + Phải gửi username và password lên (nguy hiểm, dễ bị cắp)
    + Phải lưu trữ nhiều thứ trên cơ sở dữ liệu 


## Sử dụng token 
### 1. Cơ chế 
  - Token là 1 chuỗi mang thông tin được encode theo 1 paraphase nào đó mà người tạo ra nó chọn. Chỉ ai nắm giữ cách mã hóa và paraphase này mới có thể decode được token.
  - Người sử dụng webservice sẽ được nhận 1 token khi được nhà cung cấp dịch vụ webservice (Qúa trình nhận token này có thể thông qua đăng nhập hoặc nhiều phương thức khác nhau, có thể làm tự động)
  - Token cũng có thể tạo ra do người dùng sau khi đăng nhập và chọn 1 số role truy cập thông tin của chính họ rồi tạo token. Hoặc cũng có thể do nhà cung cấp dịch vụ webservice tự tạo. Ví dụ: Adflex cho mỗi partner 1 token mang id của partner đó và type của partner đó(publisher, advertieser,...).
  - Client tốt nhất nên giữ bí mật token của riêng mình. (Ví dụ publisher nên giữ token của mình nếu không muốn người khác biết thông tin của họ).
  - Trong mô hình này, token sẽ mang thông tin về role của người yêu cầu truy cập. Token không cần thiết phải mang thông tin user. Vì vậy server phản ứng lại client theo role chứ không theo user.
  - Nếu một người sở hữu 1 token do server cung cấp thì người đó có thể sử dụng token để truy cập tài nguyên trên webservice.
  - Thực chất khi 1 token gửi lên, server decode nó theo key chỉ server xác định từ trước. Nếu không decode được tức token này là token giả mạo. Qúa trình này thực chất giống qúa trình authentication.
  - Decode được token này lấy được thông tin về role. Qúa trình này thực chất giống qúa trình authorization.
  - Đặc biệt phù hợp với nguyên lý của webservice.

### 2. Đặc điểm
  - Điểm mạnh:
    + Phù hợp với nhiều mô hình khác nhau.
    + Không phải lưu database authorization 
    + Không phải lưu database users 
    + Không phải query kiểm tra người dùng có trong database không 
    + Không phải query người dùng có những quyền hạn nào, token mang thông tin về quyền hạn 
    + Người dùng mang token thì không biết gì hơn ngoài token, (không biết gì về cơ chế authen của server)
    + Người dùng có thể tạo token với quyền hạn nào đó rồi cung cấp cho bên thứ 3 : app, website, customer...
    + Người dùng không phải send các parameter như username và password => an toàn 
    + Mỗi token chỉ có một số quyền hạn nhất định, khác với username và password nếu sở hữu 2 cái đó có thể có full quyền hạn
  
  
3. Mã hóa token sử dụng thuật Toán JWT. 
  
