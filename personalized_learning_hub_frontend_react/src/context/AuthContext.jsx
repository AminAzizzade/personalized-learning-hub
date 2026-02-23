import { createContext, useContext, useState } from "react";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  // Sayfa ilk açıldığında localStorage'dan kullanıcıyı oku
  const [user, setUser] = useState(() => {
    const storedUser = localStorage.getItem("user");
    return storedUser ? JSON.parse(storedUser) : null;
  });

  // Giriş fonksiyonu
  const login = (userData) => {
  const userInfo = {
    id: userData.id,
    email: userData.email,
    fullName: userData.fullName,  // 👈 bu alan backend'den geliyor
    role: userData.role,
    tutorId: userData.tutorId,
    studentId: userData.studentId,
    isAuthenticated: true,
  };

  setUser(userInfo);

  // 🔥 EN ÖNEMLİ SATIR: localStorage'a 'user' anahtarıyla yazıyoruz
  localStorage.setItem("user", JSON.stringify(userInfo));
};


  // Çıkış fonksiyonu
  const logout = () => {
    setUser(null);
    localStorage.removeItem("user");
    console.log("Kullanıcı çıkış yaptı, localStorage temizlendi.");
  };

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
