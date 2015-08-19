# Bảo mật API cho WebService

## Sử dụng SecurityContext
1. Cơ chế 
  - Server: Database lưu trữ cơ sở dữ liệu về người dùng (username, password, role). Khi client truy cập tài nguyên trên webservice sẽ gửi thông tin người dùng lên (username, password), server kiểm tra username và password có hợp lệ không và kiểm tra api hoặc resource client yêu cầu có được cho phép không thông qua role của nó. 
  - Khi implement có thể tự làm hoặc cấu hình cho webserver (mỗi webserver có một cách cấu hình khác nhau), nhưng vẫn là lưu cơ sở dữ liệu về và role database user.

2. Đặc điểm 
  - Điểm mạnh 
    + Cơ chế đơn giản
  - Điểm yếu 
    + Không phù hợp với nhiều mô hình khác nhau (app, user, webservice,...)
    + Phải gửi username và password lên (nguy hiểm, dễ bị cắp)
    + Phải lưu trữ nhiều thứ trên cơ sở dữ liệu 


## Sử dụng token 
1. Cơ chế 
  -
  - 1 token được mã hóa có thể mang thông tin về quyền truy cập, và token phải được mã hóa theo 1 key thì mới là token phù hợp => không thể generate bừa token được nếu không biết key này 
  - Nếu một người sở hữu 1 token do server cung cấp thì người đó có thể sử dụng token để truy cập tài nguyên trên webservice.
  - Client phải giữ bí mật token của mình 
  - Khi client gửi request lên thì chỉ cần gửi token, token phù hợp => đã qua được authen 
    Bóc token ra để được scope (quyền truy cập) => đã biết đc authorization 
  
  Lợi ích 
  - Không phải lưu database authorization 
  - Không phải lưu database users 
  - Không phải query kiểm tra người dùng có trong database không 
  - Không phải query người dùng có những quyền hạn nào, token mang thông tin về quyền hạn 
  - Người dùng mang token thì không biết gì hơn ngoài token, (không biết gì về cơ chế authen của server)
    (Người dùng có thể tạo token với quyền hạn nào đó rồi cung cấp cho bên thứ 3 : app, website, customer...)
  - Người dùng không phải send các parameter như username và password => an toàn 
  - Mỗi token chỉ có một số quyền hạn nhất định, khác với username và password nếu sở hữu 2 cái đó có thể có full quyền hạn
  
  
3. Mã hóa token sử dụng thuật Toán JWT. 
  
