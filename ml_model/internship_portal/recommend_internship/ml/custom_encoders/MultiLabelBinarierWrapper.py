from sklearn.base import BaseEstimator, TransformerMixin
from sklearn.preprocessing import MultiLabelBinarizer
import numpy as np


class MultiLabelBinarizerWrapper(BaseEstimator, TransformerMixin) :
    def __init__(self, cols):
        self.cols = cols
        self.mlbs = {}

    def fit(self, X, y) :
        for col in self.cols : 
            mlb = MultiLabelBinarizer()
            mlb.fit(X[col])
            self.mlbs[col] = mlb

        return self
    
    def transform(self, X) :
        encoded_cols = []
        for col in self.cols :
            encoded_col = self.mlbs[col].transform(X[col])
            encoded_cols.append(encoded_col)
        return np.hstack(encoded_cols)