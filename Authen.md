# Authentication 

1. Cách 1: Sử dụng database lưu định danh người dùng (username, password) cùng với type của người dùng (quyền truy cập khác nhau với mỗi type khác nhau)
  - Khi yêu cầu truy cập tài nguyên trên webservice, client gửi định danh người dùng lên và yêu cầu truy cập
  - Server check trong database và kiểm tra type người dùng này có được quyền truy cập đó không

2. Cách 2: Sử dụng token
  - 1 token được mã hóa có thể mang thông tin về quyền truy cập, và token phải được mã hóa theo 1 key thì mới là token phù hợp => không thể generate bừa token được nếu không biết key này 
  - Nếu một người sở hữu 1 token do server cung cấp thì người đó có thể sử dụng token để truy cập tài nguyên trên webservice.
  - Client phải giữ bí mật token của mình 
  - Khi client gửi request lên thì chỉ cần gửi token, token phù hợp => đã qua được authen 
    Bọc token ra để được scope (quyền truy cập) => đã biết đc authorization 
  
  Lợi ích 
  - Không phải lưu database authorization 
  - Không phải lưu database users 
  - Không phải query kiểm tra người dùng có trong database không 
  - Không phải query người dùng có những quyền hạn nào, token mang thông tin về quyền hạn 
  - Người dùng mang token thì không biết gì hơn ngoài token, (không biết gì về cơ chế authen của server)
  - Người dùng không phải send các parameter như username và password => an toàn 
  - Mỗi token chỉ có một số quyền hạn nhất định, khác với username và password nếu sở hữu 2 cái đó có thể có full quyền hạn
  
  
3. Mã hóa token sử dụng thuật Toán JWT. 
  
