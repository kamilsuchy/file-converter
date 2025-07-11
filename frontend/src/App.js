import logo from './logo.svg';
import './App.css';
import React, { useState } from "react";

function App() {
  const [errorMessage, setErrorMessage] = useState("");
  const [selectedFile, setSelectedFile] = useState(null);
  const [selectedFormat, setSelectedFormat] = useState(null);
  const [isDragging, setIsDragging] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  const formats = ["WEBP", "PNG", "JPG", "PDF", "TXT"];

  const handleFile = (file) => {
    setSelectedFile(file);
  };

  const handleDrop = (e) => {
    e.preventDefault();
    setIsDragging(false);

    if (e.dataTransfer.files && e.dataTransfer.files[0]) {
      handleFile(e.dataTransfer.files[0]);
    }
  };

  const handleDragOver = (e) => {
    e.preventDefault();
  };

  const handleDragEnter = () => setIsDragging(true);
  const handleDragLeave = () => setIsDragging(false);

  const handleConvert = async () => {
  setErrorMessage("");
  setIsLoading(true);

  if (!selectedFile || !selectedFormat) {
    
    setIsLoading(false);
    return;
  } 
  
  const formData = new FormData();
  formData.append("file", selectedFile);
  formData.append("format", selectedFormat);

  try {
    const response = await fetch("/convert", {
      method: "POST",
      body: formData,
    });

    if (!response.ok) {
      throw new Error("Conversion failed");
    }

    const blob = await response.blob();

    // Pobieranie pliku - tworzymy URL i klikamy link
    const downloadUrl = window.URL.createObjectURL(blob);

    // Nazwa pliku zmienia się na oryginalną z nowym rozszerzeniem
    const fileNameParts = selectedFile.name.split(".");
    fileNameParts.pop(); // usuń stare rozszerzenie
    const newFileName = fileNameParts.join(".") + "." + selectedFormat.toLowerCase();

    const link = document.createElement("a");
    link.href = downloadUrl;
    link.download = newFileName;
    document.body.appendChild(link);
    link.click();
    link.remove();
    window.URL.revokeObjectURL(downloadUrl);
    setSelectedFile(null);
  } catch (error) {
    setErrorMessage("Error during conversion: " + error.message);
  } finally {
    setIsLoading(false);
  }
};

  const handleInputChange = (e) => {
    if (e.target.files && e.target.files[0]) {
      handleFile(e.target.files[0]);
    }
  };

  return (
    <div style={styles.container}>
      <h1 style={styles.heading}>FileTornado - Free Online File Converter</h1>

      <div style={styles.main}>
        {/* Left: Dropzone */}
        <div style={styles.left}>
          <div
            style={{
              ...styles.dropzone,
              backgroundColor: isDragging ? "#e0e0e0" : "#fff",
              borderColor: isDragging ? "#333" : "#ccc",
            }}
            onDrop={handleDrop}
            onDragOver={handleDragOver}
            onDragEnter={handleDragEnter}
            onDragLeave={handleDragLeave}
          >
            <p style={{ pointerEvents: "none" }}>
              {selectedFile
                ? selectedFile.name
                : "Drop file here or click to select"}
            </p>
            <input
              type="file"
              style={styles.fileInput}
              onChange={handleInputChange}
            />
          </div>
        </div>

        {/* Right: Format Tiles */}
        <div style={styles.right}>
          {formats.map((format) => (
            <div
              key={format}
              style={{
                ...styles.tile,
                backgroundColor:
                  selectedFormat === format ? "#333" : "#f0f0f0",
                color: selectedFormat === format ? "#fff" : "#000",
              }}
              onClick={() => setSelectedFormat(selectedFormat === format ? null : format)}
            >
              {format}
            </div>
          ))}
        </div>
      </div>

      {/* Footer: Convert Button */}
      <div style={styles.footer}>
        <div
          style={{
            color: "red",
            minHeight: "1.2rem",   // wysokość rezerwująca miejsce na komunikat
            marginBottom: "1rem",
            textAlign: "center",
            visibility: errorMessage ? "visible" : "hidden", // ukryj tekst, ale zostaw miejsce
          }}
        >
          {errorMessage}
        </div>
        <button
          style={{
            ...styles.button,
            opacity: selectedFile && selectedFormat ? 1 : 0.5,
            cursor: selectedFile && selectedFormat ? "pointer" : "not-allowed",
          }}
          disabled={!selectedFile || !selectedFormat}
          onClick={handleConvert}
        >
          Convert
        </button>
      </div>
      {isLoading && (
        <div style={styles.loaderOverlay}>
          <div style={styles.loader}></div>
          <p style={{ marginTop: "1rem" }}>Converting...</p>
        </div>
      )}
    </div>
  );
}

const styles = {
  container: {
    fontFamily: "Inter, sans-serif",
    padding: "2rem",
    paddingTop: "5rem",
    minHeight: "100vh",
    maxWidth: "100vw",
    boxSizing: "border-box",
    backgroundColor: "#fafafa",
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    overflowX: "hidden",
  },
  heading: {
    fontSize: "2rem",
    fontWeight: 600,
    marginBottom: "2rem",
  },
  main: {
    display: "flex",
    gap: "2rem",
    width: "100%",
    maxWidth: "900px",
    justifyContent: "center",
  },
  left: {
    flex: 1,
    display: "flex",
    justifyContent: "center",
  },
  dropzone: {
    border: "2px dashed #ccc",
    borderRadius: "12px",
    padding: "2rem",
    width: "100%",
    maxWidth: "300px",
    textAlign: "center",
    cursor: "pointer",
    position: "relative",
    transition: "0.2s ease",
    wordWrap: "break-word",
    overflowWrap: "break-word",
    whiteSpace: "normal",
  },
  fileInput: {
    opacity: 0,
    position: "absolute",
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    cursor: "pointer",
  },
  right: {
    flex: 1,
    display: "grid",
    gridTemplateColumns: "repeat(auto-fill, minmax(80px, 1fr))",
    gap: "1rem",
    alignContent: "start",
  },
  tile: {
    padding: "1rem",
    borderRadius: "8px",
    textAlign: "center",
    fontWeight: 500,
    cursor: "pointer",
    transition: "0.2s ease",
    boxShadow: "0 2px 5px rgba(0,0,0,0.05)",
  },
  footer: {
    marginTop: "2rem",
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  },
  button: {
    padding: "0.75rem 2rem",
    borderRadius: "8px",
    fontSize: "1rem",
    fontWeight: 500,
    backgroundColor: "#111",
    color: "#fff",
    border: "none",
    cursor: "pointer",
    transition: "0.2s",
  },
  loaderOverlay: {
    position: "fixed",
    top: 0,
    left: 0,
    width: "100vw",
    height: "100vh",
    backgroundColor: "rgba(255, 255, 255, 0.85)",
    display: "flex",
    flexDirection: "column",
    justifyContent: "center",
    alignItems: "center",
    zIndex: 9999,
  },

  loader: {
    border: "8px solid #f3f3f3",
    borderTop: "8px solid #333",
    borderRadius: "50%",
    width: "60px",
    height: "60px",
    animation: "spin 1s linear infinite",
  },

};

export default App;