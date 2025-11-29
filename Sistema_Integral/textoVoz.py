import easyocr
import torch
import scipy.io.wavfile
from transformers import VitsModel, AutoTokenizer
import os
import cv2


ARCHIVO_IMAGEN = "luiscodigos2.jpg"
ARCHIVO_AUDIO = "lectura_final.wav"

class AsistenteInteligente:
    def __init__(self):

        self.device = "cuda" if torch.cuda.is_available() else "cpu"
        
        self.reader = easyocr.Reader(['es'], gpu=(self.device == 'cuda'))

        self.tts_model_name = 'facebook/mms-tts-spa'
        self.tokenizer = AutoTokenizer.from_pretrained(self.tts_model_name)
        self.model = VitsModel.from_pretrained(self.tts_model_name).to(self.device)
        


    def leer_imagen(self, ruta_imagen):
        if not os.path.exists(ruta_imagen):
            print(f"Error: No encuentro la imagen '{ruta_imagen}'")
            return ""

        print(f"Escaneando: {ruta_imagen} ...")
        

        resultados = self.reader.readtext(ruta_imagen, detail=0, paragraph=True)
        
        if not resultados:
            print("No detecté texto legible.")
            return ""
        

        texto_completo = " ".join(resultados)
        print(f"\nTEXTO DETECTADO:\n'{texto_completo}'\n")
        return texto_completo

    def hablar(self, texto, archivo_salida):
        if not texto: return

        print(f"Generando audio...")
        inputs = self.tokenizer(text=texto, return_tensors="pt").to(self.device)
        
        with torch.no_grad():
            output = self.model(**inputs).waveform
        
        # Guardar archivo WAV
        output_np = output.cpu().float().numpy().T
        scipy.io.wavfile.write(archivo_salida, rate=self.model.config.sampling_rate, data=output_np)
        print(f"Audio guardado en: {archivo_salida}")

def main():
    # Instanciar el asistente
    ia = AsistenteInteligente()
    
    # 1. Leer
    texto = ia.leer_imagen(ARCHIVO_IMAGEN)
    
    # 2. Hablar (solo si encontró texto)
    if texto:
        ia.hablar(texto, ARCHIVO_AUDIO)
        
        try:
            import winsound
            winsound.PlaySound(ARCHIVO_AUDIO, winsound.SND_FILENAME)
        except:
            pass
    else:
        print("❌ Fin del proceso sin resultados.")

if __name__ == "__main__":
    main()


    
