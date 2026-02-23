# 🎓 Personalized Learning Hub (Kişiselleştirilmiş Öğrenme Platformu)

Bu proje, eğitim merkezlerinin öğrenci, eğitmen ve yönetim süreçlerini dijitalleştirmek ve optimize etmek amacıyla geliştirilmiş tam kapsamlı (full-stack) bir web uygulamasıdır. 

## 🚀 Proje Hakkında

Uygulama; öğrencilerin kendilerine uygun eğitmenlerle eşleşmesini, ders programlarını yönetmesini ve gelişimlerini takip etmesini sağlarken; eğitmenlere kaynak yönetimi, yöneticilere ise tüm sistemin denetimi için güçlü araçlar sunar.

### Öne Çıkan Teknik Özellikler:
- **Backend:** Spring Boot kullanılarak **Katmanlı Mimari (Layered Architecture)** prensiplerine uygun geliştirilmiştir (Controller, Service, Repository, DTO, Mapper).
- **Frontend:** Modüler bileşen yapısı ve merkezi state yönetimi ile **React.js** kullanılarak inşa edilmiştir.
- **Veritabanı:** İlişkisel veri modellemesi için **PostgreSQL** tercih edilmiştir.
- **Güvenlik:** Rol tabanlı erişim kontrolü (Student, Tutor, Admin) için özelleştirilmiş **Spring Security** yapılandırması mevcuttur.
- **Kalite Güvencesi:** İş mantığının (business logic) doğruluğunu sağlamak adına tüm ana servisler için **JUnit 5 ve Mockito** kullanılarak **Unit Testler** yazılmıştır.

## 🛠️ Kullanılan Teknolojiler

- **Backend:** Java 17, Spring Boot, Spring Data JPA, Spring Security, MapStruct, Maven.
- **Frontend:** React, React Router, Axios, CSS Modules.
- **Veritabanı:** PostgreSQL.
- **Test:** JUnit 5, Mockito.

## ✨ Temel Özellikler

- **Kişiselleştirilmiş Paneller:** Öğrenci, Eğitmen ve Admin rolleri için özelleşmiş kullanıcı arayüzleri.
- **Ders Yönetimi:** Akıllı ders rezervasyonu, müsaitlik takibi ve otomatik eğitmen atama sistemi.
- **Gelişim Takibi:** Görsel ilerleme raporları ve yetkinlik değerlendirmeleri (Skill Assessment).
- **Kaynak Kütüphanesi:** Eğitim materyallerinin paylaşımı ve ödev yönetim merkezi.
- **Akıllı Bildirimler:** Devamsızlık uyarıları ve sistem bildirimleri ile idari verimlilik.

## 📂 Proje Yapısı

```text
├── personalized-learning-hub-backend           # Spring Boot REST API
└── personalized_learning_hub_frontend_react    # React Single Page Application (SPA)
⚙️ Kurulum ve Çalıştırma
Backend
src/main/resources/application.properties dosyasındaki veritabanı ayarlarını yapın.

./mvnw spring-boot:run komutu ile uygulamayı başlatın.

Frontend
cd personalized_learning_hub_frontend_react

npm install

npm start