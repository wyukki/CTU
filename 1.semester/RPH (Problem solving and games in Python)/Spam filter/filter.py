from corpus import Corpus
from trainingcorpus import TrainingCorpus
import os


class MyFilter:
    def __init__(self):
        self.trigger_words = {}
        self.pred_dict = {}

    def train(self, train_dir):
        training = TrainingCorpus(train_dir)
        self.trigger_words = TrainingCorpus.get_trigger_words_dict(training)
        return self.trigger_words

    def test(self, test_corpus_dir):
        self.pred_dict = Corpus.get_pred_dict(self.trigger_words, test_corpus_dir)
        with open(test_corpus_dir + '/!prediction.txt', 'wt', encoding='utf-8')as file:
            for key, value in self.pred_dict.items():
                file.write(f"{key} {value} \n")
        return self.pred_dict


if __name__ == '__main__':
    cwd = os.getcwd()
    filter = MyFilter()
    filter.train(cwd + '/1')
    filter.test(cwd + '/2')
