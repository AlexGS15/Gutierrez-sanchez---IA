import matplotlib.pyplot as plt
from sklearn import svm
from sklearn.datasets import make_blobs

# 1. Generar datos simples (2 grupos separados)
X, y = make_blobs(n_samples=100, centers=2, random_state=6)

# 2. Crear el modelo SVM con Kernel Lineal
modelo_lineal = svm.SVC(kernel='linear')
modelo_lineal.fit(X, y)

# (Aquí iría el código de visualización, pero conceptualmente traza una línea recta)
print("Modelo Lineal entrenado con éxito.")