import api from "../utils/axiosConfig";


// === CRUD İŞLEMLERİ ===

/** Yeni kaynak oluşturur (ödev veya materyal), dosya ile birlikte */
export const createResourceWithFile = async (dto, file) => {
  const formData = new FormData();
  formData.append("dto", new Blob([JSON.stringify(dto)], { type: "application/json" }));
  formData.append("file", file);

  const response = await api.post("/resources", formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });

  return response.data;
};

/** Mevcut bir kaynağı günceller */
export const updateResource = async (id, dto) => {
  const response = await api.put(`/resources/${id}`, dto);
  return response.data;
};

/** Belirli bir kaynağı siler */
export const deleteResource = async (id) => {
  await api.delete(`/resources/${id}`);
};




// === STUDENT İŞLEMLERİ ===

/** Öğrenciye atanmış kaynakları getirir */
export const getResourcesVisibleToStudent = async (studentId) => {
  const response = await api.get(`/resources/student/${studentId}`);
  return response.data;
};

/** Öğrencinin ödev çözümünü dosya olarak yüklemesini sağlar */
// export const uploadHomeworkSolution = async (resourceId, file) => {
//   const formData = new FormData();
//   formData.append("file", file);

//   const response = await fetch(`http://localhost:8080/api/resources/${resourceId}/solution`, {
//     method: "POST",
//     body: formData,
//   });

//   if (!response.ok) throw new Error("Ödev yüklenemedi");
// };

export const uploadHomeworkSolution = async (resourceId, formData) => {
  const response = await api.post(
    `/resources/${resourceId}/solution`,
    formData,
    {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    }
  );
  return response.data;
};


/** Öğrencinin dosya indirmesini sağlar */

export const downloadResourceFile = (fileName) => {
  const url = `http://localhost:8080/api/resources/download?fileName=${encodeURIComponent(fileName)}`;
  const link = document.createElement("a");
  link.href = url;
  link.setAttribute("download", fileName);
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
};




// === TUTOR İŞLEMLERİ ===

/** Öğretmenin kaynaklarını öğrencilere göre gruplanmış şekilde getirir */
export const getGroupedResourcesByTutor = async (tutorId) => {
  const response = await api.get(`/resources/tutor/${tutorId}/grouped-by-student`);
  return response.data;
};

/** Öğretmen tarafından belirli bir kaynağa not verilmesini sağlar */
export const gradeAssignment = async (resourceId, grade) => {
  const payload = { homeWorkScore: grade };
  await api.put(`/resources/${resourceId}/grade`, payload);
};