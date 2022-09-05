import scipy.io as sio
import numpy as np
import random
from math import pi
from math import sqrt
from matplotlib import pyplot as plt

# Assignment: https://cw.fel.cvut.cz/wiki/courses/b0b33opt/cviceni/hw/kruznice_lin/start (in czech)

def quad_to_center(d, e, f):
    x0 = -d / 2
    y0 = -e / 2
    r = sqrt((pow(d, 2) + pow(e, 2)) / 4 - f)
    return x0, y0, r


def fit_circle_nhom(X):
    A = np.ndarray((X.shape[0], 3))
    m = X.shape[0]
    b = np.ones(m)
    n = 3
    for row in range(m):
        for col in range(n):
            val = None
            if col == 0:
                val = X[row][0]
            elif col == 1:
                val = X[row][1]
            else:
                val = 1
            A[row][col] = val
        b[row] = (-1 * pow(X[row][0], 2)) + (-1 * pow(X[row][1], 2))
    # print(b)
    sol = np.linalg.lstsq(A, b, rcond=None)[0]
    d = sol[0]
    e = sol[1]
    f = sol[2]
    # print(d)
    # print(e)
    # print(f)
    return d, e, f


def fit_circle_hom(X):
    n = 4
    m = X.shape[0]
    X = np.array(X)
    A = np.ndarray((m, n))
    for row in range(m):
        for col in range(n):
            val = None
            if col == 0:
                val = pow(X[row][0], 2) + pow(X[row][1], 2)
            elif col == 1:
                val = X[row][0]
            elif col == 2:
                val = X[row][1]
            else:
                val = 1
            A[row][col] = val
    U, D = np.linalg.eig(A.T @ A)
    index = 0
    min_val = None
    for i in range(U.shape[0]):
        if min_val is None:
            min_val = U[i]
        elif U[i] < min_val:
            min_val = U[i]
            index = i
    sol_vector = D.T[index]
    a = sol_vector[0]
    d = sol_vector[1] / a
    e = sol_vector[2] / a
    f = sol_vector[3] / a
    print(d, "\n", e, "\n", f)
    return d, e, f


def dist(X, x0, y0, r):
    N = X.shape[0]
    i = N
    distances = np.zeros(N)
    for row in X:
        distances[N - i] = np.linalg.norm((x0 - row[0], y0 - row[1])) - r
        i -= 1
    return distances


def get_inliers(X, x0, y0, r, threshold):
    inliers_num = 0
    i = X.shape[0]
    i = 0
    distances = []
    for row in X:
        distances.append(np.linalg.norm((x0 - row[0], y0 - row[1])) - r)
        if abs(distances[i]) < threshold:
            inliers_num += 1
        i += 1
    return inliers_num


def fit_circle_ransac(X, num_iter, threshold):
    best_circle = {}
    m = X.shape[0]
    for i in range(num_iter):
        first = random.randint(0, m - 1)
        second = random.randint(0, m - 1)
        third = random.randint(0, m - 1)
        first_p = X[first]
        second_p = X[second]
        third_p = X[third]
        X_try = np.vstack((first_p, second_p, third_p))
        d, e, f = fit_circle_hom(X_try)
        x0, y0, r = quad_to_center(d, e, f)
        inliers_num = get_inliers(X, x0, y0, r, threshold)
        best_circle[inliers_num] = (x0, y0, r)
    max = 0
    params = []
    for key in best_circle:
        if key > max:
            params = [best_circle[key]]
            max = key
    x0 = params[0][0]
    y0 = params[0][1]
    r = params[0][2]
    return x0, y0, r


def plot_circle(x0, y0, r, color, label):
    t = np.arange(0, 2 * pi, 0.01)
    X = x0 + r * np.cos(t)
    Y = y0 + r * np.sin(t)
    plt.plot(X, Y, color=color, label=label)


if (__name__ == '__main__'):
    data = sio.loadmat('data.mat')
    X = data['X']  # only inliers
    A = data['A']  # X + outliers

    def_h = fit_circle_hom(X)
    def_nh = fit_circle_nhom(X)
    x0y0r_nh = quad_to_center(*def_nh)
    dnh = dist(X, *x0y0r_nh)

    x0y0r_h = quad_to_center(*def_h)
    dh = dist(X, *x0y0r_h)

    results = {'def_nh': def_nh, 'def_h': def_h,
               'x0y0r_nh': x0y0r_nh, 'x0y0r_h': x0y0r_nh,
               'dnh': dnh, 'dh': dh}

    GT = sio.loadmat('GT.mat')
    for key in results:
        print('max difference', np.amax(np.abs(results[key] - GT[key])), 'in', key)

    # x = fit_circle_ransac(A, 2000, 0.1)
    #
    plt.figure(1)
    plt.subplot(121)
    plt.scatter(X[:, 0], X[:, 1], marker='.', s=3)
    plot_circle(*x0y0r_h, 'r', 'hom')
    plot_circle(*x0y0r_nh, 'b', 'nhom')
    plt.legend()
    plt.axis('equal')
    plt.subplot(122)
    plt.scatter(A[:, 0], A[:, 1], marker='.', s=2)
    # plot_circle(*x, 'y', 'ransac')
    plt.legend()
    plt.axis('equal')
    plt.show()
