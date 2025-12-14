from sklearn.base import BaseEstimator, TransformerMixin
from sklearn.model_selection import KFold
from category_encoders import TargetEncoder
import pandas as pd


class KFoldTargetEncoder(BaseEstimator, TransformerMixin) :
    
    def __init__(self, cols, n_splits = 5, random_state = 42, shuffle = True) :
        self.n_splits = n_splits
        self.random_state = random_state
        self.shuffle = shuffle
        self.cols = cols
        self.global_encoder = None
    
    def fit(self, X, y) :
        X = X.copy()
        kf = KFold(n_splits = self.n_splits, random_state = self.random_state, shuffle = self.shuffle)
        te = TargetEncoder()
        oof_encoded = pd.DataFrame(index = X.index)

        for col in self.cols :
            oof_col = pd.Series(index = X.index, dtype = float)
            for train_idx, val_idx in kf.split(X) :
                X_train, X_val = X.iloc[train_idx], X.iloc[val_idx]
                y_train = y.iloc[train_idx]
                te.fit(X_train[col], y_train)
                oof_col.iloc[val_idx] = te.transform(X_val[col])[col]
            oof_encoded[col] = oof_col

        self.global_encoder = {
            col: TargetEncoder().fit(X[col], y)
            for col in self.cols
        }

        self.oof_encoded_ = oof_encoded
        return self

    def transform(self, X):
        X = X.copy()
        # Always use global encoder for any new data
        for col in self.cols:
            X[col] = self.global_encoder[col].transform(X[col])
        return X