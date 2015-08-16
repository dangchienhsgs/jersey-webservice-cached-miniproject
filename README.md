# Webservice Sampling by Jersey

## Action:
  - Insert Campaign
  - Query all Campaigns
  - Query all Campaigns By Id

## Các phương pháp cache được sử dụng:
  ### Cache lên memory
  - Cache tất cả các campaigns lên memory dưới dạng ConcurrentHashMap (Hiệu quả hơn HashTable kể từ Java 7) (campaignList)
  - Cache tất cả các thay đổi (các campaign được insert) vào 1 cache riêng (Cũng sử dụng ConcurrentHashMap) (changeList)

  ### Cache bằng http
  - Với action Query All Campaign, sử dụng phương pháp so sánh ngày được chỉnh sửa gần nhất. Tức là khi request gửi đến.
  sẽ mang theo 1 tag có giá trị là ngày chỉnh sửa gần nhất tại client đã được hash và đặt trong etag. Nếu giá trị này trùng với
  giá trị lưu tại server (Do đây là toàn bộ campaign nên chỉ có 1 giá trị như thế này), chỉ gửi một thông báo tới client là sử
  dụng kết quả cũ. Ngược lại thì gửi kết quả mới kèm etag là hashcode của thời gian lưu tại server.
  - Với hành động insert một campaign, sẽ làm thay đổi giá trị lastModifiedDate tại server, ta cần update giá trị này mỗi
  lần insert.
  - Với action "query campaign by id", ta sẽ lấy giá trị của campaign theo id nếu có 2 cache changeList và campaignList (changeList ưu tiên trước). Hash giá trị này và so sánh với etag của client gửi đến. Cách làm tương tự với hành động
  "query all campaign".

  ### Lợi ích
  - Với việc cache bằng http và sử dụng sự hỗ trợ của etag khiến server giảm thiểu bandwidth (thay vì send 1 giá trị cỡ vài KB thì chỉ send 1 giá trị cỡ vài bytes)
  - Với hành động "query all campaign" không hash toàn bộ kết quả rồi sử dụng etag như "query campaign by id" vì giá trị trả về thường rất lớn, việc hash sẽ tốn computation thay vì hash 1 giá trị bé là lastModifiedDate
  - Với mỗi campaign trả về cũng không sử dụng hash kết quả như hành động "query all campaign" vì lưu lastModifiedDate cho mỗi campaign là không cần thiết và tốn bộ nhớ.