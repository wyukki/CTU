import email
import os
from utils import read_classification_from_file
import re

ignore_list = ['a', 'about', 'after', 'again', 'all', 'already', 'also', 'although',
               'always', 'am', 'an', 'and', 'another', 'any', 'anybody', 'anything',
               'are', 'as', 'at', 'b', 'be', 'because', 'been', 'being', 'but', 'by',
               'c', 'can', 'cannot', 'come', 'could', 'd', 'do', 'done', 'e', 'else',
               'etc', 'even', 'ever', 'f', 'feel', 'find', 'for', 'from', 'g', 'get',
               'give', 'go', 'h', 'had', 'has', 'have', 'he', 'her', 'here', 'him',
               'his', 'how', 'however', 'i', 'ie', 'if', 'in', 'is', 'it', 'its', 'j',
               'k', 'keep', 'l', 'leave', 'look', 'ltd', 'm', 'made', 'make', 'many',
               'may', 'me', 'more', 'my', 'myself', 'n', 'name', 'nbsp', 'no', 'none',
               'nor', 'not', 'nothing', 'now', 'o', 'of', 'often', 'on', 'one', 'only',
               'or', 'other', 'others', 'our', 'out', 'over', 'p', 'please', 'probably',
               'q', 'r', 's', 'same', 'say', 'see', 'seem', 'she', 'should', 'so', 'some',
               'someone', 'something', 'sometimes', 'such', 't', 'take', 'than', 'that',
               'the', 'their', 'them', 'then', 'there', 'these', 'they', 'think', 'this',
               'those', 'through', 'to', 'together', 'too', 'towards', 'try', 'u', 'up',
               'us', 'use', 'v', 'very', 'w', 'want', 'was', 'we', 'well', 'were', 'what',
               'whatever', 'when', 'which', 'while', 'who', 'whole', 'why', 'will', 'with',
               'without', 'work', 'would', 'x', 'y', 'yet', 'you', 'your', 'yours', 'z']


class TrainingCorpus:
    def __init__(self, directory):
        self.trigger_words = {}
        self.directory = directory
        self.spam_dict = {}
        self.ham_dict = {}
        self.truth_dict = read_classification_from_file(directory + '/!truth.txt')

    def get_trigger_words_dict(self):
        """Returns a dictionary of trigger words"""
        self.read_and_work_with_email()
        return self.trigger_words

    def read_and_work_with_email(self):
        """"Opens all emails in directory,
        divides email into parts,
        if email is spam, calls function get_spam_dict,
        else calls function get_ham_dict"""
        file_list = os.listdir(self.directory)
        for file in file_list:
            if file.startswith("!"):
                continue
            tag = self.truth_dict.get(file)
            with open(self.directory + f'/{file}', 'rt', encoding='utf-8')as f:
                message = email.message_from_file(f)
                if message.is_multipart():
                    for part in message.walk():
                        if part.get_content_maintype() == "multipart":
                            continue
                        if tag == "SPAM":
                            self.get_spam_dict(part.get_payload())
                        else:
                            self.get_ham_dict(part.get_payload())
                else:
                    if tag == "SPAM":
                        self.get_spam_dict(message.get_payload())
                    else:
                        self.get_ham_dict(message.get_payload())
        self.compare_dicts_and_ignore_list()
        self.compare_spam_and_ham_dicts()

    @staticmethod
    def strip_email_body(body):
        """Deletes all unwanted objects from email"""
        body = body.replace("\n", " ")
        body = body.replace("_", "")
        body = body.lower()
        first_change = ' '.join(re.sub('<[^<]+?>', " ", body).split())  # delete html-tags
        second_change = re.sub("\d+", '', first_change)  # delete all numbers
        third_change = re.sub("http[s]?://\S+", '', second_change)  # delete every http link
        fourth_change = re.sub("www.\S+", '', third_change)  # delete every www link
        fifth_change = re.sub("[.+?]", '', fourth_change)  # delete everything in [] brackets
        sixth_change = ' '.join(re.sub(r'[^\w]', " ", fifth_change).split())  # delete every symbol
        return sixth_change

    def get_spam_dict(self, body):
        """Creates a dictionary with all remaining words from spam messages"""
        new_body = self.strip_email_body(body)
        for word in new_body.split():
            if word not in self.spam_dict:
                self.spam_dict[word] = 1
            else:
                self.spam_dict[word] += 1

    def get_ham_dict(self, body):
        """Creates a dictionary with all remaining words from OK messages"""
        new_body = self.strip_email_body(body)
        for word in new_body.split():
            if word not in self.ham_dict:
                self.ham_dict[word] = 1
            else:
                self.ham_dict[word] += 1

    def compare_dicts_and_ignore_list(self):
        """Deletes words from dictionary
        if they're in ignore_list"""
        for word in ignore_list:
            if word in self.spam_dict.keys():
                del(self.spam_dict[word])
        for word in ignore_list:
            if word in self.ham_dict.keys():
                del(self.ham_dict[word])

    def compare_spam_and_ham_dicts(self):
        """Creates a dictionary with spam words only"""
        delete = [ham_word for ham_word in self.ham_dict if ham_word not in self.spam_dict]
        for ham_word in delete:
            del(self.ham_dict[ham_word])  # delete all words that occur only in ham_dict
        delete_spam = [spam_word for spam_word in self.spam_dict if spam_word in self.ham_dict]
        for spam_word in delete_spam:
            del (self.spam_dict[spam_word])  # delete all words that occur in ham_ and spam_ dictionaries
        self.trigger_words = self.spam_dict
