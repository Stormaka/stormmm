# VibePlayer Android Project

Đây là cấu trúc dự án Android Studio sử dụng ngôn ngữ **Java**.

## Cấu trúc thư mục:
- `app/src/main/java/com/example/vibeplayer/`: Chứa mã nguồn Java (`MainActivity.java`, `MyService.java`).
- `app/src/main/res/layout/`: Chứa tệp giao diện XML (`activity_main.xml`).
- `app/src/main/AndroidManifest.xml`: Khai báo quyền và Service.
- `build.gradle`, `settings.gradle`: Các tệp cấu hình build.

## Lưu ý quan trọng:
1. **Nhạc nền:** Bạn cần thêm một tệp nhạc (ví dụ: `country_rock.mp3`) vào thư mục `app/src/main/res/raw/` để mã nguồn có thể tìm thấy `R.raw.country_rock`.
2. **Quyền thông báo:** Trên Android 13+, bạn cần yêu cầu quyền `POST_NOTIFICATIONS` lúc runtime để hiển thị thông báo.
3. **Internet:** Đã khai báo quyền Internet trong Manifest để tải ảnh từ URL.

Bạn có thể sao chép các tệp này vào một dự án Android Studio mới để chạy thử.
