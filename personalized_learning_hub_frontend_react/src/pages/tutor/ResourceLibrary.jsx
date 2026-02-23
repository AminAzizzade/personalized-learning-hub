import React, { useEffect, useState } from 'react';
import MyResources from '../../components/tutor/resource/MyResources';
import ResourceForm from '../../components/tutor/resource/ResourceForm';

const ResourceLibrary = () => {
  const [tutorId, setTutorId] = useState(null);
  const [refresh, setRefresh] = useState(false);
  const [selected, setSelected] = useState(null);

  useEffect(() => {
    const id = localStorage.getItem('tutorId');
    if (id) setTutorId(id);
    else alert("Giriş yapılmamış.");
  }, []);

  const triggerRefresh = () => setRefresh(!refresh);

  
  return (
    <div className="container mt-4">
      <h3>📚 Kaynak Yönetimi</h3>
      {tutorId && (
        <>
          <ResourceForm tutorId={tutorId} selected={selected} onSaved={triggerRefresh} />
          <hr />
          <MyResources tutorId={tutorId} refresh={refresh} onEdit={setSelected} />
          <hr />
        </>
      )}
    </div>
  );
};

export default ResourceLibrary;
