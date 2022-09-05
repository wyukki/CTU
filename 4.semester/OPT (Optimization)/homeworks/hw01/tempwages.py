import math
import numpy as np
import matplotlib.pyplot as plt

# Assignment: https://cw.fel.cvut.cz/wiki/courses/b0b33opt/cviceni/hw/lsq1/start (in czech)

with open("teplota.txt", 'r') as f:
    lines = f.readlines()
f.close()

data = []
for line in lines:
    year, salary = line.split(" ")
    l = [float(year), float(salary)]
    data.append(l)


def fit_temps(t, T, omega):
    A = np.vstack([np.ones(len(t)),
                   t,
                   np.ones(len(t)),
                   np.ones(len(t))]).T
    rows, cols = A.shape
    for row in range(rows):
        for col in range(cols):
            if col == 2:
                A[row][col] = math.sin(t[row] * omega)
            elif col == 3:
                A[row][col] = math.cos(t[row] * omega)
    x1, x2, x3, x4 = np.linalg.lstsq(A, T, rcond=None)[0]
    return (x1, x2, x3, x4)


def fit_wages(t, M):
    A = np.vstack([np.ones(len(t)), t]).T
    m, c = np.linalg.lstsq(A, M, rcond=None)[0]
    return m, c


def quarter2_2009(x):
    return x[0] + 2009.25 * x[1]


mat = np.array(data)
t = mat[:, 0]
M = mat[:, 1]
solution = fit_temps(t, M, (2*math.pi)/365)
print(solution)
