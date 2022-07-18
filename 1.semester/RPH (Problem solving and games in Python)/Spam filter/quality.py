import os
from utils import read_classification_from_file
from confmat import BinaryConfusionMatrix
pos_tag = "SPAM"
neg_tag = "OK"


def quality_score(tp, tn, fp, fn):
    return (tp + tn)/(tp + tn + 10 * fp + fn)


def compute_quality_for_corpus(corpus_dir):
    truth_dict = read_classification_from_file(corpus_dir + '/' + '!truth.txt')
    pred_dict = read_classification_from_file(corpus_dir + '/' + '!prediction.txt')
    cm = BinaryConfusionMatrix(pos_tag, neg_tag)
    cm.compute_from_dicts(truth_dict, pred_dict)
    quality_corpus = quality_score(**cm.as_dict())
    return quality_corpus


if __name__ == '__main__':
    cwd = os.getcwd()
    print(compute_quality_for_corpus(cwd + '/2'))
