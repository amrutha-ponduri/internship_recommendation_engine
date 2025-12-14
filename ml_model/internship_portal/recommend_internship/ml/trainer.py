import os
import pandas as pd
from sklearn.compose import ColumnTransformer
from sklearn.discriminant_analysis import StandardScaler
from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import train_test_split
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import MultiLabelBinarizer
import joblib
from .custom_encoders.KFoldTargetEncoder import KFoldTargetEncoder
from .custom_encoders.MultiValueKFoldEncoder import MultiValueKFoldEncoder
from recommend_internship.models import Application
from sklearn.model_selection import cross_val_score


BASE_DIR = os.path.dirname(__file__)  # points to recommend_internship/ml
MODEL_PATH = os.path.join(BASE_DIR, "model.pkl")
MLB_SKILLS_PATH = os.path.join(BASE_DIR, "mlb_skills.pkl")
MLB_REQ_PATH = os.path.join(BASE_DIR, "mlb_req.pkl")
FEATURES_PATH = os.path.join(BASE_DIR, "feature_cols.pkl")



def train_model():
    # Load data from DB
    qs = Application.objects.all().values()
    data = pd.DataFrame(qs)
    X = data.drop(columns = ['id', 'is_selected'], axis = 1)
    y = data['is_selected']
    # use pipe line to preprocess, split and train
    categorical_cols = ["gender", "user_location", "internship_location", "company", "sector", "role"]
    numerical_cols = ["age", "graduation_year", "gpa", "experience"]
    multi_hot_cols = ["user_skills", "required_skills"]

# -----------------------------------------------------------------------------------------------------------
    # preprocess --> K fold target encoder for categorical_cols, 
    # StandardScaler for numerical_cols and Target encoder with data explode for multi_hot_cols
# ------------------------------------------------------------------------------------------------------------

    preprocessor = ColumnTransformer(
        transformers = [
            ('cat', KFoldTargetEncoder(cols = categorical_cols), categorical_cols), 
            ('multi', MultiValueKFoldEncoder(cols = multi_hot_cols), multi_hot_cols),
            ('num', StandardScaler(), numerical_cols)]
    )

    pipeline = Pipeline(
        [('preprocessor', preprocessor), ('model', LogisticRegression())]
    )

    pipeline.fit(X, y)

    joblib.dump(pipeline, MODEL_PATH)

    scores = cross_val_score(pipeline, X, y, cv=5, scoring='accuracy')
    print("CV accuracy scores:", scores)
    print("Mean CV accuracy:", scores.mean())


    return scores.mean()