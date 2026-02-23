# 🎓 Personalized Learning Hub: Full-Stack Educational Platform

Bu proje, öğrencileri ve eğitmenleri dijital ortamda bir araya getiren; oturum rezervasyonu, gelişim takibi, materyal paylaşımı ve rol tabanlı yönetim modüllerine sahip kapsamlı bir **Full-Stack Web Uygulamasıdır**. 

Proje, hem Frontend hem de Backend süreçlerinin tek bir çatı altında (Monorepo) profesyonel yazılım mühendisliği pratikleriyle (Clean Code, N-Tier Architecture) yönetildiği bir mimariye sahiptir.

---

## 🏗️ Sistem Mimarisi ve Mühendislik Yaklaşımı

Sistem, veri güvenliğini ve UI performansını maksimize etmek için iki bağımsız modül olarak tasarlanmıştır:

* **Backend (Spring Boot):** Katmanlı mimari (Controller-Service-Repository) kullanılarak inşa edilmiştir. Veritabanı Entity'leri doğrudan dışarı açılmamış; veri transferi ve validasyonlar tamamen **DTO (Data Transfer Object)** ve Mapper katmanları üzerinden sağlanmıştır.
* **Frontend (React.js):** Modern SPA (Single Page Application) mimarisiyle geliştirilmiştir. Global durum yönetimi için `Context API`, ağ istekleri için merkezi `Axios` interceptor'ları ve rol tabanlı yetkilendirme için "Protected Routes" kurgulanmıştır.

---

## 🌟 Temel Modüller (Role-Based Access Control)

Sistem; Öğrenci, Eğitmen ve Admin olmak üzere 3 farklı yetkilendirme seviyesine ve özel arayüzlere (Layouts) sahiptir:

### 👨‍🎓 Öğrenci Paneli (Student)
* **Session Booking:** Eğitmenlerin müsaitlik (availability) takvimlerine göre birebir ders rezervasyonu oluşturma.
* **Skill Assessment & Progress:** Yetenek testlerine katılma ve kişisel gelişim sürecini grafiksel arayüzlerle takip etme.
* **Resource Library:** Eğitmenler tarafından paylaşılan ders materyallerine (PDF, PPTX) erişim ve indirme.

### 👩‍🏫 Eğitmen Paneli (Tutor)
* **Availability Management:** Takvim üzerinden müsaitlik saatlerini dinamik olarak belirleme.
* **Student Tracking:** Atanan öğrencilerin (Assigned Students) gelişimlerini izleme ve oturum taleplerini değerlendirme.
* **File Management:** Güvenli dosya yükleme servisi (FileStorageService) aracılığıyla sisteme sunum ve döküman yükleyip belirli öğrencilerle paylaşma.

### 🛡️ Yönetici Paneli (Admin)
* **User Management:** Sistemdeki tüm kullanıcıların (Öğrenci/Eğitmen) rol ve hesap yönetimi.
* **System Monitoring:** Ödeme akışlarının (Payments), sistem ayarlarının ve yoklama uyarılarının (Attendance Alerts) merkezi olarak denetlenmesi.

---

## 🛠️ Teknoloji Yığını (Tech Stack)

**Backend:**
* Java 17+, Spring Boot
* Spring Security & JWT (Kimlik Doğrulama)
* Spring Data JPA & Hibernate
* Maven

**Frontend:**
* React.js (Hooks & JSX)
* React Router DOM (Role-Based Routing)
* Context API (Global State Management)
* Axios (HTTP Client)

