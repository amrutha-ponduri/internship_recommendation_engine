import os
import pandas as pd
from sklearn.compose import ColumnTransformer
from sklearn.preprocessing import OneHotEncoder, StandardScaler
from sklearn.linear_model import LogisticRegression
from sklearn.pipeline import Pipeline
import joblib


from recommend_internship.ml.custom_encoders.MultiLabelBinarierWrapper import MultiLabelBinarizerWrapper


from recommend_internship.ml.custom_encoders.KFoldTargetEncoder import KFoldTargetEncoder
from recommend_internship.models import SelectionRecords
from sklearn.model_selection import cross_val_score


BASE_DIR = os.path.dirname(__file__)  # points to recommend_internship/ml
MODEL_PATH = os.path.join(BASE_DIR, "model.pkl")



def train_model():
    qs = SelectionRecords.objects.all().values()
    data = pd.DataFrame(qs)

    # normalize empty strings
    for col in data.select_dtypes(include="object"):
        data[col] = data[col].replace(r"^\s*$", pd.NA, regex=True)


    # skill cols
    skills_cols = ['internship_required_skills', 'user_skills']

    # categorical handling
    internship_cols = ['internship_field', 'internship_sector']
    data[internship_cols] = data[internship_cols].fillna('Not Specified')

    # floats
    numerical_cols = [
        'max_exp_gap',
        'min_exp_gap',
    ]

    for col in numerical_cols:
        data[col] = pd.to_numeric(data[col], errors='coerce')
        data[col] = data[col].fillna(data[col].median())

    # is experience in range missing values
    data['is_exp_in_range'] = pd.to_numeric(data['is_exp_in_range'], errors="coerce")
    data['is_exp_in_range'] = data['is_exp_in_range'].fillna(data['is_exp_in_range'].mode()[0])

    # drop rows with missing target
    data = data.dropna(subset=['selection_status'])


    cat_cols = [
        'internship_field',
        'internship_sector'
    ]

    for col in cat_cols:
        data[col] = data[col].astype('category')

    X = data.drop(columns=['selection_status'])
    y = data['selection_status']

    preprocessor = ColumnTransformer(
    [
        ('skills', MultiLabelBinarizerWrapper(cols=skills_cols), skills_cols),
        ('num', StandardScaler(), numerical_cols + ['is_exp_in_range']),
        ('cat_cols', KFoldTargetEncoder(cols=cat_cols), cat_cols)
    ]
)

    pipeline = Pipeline([
        ('preprocessor', preprocessor),
        ('model', LogisticRegression(
            max_iter=1000,
            solver='liblinear'
        ))
    ])


    scores = cross_val_score(pipeline, X, y, cv=5, scoring='accuracy')

    pipeline.fit(X, y)

    joblib.dump(pipeline, MODEL_PATH)

    print("Accuracy:", scores.mean())
    return scores.mean()
