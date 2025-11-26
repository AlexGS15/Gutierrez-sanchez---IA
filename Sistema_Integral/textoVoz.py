import easyocr
import torch
import scipy.io.wavfile
from transformers import VitsModel, AutoTokenizer
import os
import cv2

# --- CONFIGURACIÓN ---
# Usamos EasyOCR para la visión (Soporta español nativo 'es')
# Usamos Facebook MMS para la voz (Soporta español 'spa')
ARCHIVO_IMAGEN = "luiscodigos2.jpg"
ARCHIVO_AUDIO = "lectura_final.wav"

class AsistenteInteligente:
    def __init__(self):
        print("⚙️ Inicializando sistema...")
        self.device = "cuda" if torch.cuda.is_available() else "cpu"
        
        # 1. Cargar Motor de Visión (EasyOCR)
        # gpu=True acelerará mucho el proceso si tienes tarjeta NVIDIA
        print(f"👁️ Cargando EasyOCR (Español) en {self.device}...")
        self.reader = easyocr.Reader(['es'], gpu=(self.device == 'cuda'))
        
        # 2. Cargar Motor de Voz (MMS-TTS)
        print(f"🔊 Cargando Modelo de Voz...")
        self.tts_model_name = 'facebook/mms-tts-spa'
        self.tokenizer = AutoTokenizer.from_pretrained(self.tts_model_name)
        self.model = VitsModel.from_pretrained(self.tts_model_name).to(self.device)
        
        print("✅ Sistema listo para usar.")

    def leer_imagen(self, ruta_imagen):
        if not os.path.exists(ruta_imagen):
            print(f"❌ Error: No encuentro la imagen '{ruta_imagen}'")
            return ""

        print(f"📷 Escaneando: {ruta_imagen} ...")
        
        # EasyOCR hace todo el trabajo: detección, segmentación y lectura.
        # detail=0 hace que solo nos devuelva el texto puro, sin coordenadas.
        # paragraph=True intenta unir las líneas en frases coherentes.
        resultados = self.reader.readtext(ruta_imagen, detail=0, paragraph=True)
        
        if not resultados:
            print("⚠️ No detecté texto legible.")
            return ""
        
        # Unir todo el texto encontrado
        texto_completo = " ".join(resultados)
        print(f"\n📖 TEXTO DETECTADO:\n'{texto_completo}'\n")
        return texto_completo

    def hablar(self, texto, archivo_salida):
        if not texto: return

        print(f"🗣️ Generando audio...")
        inputs = self.tokenizer(text=texto, return_tensors="pt").to(self.device)
        
        with torch.no_grad():
            output = self.model(**inputs).waveform
        
        # Guardar archivo WAV
        output_np = output.cpu().float().numpy().T
        scipy.io.wavfile.write(archivo_salida, rate=self.model.config.sampling_rate, data=output_np)
        print(f"💾 Audio guardado en: {archivo_salida}")

def main():
    # Instanciar el asistente
    ia = AsistenteInteligente()
    
    # 1. Leer
    texto = ia.leer_imagen(ARCHIVO_IMAGEN)
    
    # 2. Hablar (solo si encontró texto)
    if texto:
        ia.hablar(texto, ARCHIVO_AUDIO)
        
        # Reproducir automáticamente en Windows (Opcional)
        try:
            import winsound
            winsound.PlaySound(ARCHIVO_AUDIO, winsound.SND_FILENAME)
        except:
            pass
    else:
        print("❌ Fin del proceso sin resultados.")

if __name__ == "__main__":
    main()

    