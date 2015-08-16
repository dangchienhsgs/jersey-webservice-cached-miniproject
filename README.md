# Webservice Sample by Jersey

## Action:

  - Insert Campaign
    ```
    POST: /campaign
    data: {"campaign_id": "cuulong", "retention_rate":13, "app_key":"appkey", "total_installed":24}
    type: application/json
    ```
  - Query all Campaigns
    ```
    GET: /campaign
    data: None
    ```
  - Query all Campaigns By Id
    """
    GET: /campaign/{campaign_id}
    data: None
    """
## Các phương pháp cache được sử dụng:

### Cache lên memory

  - Cache tất cả các campaigns lên memory dưới dạng ConcurrentHashMap (Hiệu quả hơn HashTable kể từ Java 7) (**campaignList**)

  - Cache tất cả các thay đổi (các campaign được insert) vào 1 cache riêng (Cũng sử dụng ConcurrentHashMap) (**changeList**)

### Cache bằng http

  - Với action **query all campaign**, sử dụng phương pháp so sánh **lastModifiedDate**. Tức là khi request gửi đến.
  sẽ mang theo 1 tag có giá trị là ngày chỉnh sửa gần nhất tại client đã được hash và đặt trong 1 tag. Nếu giá trị này trùng với giá trị lưu tại server (Do đây là toàn bộ campaign nên chỉ có 1 giá trị như thế này), chỉ gửi một thông báo tới client là sử dụng kết quả cũ. Ngược lại thì gửi kết quả mới kèm etag là hashcode của thời gian lưu tại server.

  - Với hành động insert một campaign, sẽ làm thay đổi giá trị **lastModifiedDate** tại server, ta cần update giá trị này mỗi lần insert.

  - Với action **query campaign by id**, ta sẽ lấy giá trị của campaign theo id nếu có 2 cache changeList và campaignList (changeList ưu tiên trước). Hash giá trị này và so sánh với etag của client gửi đến. Cách làm tương tự với hành động
  **query all campaign**.

### Lợi ích

  - Với việc cache bằng http và sử dụng sự hỗ trợ của etag khiến server giảm thiểu bandwidth (thay vì send 1 giá trị cỡ vài KB thì chỉ send 1 giá trị cỡ vài bytes), cùng với đó là giảm thiểu độ trễ trong việc query database

  - Với hành động **query all campaign** không hash toàn bộ kết quả rồi sử dụng etag như **query campaign by id** vì giá trị trả về thường rất lớn, việc hash sẽ tốn computation thay vì hash 1 giá trị bé là lastModifiedDate

  - Với mỗi campaign trả về cũng không sử dụng hash kết quả như hành động **query all campaign** vì lưu lastModifiedDate cho mỗi campaign là không cần thiết và tốn bộ nhớ.

### Phương pháp tránh xung đột

Quá trình insert 1 campaign:

  - Insert campaign đó vào changesList (đặt status là **WAITING**)

  - Insert campaign đó vào campaignList, sau quá trình này sửa lại status của campaign này trong changeList thành **RELOAD**

  - Insert campaign đó vào database, sau quá trình này xóa campaign này đi ở changeList.

  - Điều này giảm thiểu cho việc phải reload toàn bộ database gây tốn thời gian, các quá trình này được xử lý gần như tức thời.
