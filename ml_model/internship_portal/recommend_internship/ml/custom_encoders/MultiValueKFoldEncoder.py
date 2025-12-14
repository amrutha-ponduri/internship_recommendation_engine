from category_encoders import TargetEncoder
import pandas as pd
from sklearn.base import BaseEstimator, TransformerMixin
from sklearn.model_selection import KFold
import numpy as np


class MultiValueKFoldEncoder(BaseEstimator, TransformerMixin) :
    def __init__(self, n_splits = 5, shuffle = True, random_state = 42, cols = [], smoothing = 0.3, agg = 'mean'):
        self.n_splits = n_splits
        self.shuffle = shuffle
        self.random_state = random_state
        self.cols = cols
        self.smoothing = smoothing
        self.agg = agg


    def fit(self, X, y) :
        kf = KFold(n_splits = self.n_splits, shuffle = self.shuffle, random_state = self.random_state)
        self.global_mean = y.mean()
        self.mappings = {}

        for col in self.cols :
            df = X[[col]].copy()
            df['is_selected'] = y.values
            df['row_id'] = np.arange(len(df))

            exploded = df.explode(col).reset_index(drop = True)
            exploded['enc'] = np.nan

            for train_idx, val_idx in kf.split(exploded) :
                train = exploded.iloc[train_idx]
                val = exploded.iloc[val_idx]

                stats = train.groupby(col)['is_selected'].agg(['mean','count'])
    
                smoothed = (stats['count'] * stats['mean'] + self.smoothing * self.global_mean) / (stats['count'] + self.smoothing)
                exploded.loc[val_idx, 'enc'] = val[col].map(smoothed)

            exploded['enc'] = exploded['enc'].fillna(self.global_mean)
            encoded = exploded.groupby('row_id')['enc'].agg(self.agg)
            self.mappings[col] = encoded
        return self

    def transform(self, X):
        transformed_cols = []

        for col in self.cols:
            df = X[[col]].copy()
            df['row_id'] = np.arange(len(df))
            exploded = df.explode(col).reset_index(drop=True)

            # Use mappings computed in fit
            mapping = self.mappings.get(col, pd.Series())
            exploded['enc'] = exploded[col].map(mapping).fillna(self.global_mean)

            encoded = exploded.groupby('row_id')['enc'].agg(self.agg)
            transformed_cols.append(encoded.values.reshape(-1, 1))

        return np.hstack(transformed_cols)


            

