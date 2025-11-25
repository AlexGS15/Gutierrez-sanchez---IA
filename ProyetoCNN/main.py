import cv2
import numpy as np
import os
from tensorflow.keras.models import load_model
from tensorflow.keras.preprocessing.image import img_to_array
from tensorflow.keras.applications.mobilenet_v2 import preprocess_input

# --- CONFIGURACIÓN ---
# Asegúrate de que el nombre coincida con el archivo que descargaste de Colab
MODELO_PATH = "modelo_rostros_v2.h5"
ETIQUETAS_PATH = "etiquetas.txt"
UMBRAL_CONFIANZA = 0.80  # 80% de seguridad mínima para mostrar el nombre
# ---------------------

print("[INFO] Iniciando sistema de reconocimiento...")

# 1. Cargar las etiquetas
if not os.path.exists(ETIQUETAS_PATH):
    print(f"[ERROR] No encuentro el archivo {ETIQUETAS_PATH}")
    exit()

with open(ETIQUETAS_PATH, "r") as f:
    etiquetas = [line.strip() for line in f.readlines()]
print(f"[INFO] {len(etiquetas)} Clases cargadas: {etiquetas}")

# 2. Cargar el modelo entrenado
print(f"[INFO] Cargando modelo {MODELO_PATH}...")
try:
    model = load_model(MODELO_PATH)
    print("[INFO] ¡Modelo cargado exitosamente!")
except Exception as e:
    print(f"[ERROR] No se pudo cargar el modelo. Verifica el nombre y la ruta.\nError: {e}")
    exit()

# 3. Iniciar Webcam
# Si tienes una cámara USB externa, cambia el 0 por 1
cap = cv2.VideoCapture(0) 

# Cargar detector de rostros (Haar Cascade)
face_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')

print("------------------------------------------------")
print("[SISTEMA ACTIVO] Presiona 'Q' para salir.")
print("------------------------------------------------")

while True:
    ret, frame = cap.read()
    if not ret: break


    output = frame.copy() # Copia para dibujar encima
    
    # Detectar rostros (en escala de grises)
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    faces = face_cascade.detectMultiScale(gray, scaleFactor=1.1, minNeighbors=5, minSize=(60,60))

    for (x, y, w, h) in faces:
        # Recortar la región del rostro
        rostro = frame[y:y+h, x:x+w]
        
        try:
            # --- CORRECCIÓN DE COLOR CRÍTICA ---
            # OpenCV captura en BGR, pero MobileNetV2 se entrenó en RGB.
            # Convertimos SOLO el recorte que va a la IA.
            rostro_rgb = cv2.cvtColor(rostro, cv2.COLOR_BGR2RGB)

            # --- PREPROCESAMIENTO ---
            # 1. Redimensionar a 224x224
            rostro_rgb = cv2.resize(rostro_rgb, (224, 224))
            # 2. Convertir a array
            rostro_blob = img_to_array(rostro_rgb)
            # 3. Preprocesar (Escalar valores para MobileNet)
            rostro_blob = preprocess_input(rostro_blob)
            # 4. Expandir dimensiones (Batch de 1)
            rostro_blob = np.expand_dims(rostro_blob, axis=0)

            # --- PREDICCIÓN ---
            preds = model.predict(rostro_blob, verbose=0)[0]
            idx = np.argmax(preds)
            label = etiquetas[idx]
            score = preds[idx]

            # --- VISUALIZACIÓN ---
            text = f"{label}: {score*100:.1f}%"
            
            # Verde si es seguro, Rojo si duda
            if score >= UMBRAL_CONFIANZA:
                color = (0, 255, 0) # Verde
                thickness = 2
            else:
                color = (0, 0, 255) # Rojo
                thickness = 1
                text = f"Dudoso ({score*100:.1f}%)"
                # Opcional: Si quieres que diga "Desconocido" en lugar del nombre incorrecto:
                # label = "Desconocido"

            # Dibujamos en 'output' (que sigue siendo BGR para que los colores se vean bien en pantalla)
            cv2.rectangle(output, (x, y), (x+w, y+h), color, thickness)
            cv2.putText(output, text, (x, y - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.6, color, 2)

        except Exception as e:
            print(f"Error en un cuadro: {e}")

    cv2.imshow("Reconocimiento Facial Final", output)

    # Salir con Q
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()