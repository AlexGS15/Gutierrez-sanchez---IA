from sklearn.datasets import make_circles

# 1. Generar datos en forma de círculos (la "dona")
X_circulo, y_circulo = make_circles(n_samples=100, factor=0.5, noise=0.1)

# 2. Crear el modelo SVM con Kernel Radial (RBF)
# gamma controla qué tan "curva" o ajustada es la frontera
modelo_rbf = svm.SVC(kernel='rbf', C=1, gamma='scale')
modelo_rbf.fit(X_circulo, y_circulo)

print("Modelo Radial entrenado con éxito.")