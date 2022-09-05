import numpy as np
import scipy.io.wavfile as wav
from matplotlib import pyplot as plt
import warnings
warnings.filterwarnings("ignore")

# Assignment: https://cw.fel.cvut.cz/wiki/courses/b0b33opt/cviceni/hw/lsq2/start (in czech)

def get_subseq(y: np.array, index: int, order: int):
    seq = []
    length = len(y)
    for i in range(p - index, length - index):
        seq.append(y[i])
    return seq


def ar_fit_model(y: np.ndarray, p: int) -> np.ndarray:
    """Computes the parameters of the autogression model

    Args:
        y: np.ndarray: sound signal
        p: int: required order of AR model

    Return:
        np.ndarray: estimated parameters of AR model 



    Shape:
       - Input: (N,)
       - Output: (p+1,)
    """
    A = np.ndarray((p + 1, len(y) - p))
    for i in range(p + 1):
        for j in range(len(y) - p):
            if i == 0:
                A[i][j] = 1
            else:
                A[i][j] = y[p - i + j]

    b = y[p: len(y) + 1]
    m = np.linalg.lstsq(A.T, b.T)
    return m[0]


def ar_predict(a: np.ndarray, y0: np.ndarray, N: int) -> np.ndarray:
    """ computes the rest of elements of y, starting from (p+1)-th 
        one up to N-th one. 

    Args:
        a: np.ndarray: estimated parameters of AR model
        y0: np.ndarray: beginning of sequence to be predicted
        N: int:  required length of predicted sequence, including the 
                 beginning represented by y0. 
    Return:
        np.ndarray: the predicted sequence 


    Shape:
       - Input: (p+1,), (p,)
       - Output: (N,)
    """
    p = len(y0)
    y_pred = np.zeros(N)
    y_pred[:p] = y0
    for i in range(p, N):
        y_pred[i] = a[0] + np.dot(a[1:i+1], list(reversed(y_pred[i-p:i])))
    return y_pred


if(__name__ == '__main__'):

    fs, y = wav.read('gong.wav')
    y = y.copy()/32767
    p = 300      # size of history considered for prediction
    N = len(y)   # length of the sequence
    K = 10000    # visualize just K first elements

    a = ar_fit_model(y, p)
    print(a)
    exit()
    y0 = y[:p]
    y_pred = ar_predict(a, y0, N)

    wav.write('gong_predicted.wav', fs, y_pred)
    plt.plot(y[:K], 'b', label='original')
    plt.plot(y_pred[:K], 'r', label='AR model')
    plt.legend()
    plt.show()
    plt.savefig("image.png")
