import numpy as np
import scipy.optimize as sp

# Assignment: https://cw.fel.cvut.cz/wiki/courses/b0b33opt/cviceni/hw/lp1/start (in czech)

def vyhra(c, k):
    x1 = -c[0]
    x10 = -c[1]
    x0 = -c[2]
    x02 = -c[3]
    x2 = -c[4]
    A = [[x1, x10, 0, 0, 0, 1],
         [0, x10, x0, x02, 0, 1],
         [0, 0, 0, x02, x2, 1]]
    z = [0, 0, 0, 0, 0, -1]
    b = [0, 0, 0]
    x1_bounds = (0, None)
    x10_bounds = (0, None)
    x0_bounds = (0, None)
    x02_bounds = (0, None)
    x2_bounds = (0, None)
    z_bounds = (0, None)
    return sp.linprog(z, A_ub=A, b_ub=b, A_eq=[[1, 1, 1, 1, 1, 0]], b_eq=k,
                      bounds=[x1_bounds, x10_bounds, x0_bounds, x02_bounds, x2_bounds, z_bounds]).get('x')[:5]


def vyhra2(c, k, m):
    x1 = -c[0]
    x0 = -c[1]
    x2 = -c[2]
    A = [[x1, 0, 0, 1],
         [0, x0, 0, 1],
         [0, 0, x2, 1]]
    c = [0, 0, 0, -1]
    b = [0, 0, 0]
    x1_bounds = (m, None)
    x0_bounds = (m, None)
    x2_bounds = (m, None)
    z_bounds = (k - 1000, None)
    return sp.linprog(c, A_ub=A, b_ub=b, A_eq=[[1, 1, 1, 0]], b_eq=k,
                      bounds=[x1_bounds, x0_bounds, x2_bounds, z_bounds]).get('x')[:3]


def minimaxfit(x, y):
    m, n = np.shape(x)
    z = []
    for i in range(m):
        z.append(0)  # x vars
    z.append(0)  # y
    z.append(1)  # z
    X = x.T
    A = np.ndarray((n * 2, m + 2))
    for a_col in range(2):
        for row in range(n):
            for col in range(m):
                if a_col == 0:
                    A[row][col] = X[row][col]
                else:
                    A[row + n][col] = -X[row][col]
            if a_col == 0:
                A[row][m] = 1
                A[row][m + 1] = -1
            else:
                A[row + n][m] = -1
                A[row + n][m + 1] = -1
    b = []
    for i in range(2):
        for j in range(n):
            if i == 0:
                b.append(y[0][j])
            else:
                b.append(-y[0][j])
    res = sp.linprog(z, A_ub=A, b_ub=b, bounds=(-np.inf, np.inf))
    res = res.get('x')
    a = res[: m]
    b = res[m]
    r = res[m + 1]
    return a, b, r


x = np.array([[1, 2, 3, 3, 2], [4, 1, 2, 5, 6], [7, 8, 9, -5, 7]])
y = np.array([[7, 4, 1, 2, 5]])
a, b, r = minimaxfit(x, y)
print(a, b, r)
